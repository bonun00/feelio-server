package app.feelio.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "로그인 응답 데이터")
public record SignInRes (
	@Schema(
		description = "사용자 고유 식별자 (ID)",
		example = "1",
		type = "integer",
		format = "int64"
	)
	Long userId,

	@Schema(
		description = "사용자 이메일",
		example = "user@feelio.com",
		type = "string"
	)
	String email,

	@Schema(
		description = "사용자 닉네임",
		example = "행복한필리오",
		type = "string"
	)
	String nickName
){}