package app.feelio.domain.diary;

import java.time.LocalDateTime;

import app.feelio.domain.analysis.Emotion;

public interface SimilarDiaryProjection {
	String getContent();
	Emotion getEmotion();
	LocalDateTime getCreatedAt();
	Double getSimilarity();

}
