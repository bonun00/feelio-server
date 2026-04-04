package app.feelio.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.feelio.domain.diary.DiaryRepo;
import app.feelio.domain.report.EmotionReport;
import app.feelio.domain.report.EmotionReportRepo;
import app.feelio.domain.user.User;
import app.feelio.domain.user.UserRepo;
import app.feelio.dto.diary.ReportRes;
import lombok.RequiredArgsConstructor;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {
	private final DiaryRepo diaryRepo;
	private final UserRepo userRepo;
	private final EmotionReportRepo reportRepo;

	private final AiAnalysisManager aiService;

	@Transactional
	public ReportRes generateMonthlyReport(Long userId, YearMonth currentMonth) {

		return reportRepo.findByUserIdAndYearAndMonth(userId, currentMonth.getYear(), currentMonth.getMonthValue())
			.map(this::convertToRes)
			.orElseGet(() -> createAndSaveReport(userId, currentMonth));
	}

	private ReportRes createAndSaveReport(Long userId, YearMonth currentMonth) {
		User user = userRepo.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자 입니다"));

		YearMonth lastMonth = currentMonth.minusMonths(1);
		Map<String, Integer> currentStats = getEmotionCounts(userId, currentMonth);
		Map<String, Integer> lastStats = getEmotionCounts(userId, lastMonth);

		int lastMonthTotalCount = lastStats.values().stream().mapToInt(Integer::intValue).sum();
		boolean hasLastMonthData = lastMonthTotalCount > 0;

		String mainName = findTopEmotion(currentStats);
		String rareName = findBottomEmotion(currentStats);

		EmotionTrend mainTrend = calculateTrend(mainName, currentStats, lastStats, hasLastMonthData);
		EmotionTrend rareTrend = calculateTrend(rareName, currentStats, lastStats, hasLastMonthData);

		LocalDate bestDay = getEmotionDate(userId, mainName, currentMonth);
		LocalDate worstDay = getEmotionDate(userId, rareName, currentMonth);


		String aiFeedback = generateFeedbackByStats(currentStats, lastStats, mainName);


		EmotionReport report = EmotionReport.builder()
			.user(user)
			.year(currentMonth.getYear())
			.month(currentMonth.getMonthValue())
			.mainEmotion(mainName)
			.mainStatus(mainTrend.status())
			.mainDifference(mainTrend.diff())
			.bestDay(bestDay)
			.rareEmotion(rareName)
			.rareStatus(rareTrend.status())
			.rareDifference(rareTrend.diff())
			.worstDay(worstDay)
			.aiFeedback(aiFeedback)
			.createdAt(LocalDateTime.now())
			.build();

		reportRepo.save(report);

		return convertToRes(report, currentStats);
	}


	public String generateFeedbackByStats(Map<String, Integer> currentStats, Map<String, Integer> lastStats, String mainName) {
		// 1. 데이터를 문자열로 가공
		String statsData = currentStats.entrySet().stream()
			.map(e -> String.format("- %s: %d회", e.getKey(), e.getValue()))
			.collect(Collectors.joining("\n"));

		// 2. 페르소나와 분석 가이드를 담은 프롬프트
		String prompt = String.format("""
        너는 심리 통계 분석 전문가이자 따뜻한 상담가야.
        사용자의 이번 달 감정 통계 데이터를 보고 '마음 날씨 리포트'를 작성해줘.
        
        [이번 달 감정 통계]
        %s
        
        [주요 특징]
        - 가장 많이 느낀 감정: %s
        
        [작성 규칙]
        1.따뜻하고 격려하는 말투로 1문장으로 짧게 평가해줘 100자로 평가해줘
      
        """, statsData, mainName);

		return aiService.callGemini(prompt); // Gemini API 호출
	}

	// Entity를 응답 DTO로 변환
	private ReportRes convertToRes(EmotionReport report, Map<String, Integer> currentStats) {
		return new ReportRes(
			report.getYear(),
			report.getMonth(),
			new ReportRes.Summary(
				new ReportRes.EmotionDetail(report.getMainEmotion(), report.getMainStatus(), report.getMainDifference(), report.getBestDay()),
				new ReportRes.EmotionDetail(report.getRareEmotion(), report.getRareStatus(), report.getRareDifference(), report.getWorstDay()),
				report.getAiFeedback()
			),
			buildStatsList(currentStats)
		);
	}

	// DB에서 바로 가져올 때를 위한 오버로딩 (Stats는 다시 계산 필요할 수 있음)
	private ReportRes convertToRes(EmotionReport report) {
		Map<String, Integer> currentStats = getEmotionCounts(report.getUser().getId(),
			YearMonth.of(report.getYear(), report.getMonth()));
		return convertToRes(report, currentStats);
	}
	/**
	 * 감정별 증감 추이를 계산하는 공통 메서드
	 */
	private EmotionTrend calculateTrend(String emotionName, Map<String, Integer> currentStats,
		Map<String, Integer> lastStats, boolean hasLastData) {
		if (!hasLastData || emotionName == null) {
			return new EmotionTrend(null, null); // 전달 데이터 없으면 null, null 반환
		}

		int currentPct = calculatePercentage(currentStats, emotionName);
		int lastPct = calculatePercentage(lastStats, emotionName);

		int diff = Math.abs(currentPct - lastPct);
		String status;
		if (currentPct > lastPct) status = "UP";
		else if (currentPct < lastPct) status = "DOWN";
		else status = "SAME";

		return new EmotionTrend(status, diff);
	}

	// 헬퍼용 Record
	private record EmotionTrend(String status, Integer diff) {}

	// 날짜 조회 헬퍼
	private LocalDate getEmotionDate(Long userId, String emotionName, YearMonth month) {
		if (emotionName == null) return null;

		LocalDateTime start = month.atDay(1).atStartOfDay(); // 2026-03-01 00:00:00
		LocalDateTime nextMonthStart = month.plusMonths(1).atDay(1).atStartOfDay(); // 2026-04-01 00:00:00

		return diaryRepo.findTopEmotionDate(userId, emotionName, start, nextMonthStart)
			.map(LocalDateTime::toLocalDate)
			.filter(date -> YearMonth.from(date).equals(month)) // 방어 로직: 3월인지 다시 확인
			.orElse(null);
	}

	// 감정별 통계 리스트 빌드 로직
	private List<ReportRes.EmotionStat> buildStatsList(Map<String, Integer> stats) {
		int total = stats.values().stream().mapToInt(Integer::intValue).sum();
		return stats.entrySet().stream()
			.map(entry -> new ReportRes.EmotionStat(
				entry.getKey(),
				entry.getValue(),
				total == 0 ? 0 : (entry.getValue() * 100) / total
			))
			.collect(Collectors.toList());
	}

	// 유틸리티: 가장 많이 나타난 감정 찾기
	private String findTopEmotion(Map<String, Integer> stats) {
		return stats.entrySet().stream()
			.max(Map.Entry.comparingByValue())
			.map(Map.Entry::getKey)
			.orElse(null);
	}

	// 유틸리티: 가장 적게 나타난 감정 찾기
	private String findBottomEmotion(Map<String, Integer> stats) {
		return stats.entrySet().stream()
			.min(Map.Entry.comparingByValue())
			.map(Map.Entry::getKey)
			.orElse(null);
	}

	@Transactional(readOnly = true)
	public Map<String, Integer> getEmotionCounts(Long userId, YearMonth month) {
		LocalDateTime start = month.atDay(1).atStartOfDay();
		LocalDateTime end = month.atEndOfMonth().atTime(LocalTime.MAX);

		List<Object[]> results = diaryRepo.countEmotionsByMonth(userId, start, end);

		return results.stream().collect(Collectors.toMap(
			res -> res[0].toString(),
			res -> ((Long) res[1]).intValue()
		));
	}

	private int calculatePercentage(Map<String, Integer> stats, String emotion) {
		int total = stats.values().stream().mapToInt(Integer::intValue).sum();
		if (total == 0) return 0;
		return (stats.getOrDefault(emotion, 0) * 100) / total;
	}
}