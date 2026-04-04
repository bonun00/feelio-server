package app.feelio.dto.diary;

import java.time.LocalDate;
import java.util.List;

public record ReportRes(
	int year,
	int month,
	Summary summary,
	List<EmotionStat> emotionStats
) {
	public record Summary(
		EmotionDetail mainEmotion,
		EmotionDetail rareEmotion,
		String aiFeedback
	) {}

	public record EmotionDetail(
		String emotion,
		String status,
		Integer differencePercentage,
		LocalDate date
	) {}

	public record EmotionStat(
		String emotion,
		int count,
		int percentage
	) {}
}
