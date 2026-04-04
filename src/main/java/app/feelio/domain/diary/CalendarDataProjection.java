package app.feelio.domain.diary;

import java.time.LocalDate;

public interface CalendarDataProjection {
	Long getDiaryId();
	LocalDate getDate();
	String getEmotion();
}
