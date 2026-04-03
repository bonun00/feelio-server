package app.feelio.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "회원가입 요청 데이터")
public record SignUpReq (
	@Schema(
		description = "사용자 이메일 주소",
		example = "user@feelio.com",
		type = "string",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	String email,

	@Schema(
		description = "비밀번호",
		example = "password1234",
		type = "string",
		format = "password",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	String password,

	@Schema(
		description = "사용할 닉네임 (2~10자)",
		example = "행복한필리오",
		type = "string",
		requiredMode = Schema.RequiredMode.REQUIRED
	)
	String nickName
){}