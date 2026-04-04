package app.feelio.dto.diary;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;

@Schema(description = "월별 캘린더 조회 응답 데이터")
public record CalendarRes(
	@Schema(description = "조회된 연도", example = "2026")
	int year,

	@Schema(description = "조회된 월", example = "4")
	int month,

	@Schema(description = "해당 월의 1일부터 말일까지의 상세 정보 리스트")
	List<DayDetail> days
) {
	@Schema(description = "캘린더 내 개별 날짜 정보")
	public record DayDetail(
		@Schema(description = "날짜", example = "2026-04-04")
		LocalDate date,

		@Schema(description = "작성된 일기 ID (일기가 없으면 null)", example = "53")
		Long diaryId,

		@Schema(description = "해당 날짜의 감정 (일기가 없으면 null)", example = "행복")
		String emotion
	) {}
}