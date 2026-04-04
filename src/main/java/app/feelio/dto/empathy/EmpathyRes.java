package app.feelio.dto.empathy;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "공감글 상세 응답 데이터")
public record EmpathyRes(
	@Schema(description = "생성된 공감글의 고유 ID", example = "1")
	Long empathyId,

	@Schema(description = "저장된 공감글의 본문 내용", example = " 힘내세요!")
	String content
) {
}