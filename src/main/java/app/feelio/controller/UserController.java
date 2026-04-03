package app.feelio.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.feelio.dto.user.SignInReq;
import app.feelio.dto.user.SignInRes;
import app.feelio.dto.user.SignUpReq;
import app.feelio.dto.user.SignUpRes;

import app.feelio.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
;

@Tag(name = "User", description = "사용자 인증 및 관리 API")
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

	final UserService userService;

	@PostMapping("/sign-up")
	@Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다.")
	public ResponseEntity<SignUpRes> signUp(@RequestBody SignUpReq req) {
		return ResponseEntity.ok(userService.signUp(req));
	}

	@PostMapping("/sign-in")
	@Operation(summary = "로그인", description = "이메일과 비밀번호로 로그인을 진행합니다.")
	public ResponseEntity<SignInRes> signIn(@RequestBody SignInReq req) {
		return ResponseEntity.ok(userService.signIn(req));
	}

}
