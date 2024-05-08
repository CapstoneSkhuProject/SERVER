package org.emotion.emotion.controller;

import java.util.Map;

import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
public class GptController {
	private final OpenAiChatClient chatClient;

	// 생성자에 @Autowired 어노테이션을 사용하여 OpenAiChatClient 객체의 의존성 주입을 요청하기
	// 스프링 컨테이너는 OpenAiChatClient 타입의 빈을 찾아 이 변수에 자동으로 할당합니다.

	// @RequestParam을 사용하여 "message"라는 이름의 요청 파라미터를 String 타입의 message 변수에 바인딩합니다.
	// 메세지가 없을시 기본 값을 "안녕하세요"으로 설정하여, 요청에서 message 파라미터가 누락된 경우 기본값을 사용합니다.
	@GetMapping("/ai/generate")
	public Map generate(@RequestParam(value = "message", defaultValue = "안녕하세요 ") String message) {
		// chatClient의 call 메소드를 호출하여 입력받은 메시지를 처리하고,
		// "generation"이라는 키와 함께 결과를 Map 객체로 래핑하여 반환합니다.
		// 이 Map 객체는 JSON 형식으로 클라이언트에게 응답됩니다.
		return Map.of("generation", chatClient.call(message));
	}

	// 비동기 스트림 처리 방식

	// 반환 타입: Flux<ChatResponse>를 반환합니다.
	// Flux는 Project Reactor에서 제공하는 반응형 프로그래밍을 위한 API 중 하나로,
	// 여러 개의 데이터 아이템을 시간에 걸쳐 처리하고 반환할 때 사용합니다.
	@GetMapping("/ai/generateStream")
	public Flux<ChatResponse> generateStream(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
		Prompt prompt = new Prompt(new UserMessage(message));
		return chatClient.stream(prompt);
	}
}
