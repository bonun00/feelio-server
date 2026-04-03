package app.feelio.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import app.feelio.domain.user.User;
import app.feelio.domain.user.UserRepo;
import app.feelio.dto.user.SignInReq;
import app.feelio.dto.user.SignInRes;
import app.feelio.dto.user.SignUpRes;
import app.feelio.dto.user.SignUpReq;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepo userRepository;

	@Transactional
	public SignUpRes signUp(SignUpReq request) {

		userRepository.findByEmail(request.email())
			.ifPresent(u -> { throw new IllegalStateException("이미 존재하는 이메일입니다."); });

		User user = User.builder()
			.email(request.email())
			.password(request.password())
			.nickName(request.nickName())
			.build();

		userRepository.save(user);

		return SignUpRes.builder()
			.userId(user.getId())
			.email(user.getEmail())
			.nickName(user.getNickName())
			.build();
	}


	@Transactional(readOnly = true)
	public SignInRes signIn(SignInReq req) {

		User user = userRepository.findByEmail(req.email())
			.orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다."));

		if (!user.getPassword().equals(req.password())) {
			throw new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다.");
		}

		return SignInRes.builder()
			.userId(user.getId())
			.email(user.getEmail())
			.nickName(user.getNickName())
			.build();
	}
}