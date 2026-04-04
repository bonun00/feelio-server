package app.feelio.dto.diary;

import app.feelio.dto.analysis.AnalysisType;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "일기 작성 및 분석 요청 데이터")
public record DiaryReq(

	@Schema(description = "일기 본문 내용", example = "오늘 드디어 프로젝트의 비동기 처리를 완성했다! 4초 걸리던 응답이 0.1초로 줄어들어서 정말 기쁘다.")
	String content,

	@Schema(description = "AI 분석 모드 선택 (T/F/TF)", implementation = AnalysisType.class, example = "T")
	AnalysisType analysisType
) {}