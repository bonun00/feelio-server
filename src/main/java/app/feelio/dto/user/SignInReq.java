package app.feelio.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "로그인 요청 데이터")
public record SignInReq(
	@Schema(
		description = "사용자 이메일 주소",
		example = "user@feelio.com",
		type = "string",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	String email,

	@Schema(
		description = "사용자 비밀번호",
		example = "password1234",
		type = "string",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	String password
) {}