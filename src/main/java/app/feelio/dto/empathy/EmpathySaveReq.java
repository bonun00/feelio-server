package app.feelio.dto.empathy;


import io.swagger.v3.oas.annotations.media.Schema;

public record EmpathySaveReq(

	@Schema(description = "작성할 공감/위로 메시지", example = "오늘 하루 정말 고생 많으셨어요. 당신의 내일을 응원합니다!")
	String content
) {}