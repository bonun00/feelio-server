package app.feelio.domain.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, nullable = false, length = 50)
	private String email;

	@Column(nullable = false, length = 50)
	private String password;

	@Column(nullable = false, length = 10)
	private String nickName;

	@Builder
	public User(String email, String password, String nickName) {
		this.email = email;
		this.password = password;
		this.nickName = nickName;
	}


}