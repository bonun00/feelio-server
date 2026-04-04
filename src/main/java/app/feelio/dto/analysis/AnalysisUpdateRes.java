package app.feelio.dto.analysis;

import java.time.LocalDateTime;
import app.feelio.domain.analysis.Emotion;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "감정 분석 결과 수정 성공 응답")
public record AnalysisUpdateRes(
	@Schema(description = "수정된 최종 감정", implementation = Emotion.class)
	Emotion emotion,

	@Schema(description = "수정된 최종 점수 (0~100)", example = "95")
	int score,

	@Schema(description = "기존 AI 코멘트 (유지됨)", example = "오늘 하루 정말 알차게 보내셨네요!")
	String comment,

	@Schema(description = "수정 완료 일시", example = "2026-04-04T19:10:00")
	LocalDateTime updateAt
) {}