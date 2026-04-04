package app.feelio.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import app.feelio.domain.analysis.AIAnalysis;
import app.feelio.domain.analysis.AIAnalysisRepo;
import app.feelio.domain.diary.Diary;
import app.feelio.domain.diary.DiaryRepo;
import app.feelio.domain.user.User;
import app.feelio.domain.user.UserRepo;
import app.feelio.dto.analysis.AnalysisType;
import app.feelio.dto.analysis.AnalysisUpdateReq;
import app.feelio.dto.analysis.AnalysisUpdateRes;
import app.feelio.dto.diary.DiaryRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalysisService {

	private final EmbeddingModel embeddingModel;
	private final DiaryRepo diaryRepository;
	private final UserRepo userRepo;
	private final AsyncAnalysis async;
	private final AIAnalysisRepo aiAnalysisRepo;

	@Transactional
	public DiaryRes saveDiaryAndTriggerAnalysis(Long userId, String content,LocalDate date, AnalysisType type) {
		Diary diary = saveDiary(userId, content,date);

		TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
			@Override
			public void afterCommit() {
				async.processAiAnalysisAsync(diary, content, userId, type);
			}
		});


		return DiaryRes.builder().diaryId(diary.getId()).date(diary.getCreatedAt().toLocalDate()).build();
	}

	@Transactional
	public Diary saveDiary(Long userId, String content, LocalDate createdAt) {
		User user = userRepo.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자 입니다"));

		diaryRepository.findDiaryByDate(userId, createdAt)
			.ifPresent(d -> {
				throw new IllegalStateException("오늘 일기를 이미 작성했습니다.");
			});

		float[] embedding = embeddingModel.embed(content);
		Diary diary = Diary.builder()
			.user(user)
			.content(content)
			.embedding(embedding).localDate(createdAt)
			.build();

		return diaryRepository.save(diary);
	}


	@Transactional
	public AnalysisUpdateRes updateAnalysis(Long userId, Long diaryId, AnalysisUpdateReq req) {

		userRepo.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자 입니다"));

		AIAnalysis analysis = aiAnalysisRepo.findByDiaryId(diaryId)
			.orElseThrow(() -> new NoSuchElementException("수정할 분석 데이터가 없습니다."));

		analysis.updateResult(req.emotion(), req.score());
		Diary diary=diaryRepository.findById(diaryId).orElseThrow(() -> new NoSuchElementException("수정할 분석 데이터가 없습니다."));
		diary.updateUserCheck();
		return new AnalysisUpdateRes(
			analysis.getEmotion(),
			analysis.getEmotionScore(),
			analysis.getAiComment(),
			LocalDateTime.now()
		);
	}

}