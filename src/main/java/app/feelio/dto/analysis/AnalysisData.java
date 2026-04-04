package app.feelio.dto.analysis;

import app.feelio.domain.analysis.Emotion;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "AI 감정 분석 결과 데이터")
public record AnalysisData(
	@Schema(description = "분석된 감정 종류", example = "행복", implementation = Emotion.class)
	Emotion emotion,

	@Schema(description = "감정 점수 (0~100)", example = "85")
	Integer score,

	@Schema(description = "AI가 작성한 맞춤형 코멘트", example = "오늘 하루 정말 알차게 보내셨네요!")
	String comment
) {
}