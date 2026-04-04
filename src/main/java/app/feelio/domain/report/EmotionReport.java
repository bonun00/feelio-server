package app.feelio.domain.report;

import java.time.LocalDate;
import java.time.LocalDateTime;

import app.feelio.domain.user.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;


@Entity
@Getter
@Table(name = "emotion_report")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class EmotionReport {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	private int year;
	private int month;

	// 메인 감정 관련 (null 허용을 위해 Integer/LocalDate 사용)
	private String mainEmotion;
	private String mainStatus;            // UP, DOWN, SAME, null
	private Integer mainDifference;       // 전달 데이터 없으면 null
	private LocalDate bestDay;

	// 희소 감정 관련
	private String rareEmotion;
	private String rareStatus;            // UP, DOWN, SAME, null
	private Integer rareDifference;       // 전달 데이터 없으면 null
	private LocalDate worstDay;

	@Column(columnDefinition = "TEXT")
	private String aiFeedback;

	// 리포트에 포함된 감정별 상세 수치 (1:N)
	@OneToMany(mappedBy = "report", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<EmotionStatDetail> stats;

	private LocalDateTime createdAt;
}