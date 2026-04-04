package app.feelio.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.feelio.domain.cheer.EmpathyMessage;
import app.feelio.domain.cheer.EmpathyMessageRepo;
import app.feelio.domain.diary.Diary;
import app.feelio.domain.diary.DiaryRepo;
import app.feelio.domain.user.User;
import app.feelio.domain.user.UserRepo;
import app.feelio.dto.empathy.EmpathyRes;
import app.feelio.dto.empathy.EmpathySaveReq;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmpathyService {

	private final EmpathyMessageRepo empathyMessageRepository;
	private final UserRepo userRepo;
	private final DiaryRepo diaryRepo;

	@Transactional(readOnly = true)
	public List<EmpathyRes> getMatchedEmpathy(Long userId, Long diaryId) {
		List<EmpathyMessage> results = empathyMessageRepository.findTop5SimilarMessages(userId, diaryId);
		if (results.isEmpty()) {
			return List.of(new EmpathyRes(
				null,
				"당신의 마음을 가장 잘 이해할 이웃을 찾고 있어요. 조금만 기다려주세요!"
			));
		}
		return results.stream()
			.map(res -> new EmpathyRes(
						res.getId(),
				res.getContent()
			))
			.collect(Collectors.toList());
	}

	@Transactional
	public EmpathyRes saveEmpathyMessage(Long userId, Long diaryId,EmpathySaveReq request) {

		User user=userRepo.findById(userId).orElseThrow(()->new IllegalArgumentException("존재하지 않는 사용자 입니다"));

		Diary diary=diaryRepo.findById(diaryId).orElseThrow(()->new IllegalStateException("해당하는 일기가 없습니다."));
		EmpathyMessage newMessage = EmpathyMessage.builder()
			.diary(diary)
			.content(request.content())
			.build();
		empathyMessageRepository.save(newMessage);
		return EmpathyRes.builder().empathyId(newMessage.getId()).content(newMessage.getContent()).build();
	}

}
