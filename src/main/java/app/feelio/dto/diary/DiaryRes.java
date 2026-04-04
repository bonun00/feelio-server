package app.feelio.dto.diary;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.Builder;

@Builder
@Schema(description = "일기 저장 성공 응답 데이터")
public record DiaryRes(
	@Schema(description = "저장된 일기의 작성 날짜", example = "2026-04-04")
	LocalDate date
) { }