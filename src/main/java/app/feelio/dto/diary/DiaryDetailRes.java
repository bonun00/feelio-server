package app.feelio.dto.diary;

import app.feelio.domain.analysis.AIAnalysis;
import app.feelio.domain.diary.Diary;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "일기 상세 조회 응답 데이터")
public record DiaryDetailRes(
	@Schema(description = "일기 본문 내용", example = "오늘 프로젝트 배포를 성공적으로 마쳤다!")
	String content,

	@Schema(description = "분석된 감정 (문자열 또는 Emotion 객체)", example = "행복")
	Object emotion,

	@Schema(description = "감정 점수 (0~100)", example = "92")
	Integer score,

	@Schema(description = "AI가 작성한 상세 분석 코멘트",
		example = "어려운 과제를 해결하며 성장하는 모습이 정말 멋져요.")
	String comment,
	@Schema(description = "분석 상태 (PROCESSING: 분석 중, COMPLETED: 완료)", example = "PROCESSING")
	String status,

	boolean userCheck
) {
	public DiaryDetailRes(String content, Object emotion, Integer score, String comment,boolean userCheck) {
		this(content, emotion, score, comment, (comment == null) ? "PROCESSING" : "COMPLETED", userCheck);
	}
	public static DiaryDetailRes from(Diary diary, AIAnalysis analysis) {
		String status = (analysis == null) ? "PROCESSING" : "COMPLETED";
		return new DiaryDetailRes(
			diary.getContent(),
			analysis != null ? analysis.getEmotion() : null,
			analysis != null ? analysis.getEmotionScore() : null,
			analysis != null ? analysis.getAiComment() : null,
			status,
			diary.isUserChecked()
		);
	}
}