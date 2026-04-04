package app.feelio.domain.diary;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnTransformer;

import app.feelio.domain.analysis.AIAnalysis;
import app.feelio.domain.user.User;
import app.feelio.global.converter.VectorConverter;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "diary")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Diary {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(columnDefinition = "TEXT", nullable = false)
	private String content;

	@OneToOne(mappedBy = "diary", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private AIAnalysis analysis;

	@Convert(converter = VectorConverter.class)
	@Column(columnDefinition = "vector(768)")
	@ColumnTransformer(write = "?::vector")
	private float[] embedding;

	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt = LocalDateTime.now();

	@Builder
	public Diary(User user, String content, float[] embedding) {
		this.user = user;
		this.content = content;
		this.embedding = embedding;
		this.createdAt = LocalDateTime.now();
	}
}