package org.emotion.emotion.service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.emotion.emotion.controller.dto.ClovarResponseDto;
import org.emotion.emotion.controller.dto.DiaryDto;
import org.emotion.emotion.controller.dto.DiaryListDto;
import org.emotion.emotion.controller.dto.NaverClovaPromptRequestDto;
import org.emotion.emotion.controller.dto.SaveDiaryRequestDto;
import org.emotion.emotion.entity.Diary;
import org.emotion.emotion.entity.Emotion;
import org.emotion.emotion.entity.Type;
import org.emotion.emotion.repository.DiaryRepository;
import org.emotion.emotion.repository.MemberRepository;
import org.emotion.emotion.entity.Member;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import jakarta.persistence.Column;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DiaryService {

	@Value("${jwt.NAVER-CLIENT-KEY}")
	private String NAVER_KEY;
	@Value("${jwt.NAVER-CLIENT-SECRET}")
	private String NAVER_SECRET;
	@Value("${jwt.CLOVAR-URL}")
	private String NAVER_CLOVAR_URL;

	@Value("${jwt.OPEN_API_KEY}")
	private String OPEN_API_KEY;


	private final MemberRepository memberRepository;
	private final DiaryRepository diaryRepository;
	@Transactional
	public void saveDiary(SaveDiaryRequestDto saveDiaryRequestDto){
		Member member = memberRepository.findById(saveDiaryRequestDto.userId())
			.orElseThrow();
		ResponseEntity<ClovarResponseDto> clovarResponse = requestClovarServer(NaverClovaPromptRequestDto.from(saveDiaryRequestDto.content()));
		ClovarResponseDto res = clovarResponse.getBody();

		Diary diary = Diary.builder()
			.member(member)
			.emotion(
				Emotion.builder()
					.type(clovarEnumConverter(res.document().sentiment()))
					.point((int)Math.max(Math.max(res.document().confidence().neutral(), res.document().confidence().negative()),res.document().confidence().positive()))
					.solution("gpt에서 나온 결과입니다.")
					.build())
			.date(LocalDateTime.now().toLocalDate())
			.content(saveDiaryRequestDto.content())
			.emotionSummary(res.sentences().toString())
			.build();
		diaryRepository.save(diary);

	}


	public DiaryListDto getAllDiary(Long memberId){
		Member member = memberRepository.findById(memberId).orElseThrow();
		return DiaryListDto.from(diaryRepository.findAllByMember(member));
	}

	public DiaryDto getDiaryById(Long memberId, Long diaryId){
		Member member = memberRepository.findById(memberId).orElseThrow();
		return DiaryDto.from(diaryRepository.findById(diaryId).orElseThrow());
	}

	private ResponseEntity<ClovarResponseDto> requestClovarServer(NaverClovaPromptRequestDto naverClovaPromptRequestDto){
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();


		headers.add("X-NCP-APIGW-API-KEY-ID",NAVER_KEY);
		headers.add("X-NCP-APIGW-API-KEY",NAVER_SECRET);
		headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

		MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();
		body.add("content",naverClovaPromptRequestDto.content());
		HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(body, headers);

		return restTemplate.postForEntity(NAVER_CLOVAR_URL,httpEntity,ClovarResponseDto.class);
	}

	private Type clovarEnumConverter(String sentiment){
		switch (sentiment){
			case "negative":
				return Type.NEGATIVE;
			case "positive":
				return Type.POSITIVE;
			case "neutral":
				return Type.SOSO;
		}
		return Type.POSITIVE;
	}

}
