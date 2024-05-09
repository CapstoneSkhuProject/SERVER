package org.emotion.emotion.service;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

import org.emotion.emotion.controller.dto.ClovarResponseDto;
import org.emotion.emotion.controller.dto.NaverClovaPromptRequestDto;
import org.emotion.emotion.controller.dto.SaveDiaryRequestDto;
import org.emotion.emotion.entity.Type;
import org.emotion.emotion.facade.AiResponseDto;
import org.emotion.emotion.facade.SaveSolutionDto;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;

@Service
public class GptService {

	@Value("${jwt.NAVER-CLIENT-KEY}")
	private String NAVER_KEY;
	@Value("${jwt.NAVER-CLIENT-SECRET}")
	private String NAVER_SECRET;
	@Value("${jwt.CLOVAR-URL}")
	private String NAVER_CLOVAR_URL;

	@Value("${jwt.OPEN_API_KEY}")
	private String OPEN_API_KEY;

	private final OpenAiChatClient chatClient;

	@Autowired
	public GptService(OpenAiChatClient chatClient) {
		this.chatClient = chatClient;
	}

	@Async
	public AiResponseDto saveSolutionByGpt(SaveDiaryRequestDto saveDiaryRequestDto) {
		ResponseEntity<ClovarResponseDto> clovarResponse = requestClovarServer(NaverClovaPromptRequestDto.from(saveDiaryRequestDto.content()));
		ClovarResponseDto res = clovarResponse.getBody();
		if (res == null){
			return AiResponseDto.of(CompletableFuture.completedFuture(chatClient.call(requestGptPrompt("positive"))),null);
		}
		return AiResponseDto.of(CompletableFuture.completedFuture(chatClient.call(requestGptPrompt(res.document().sentiment()))),res);
	}

	private String requestGptPrompt(String sentiment) {
		switch (sentiment) {
			case "negative":
				return "부정적인 감정이 드는 것 같아. 어떻게 하면 기분을 좋게 만들 수 있을까? 한국어로 출력해줘.";
			case "positive":
				return "긍정적인 기분인데 이럴 때 뭘하면 더 나를 발전시킬 수 있을까? 솔루션을 제시해줄래?";
			case "neutral":
				return "자기 계발하기에 좋은 솔루션을 제시해줄 수 있을까?";
		}
		return "자기 계발하기에 좋은 솔루션을 제시해줄 수 있을까?";

	}


	private ResponseEntity<ClovarResponseDto> requestClovarServer(
		NaverClovaPromptRequestDto naverClovaPromptRequestDto) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();

		headers.add("X-NCP-APIGW-API-KEY-ID", NAVER_KEY);
		headers.add("X-NCP-APIGW-API-KEY", NAVER_SECRET);
		headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

		MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();
		body.add("content", naverClovaPromptRequestDto.content());
		HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(body, headers);

		return restTemplate.postForEntity(NAVER_CLOVAR_URL, httpEntity, ClovarResponseDto.class);
	}


}

