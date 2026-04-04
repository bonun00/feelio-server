package app.feelio.domain.cheer;

import java.time.LocalDateTime;

import app.feelio.domain.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class CheerMessage {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(nullable = false)
	private String emotion;

	@Column(columnDefinition = "TEXT", nullable = false)
	private String content;



	@Column(name = "created_at")
	private LocalDateTime createdAt = LocalDateTime.now();


	@Builder
	public CheerMessage(User user, String content ,String emotion) {
		this.user = user;
		this.content = content;
		this.emotion = emotion;
	}


}
