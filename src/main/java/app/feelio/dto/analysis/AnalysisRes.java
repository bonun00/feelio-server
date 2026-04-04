package app.feelio.dto.analysis;

import app.feelio.domain.analysis.Emotion;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "AI 일기 분석 응답 상세 정보")
public record AnalysisRes(
	@Schema(description = "감정 점수 (0~100)", example = "88")
	int score,

	@Schema(description = "분석된 대표 감정", implementation = Emotion.class)
	Emotion emotion,

	@Schema(description = "AI가 작성한 분석 코멘트",
		example = "지난번 프로젝트 고민 때보다 훨씬 밝아진 모습이 보기 좋아요!")
	String comment,

	@Schema(description = "과거 맥락(유사 일기) 참조 여부", example = "true")
	boolean isContextUsed
) {}