package app.feelio.controller;

import java.time.LocalDate;
import java.util.List;

import app.feelio.dto.analysis.AnalysisUpdateReq;
import app.feelio.dto.analysis.AnalysisUpdateRes;
import app.feelio.dto.diary.DiaryReq;
import app.feelio.dto.diary.DiaryRes;
import app.feelio.dto.diary.CalendarRes;
import app.feelio.dto.diary.DiaryDetailRes;
import app.feelio.service.AnalysisService;
import app.feelio.service.DiaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Diary", description = "일기 저장, 조회 및 AI 분석 관리 API")
@RestController
@RequestMapping("/api/v1/diaries")
@RequiredArgsConstructor
public class DiaryController {

	private final AnalysisService analysisService;
	private final DiaryService diaryService;

	@PostMapping
	@Operation(summary = "일기 작성 및 AI 분석 요청",
		description = "일기를 즉시 저장하고, 백그라운드에서 AI 분석을 시작합니다. 저장된 일기의 날짜를 반환합니다.")
	public ResponseEntity<DiaryRes> analyzeDiary(
		@RequestHeader("userId") Long userId,
		@RequestBody DiaryReq request) {

		DiaryRes result = analysisService.saveDiaryAndTriggerAnalysis(userId, request.content(), request.date(),request.analysisType());
		return ResponseEntity.ok(result);
	}

	@GetMapping("/{date}")
	@Operation(summary = "특정 날짜 일기 상세 조회",
		description = "해당 날짜에 작성된 일기 본문과 AI 분석 결과(감정, 점수, 코멘트) 리스트를 조회합니다.")
	public ResponseEntity<DiaryDetailRes> getDiary(
		@Parameter(description = "조회할 날짜 (예: 2026-04-04)", example = "2026-04-04")
		@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
		@RequestHeader("userId") Long userId
	) {
		DiaryDetailRes res = diaryService.getDiaryByDate(userId, date);
		return ResponseEntity.ok(res);
	}

	@GetMapping
	@Operation(summary = "월별 캘린더 데이터 조회",
		description = "특정 월(year-month)의 모든 날짜별 일기 작성 여부와 감정 이콘을 조회합니다.")
	public ResponseEntity<CalendarRes> getCalendar(
		@Parameter(description = "조회할 년-월 (예: 2026-04)", example = "2026-04")
		@RequestParam String month,
		@RequestHeader("userId") Long userId
	) {
		String[] parts = month.split("-");
		int year = Integer.parseInt(parts[0]);
		int targetMonth = Integer.parseInt(parts[1]);

		CalendarRes res = diaryService.getMonthCalendar(userId, year, targetMonth);
		return ResponseEntity.ok(res);
	}

	@PatchMapping("/{diaryId}/analysis")
	@Operation(summary = "AI 분석 결과 수동 수정",
		description = "AI가 분석한 감정이나 점수를 사용자가 직접 수정합니다. 수정된 필드만 반영됩니다.")
	public ResponseEntity<AnalysisUpdateRes> updateDiaryAnalysis(
		@PathVariable Long diaryId,
		@RequestHeader("userId") Long userId,
		@RequestBody AnalysisUpdateReq request
	) {
		AnalysisUpdateRes res = analysisService.updateAnalysis(userId, diaryId, request);
		return ResponseEntity.ok(res);
	}
}