package app.feelio.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "회원가입 응답 데이터")
public record SignUpRes (
	@Schema(
		description = "생성된 사용자 고유 ID",
		example = "1",
		type = "integer"
	)
	Long userId,

	@Schema(
		description = "가입된 이메일",
		example = "user@feelio.com",
		type = "string"
	)
	String email,

	@Schema(
		description = "등록된 닉네임",
		example = "행복한필리오",
		type = "string"
	)
	String nickName
){}