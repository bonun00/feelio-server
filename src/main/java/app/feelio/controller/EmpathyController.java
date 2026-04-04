package app.feelio.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.feelio.dto.empathy.EmpathyRes;
import app.feelio.dto.empathy.EmpathySaveReq;
import app.feelio.service.EmpathyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import java.util.List;

@Tag(name = "Empathy", description = "공감 및 위로 메시지 관련 API")
@RestController
@RequestMapping("/api/v1/empathy")
@RequiredArgsConstructor
public class EmpathyController {

	private final EmpathyService empathyService;

	@Operation(
		summary = "유사 경험 공감글 조회",
		description = "내 일기(diaryId)와 유사한 경험을 가진 타인의 공감글 5개를 조회합니다."
	)
	@GetMapping("/{diaryId}")
	public ResponseEntity<List<EmpathyRes>> getMatchedMessage(
		@RequestHeader("userId") Long userId,
		@PathVariable Long diaryId) {


		return ResponseEntity.ok(empathyService.getMatchedEmpathy(userId, diaryId));
	}

	@Operation(
		summary = "본인 공감글 저장",
		description = "내 일기(diaryId)에 담긴 감정과 경험을 바탕으로 다른 이웃에게 보낼 공감글을 저장합니다."
	)
	@PostMapping("/{diaryId}")
	public ResponseEntity<EmpathyRes> saveUserEmpathy(
		@RequestHeader("userId") Long userId,
		@RequestBody EmpathySaveReq request,
		@PathVariable Long diaryId) {

		return ResponseEntity.ok(empathyService.saveEmpathyMessage(userId,diaryId, request));
	}
}