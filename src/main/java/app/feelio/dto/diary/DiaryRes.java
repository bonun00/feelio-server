package app.feelio.dto.diary;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.Builder;

@Builder
@Schema(description = "일기 저장 성공 응답 데이터")
public record DiaryRes(

	@Schema(description = "일기 본문 내용", example = "오늘 드디어 프로젝트의 비동기 처리를 완성했다! 4초 걸리던 응답이 0.1초로 줄어들어서 정말 기쁘다.")
	Long diaryId,
	@Schema(description = "저장된 일기의 작성 날짜", example = "2026-04-04")
	LocalDate date
) { }