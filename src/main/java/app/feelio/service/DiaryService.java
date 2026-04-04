package app.feelio.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.feelio.domain.analysis.AIAnalysis;
import app.feelio.domain.analysis.AIAnalysisRepo;
import app.feelio.domain.diary.CalendarDataProjection;
import app.feelio.domain.diary.Diary;
import app.feelio.domain.diary.DiaryRepo;
import app.feelio.dto.diary.CalendarRes;
import app.feelio.dto.diary.DiaryDetailRes;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiaryService {
	private final DiaryRepo diaryRepo;
	private final AIAnalysisRepo aiAnalysisRepo;

	public List<DiaryDetailRes> getDiaryByDate(Long userId, LocalDate date) {
		LocalDateTime start = date.atStartOfDay(); // 2026-04-04 00:00:00
		LocalDateTime end = date.atTime(LocalTime.MAX); // 2026-04-04 23:59:59.999...

		List<Diary> diaries = diaryRepo.findDiariesByUserIdAndDateRange(userId, start, end);

		return diaries.stream()
			.map(diary -> DiaryDetailRes.from(diary, diary.getAnalysis()))
			.toList();
	}

	public CalendarRes getMonthCalendar(Long userId, int year, int month) {
		LocalDate start = LocalDate.of(year, month, 1);
		LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

		Map<LocalDate, CalendarDataProjection> diaryMap = diaryRepo.findMonthData(
				userId, start.atStartOfDay(), end.atTime(LocalTime.MAX))
			.stream()
			.collect(Collectors.toMap(CalendarDataProjection::getDate, d -> d, (ex, rep) -> ex));

		List<CalendarRes.DayDetail> days = new ArrayList<>();
		for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
			CalendarDataProjection data = diaryMap.get(date);
			days.add(new CalendarRes.DayDetail(
				date,
				data != null ? data.getDiaryId() : null,
				data != null ? data.getEmotion() : null
			));
		}

		return new CalendarRes(year, month, days);
	}

}