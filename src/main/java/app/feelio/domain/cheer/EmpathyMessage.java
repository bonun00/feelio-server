package app.feelio.domain.cheer;

import java.time.LocalDateTime;

import app.feelio.domain.analysis.Emotion;
import app.feelio.domain.diary.Diary;
import app.feelio.domain.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class EmpathyMessage {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "diary_id", referencedColumnName = "id", nullable = false)
	private Diary diary;

	@Column(columnDefinition = "TEXT", nullable = false)
	private String content;

	@Column(name = "created_at")
	private LocalDateTime createdAt = LocalDateTime.now();


	@Builder
	public EmpathyMessage(Diary diary, String content) {
		this.diary = diary;
		this.content = content;
	}


}
