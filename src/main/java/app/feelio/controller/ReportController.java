package app.feelio.controller;

import java.time.YearMonth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import app.feelio.dto.diary.ReportRes;
import app.feelio.service.ReportService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
public class ReportController {

	private final ReportService reportService;

	@GetMapping
	public ResponseEntity<?> getMonthlyReport(
		@RequestHeader("userId") Long userId,
		@RequestParam("date") String date // "2026-04" 형식
	) {
		YearMonth yearMonth = YearMonth.parse(date);
		ReportRes response = reportService.generateMonthlyReport(userId, yearMonth);
		return ResponseEntity.ok(response);

	}
}
