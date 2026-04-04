package app.feelio.dto.analysis;

import app.feelio.domain.analysis.Emotion;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사용자 감정 분석 결과 수정 요청 (선택적 수정 가능)")
public record AnalysisUpdateReq(
	@Schema(description = "수정하려는 감정 종류 (수정하지 않을 경우 생략 가능)",
		implementation = Emotion.class, example = "뿌듯함")
	Emotion emotion,

	@Schema(description = "수정하려는 감정 점수 (0~100, 수정하지 않을 경우 생략 가능)",
		example = "95")
	Integer score
) {}