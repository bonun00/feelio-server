package app.feelio.service;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import app.feelio.domain.analysis.AIAnalysis;
import app.feelio.domain.analysis.AIAnalysisRepo;
import app.feelio.domain.diary.Diary;
import app.feelio.domain.diary.SimilarDiaryProjection;
import app.feelio.dto.analysis.AnalysisData;
import app.feelio.dto.analysis.AnalysisRes;
import app.feelio.dto.analysis.AnalysisType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class AiAnalysisManager {
	private final ChatModel chatModel;
	private final ObjectMapper objectMapper;
	private final AIAnalysisRepo aiAnalysisRepo;

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public AnalysisRes performAiAnalysis(Diary diary, String content, SimilarDiaryProjection similarResults,
		AnalysisType type) throws Exception {
		boolean isContextUsed = (similarResults != null);

		if (isContextUsed) {
			log.info("유사 일기 발견! 점수: {}", similarResults.getSimilarity());
		} else {
			log.info("유사한 과거 일기가 없습니다.");
		}

		String contextPrompt = isContextUsed
			? String.format(
			"참고할 과거 기록: [작성일: %s, 감정: %s, 내용: %s]. " +
				"이 작성일이 몇일인지와 내용을 언급하며 오늘의 일기와 연결 지어 공감되는 코멘트를 남겨줘.",
			similarResults.getCreatedAt(),
			similarResults.getEmotion(),
			similarResults.getContent()
		)
			: "참고할 과거 기록 없음. 오늘의 일기 내용에만 집중해서 분석해줘.";

		String personaInstruction = switch (type) {
			case T -> "당신은 '논리적이고 객관적인 분석가'입니다. 감정 과잉을 배제하고, 현상을 이성적으로 진단하여 담백한 조언을 제공하세요.";
			case F -> "당신은 '따뜻하고 공감 능력이 뛰어난 상담사'입니다. 사용자의 마음을 충분히 어루만지고 다정한 응원과 위로를 건네세요.";
			case TF -> "당신은 '이성과 감성이 조화로운 멘토'입니다. 충분히 공감해 주되, 상황을 객관적으로 바라볼 수 있는 통찰을 함께 제공하세요.";
		};

		String finalPrompt = String.format("""
       %s
       
       ## 분석 지침
       1. 감정 추론: 텍스트 이면의 뉘앙스를 파악하여 심층적으로 분석하세요.
       2. 수치화(score): 0(매우 부정/절망) ~ 100(매우 긍정/안정) 사이의 정수로 측정하세요.
       3. 감정 키워드(emotion): [행복, 평온, 슬픔, 분노, 불안, 피곤] 중 가장 적절한 하나를 선택하세요.
       4. 코멘트(comment): 
          - 반드시 3~4줄 분량으로 작성하세요.
          - %s 성향에 맞춰 응답하세요.
          - 마크다운이나 코드 블록(```)을 절대 사용하지 마세요.
       
       %s
       
       ## 분석할 오늘의 일기
       "%s"
       
       ## 응답 형식 (순수 JSON만 출력)
       {
         "score": (number),
         "emotion": (string),
         "comment": (string)
       }
       """, personaInstruction, type.name(), contextPrompt, content);

		String response = chatModel.call(finalPrompt);
		String jsonOnly = response.replaceAll("```json|```", "").trim();
		AnalysisData data = objectMapper.readValue(jsonOnly, AnalysisData.class);

		AIAnalysis aiRes = AIAnalysis.builder()
			.diary(diary)
			.emotionScore(data.score())
			.emotion(data.emotion())
			.aiComment(data.comment()).
			build();

		aiAnalysisRepo.save(aiRes);

		return AnalysisRes.builder()
			.score(data.score()).emotion(data.emotion()).comment(data.comment())
			.isContextUsed(isContextUsed).build();
	}
}