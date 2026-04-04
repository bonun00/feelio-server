package app.feelio.service;

import java.util.Arrays;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import app.feelio.domain.diary.Diary;
import app.feelio.domain.diary.DiaryRepo;
import app.feelio.domain.diary.SimilarDiaryProjection;
import app.feelio.dto.analysis.AnalysisType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;



@Service
@Slf4j
@RequiredArgsConstructor
public class AsyncAnalysis {

	private final EmbeddingModel embeddingModel;
	private final DiaryRepo diaryRepository;
	private final AiAnalysisManager aiAnalysisManager;

	@Async
	public void processAiAnalysisAsync(Diary diary, String content, Long userId, AnalysisType type) {
		try {
			log.info("비동기 AI 분석 시작: diaryId={}", diary.getId());

			float[] embedding = embeddingModel.embed(content);
			String vectorString = Arrays.toString(embedding);

			SimilarDiaryProjection similarDiary = diaryRepository
				.findMostSimilarContent(vectorString, 0.5, userId, diary.getId())
				.orElse(null);

			aiAnalysisManager.performAiAnalysis(diary, content, similarDiary, type);

			log.info("비동기 AI 분석 완료: diaryId={}", diary.getId());
		} catch (Exception e) {
			log.error("비동기 분석 중 에러 발생: diaryId={}", diary.getId(), e);
		}
	}


}
