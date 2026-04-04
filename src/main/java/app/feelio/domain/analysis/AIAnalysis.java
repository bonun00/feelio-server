package app.feelio.domain.analysis;

import app.feelio.domain.diary.Diary;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
;
@Entity
@Table(name = "ai_analysis")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AIAnalysis {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "diary_id", nullable = false)
	private Diary diary;

	@Column(nullable = false)
	private Integer emotionScore;

	@Column(nullable = false, length = 100)
	@Enumerated(EnumType.STRING)
	private Emotion emotion;

	@Column(columnDefinition = "TEXT", nullable = false)
	private String aiComment;

	@Builder
	public AIAnalysis(Diary diary, Integer emotionScore, Emotion emotion, String aiComment) {
		this.diary = diary;
		this.emotionScore = emotionScore;
		this.emotion = emotion;
		this.aiComment = aiComment;
	}
	public void updateResult(Emotion emotion, Integer score) {
		if (emotion != null) {
			this.emotion = emotion;
		}
		if (score != null) {
			this.emotionScore = score;
		}
	}

}