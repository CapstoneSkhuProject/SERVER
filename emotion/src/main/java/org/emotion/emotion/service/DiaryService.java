package org.emotion.emotion.service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.emotion.emotion.controller.dto.ClovarResponseDto;
import org.emotion.emotion.controller.dto.DiaryDto;
import org.emotion.emotion.controller.dto.DiaryListDto;
import org.emotion.emotion.controller.dto.NaverClovaPromptRequestDto;
import org.emotion.emotion.controller.dto.SaveDiaryRequestDto;
import org.emotion.emotion.entity.Diary;
import org.emotion.emotion.entity.Emotion;
import org.emotion.emotion.entity.Type;
import org.emotion.emotion.facade.AiResponseDto;
import org.emotion.emotion.repository.DiaryRepository;
import org.emotion.emotion.repository.MemberRepository;
import org.emotion.emotion.entity.Member;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import jakarta.persistence.Column;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class DiaryService {



	private final MemberRepository memberRepository;
	private final DiaryRepository diaryRepository;

	/**
	 *
	 * @param saveDiaryRequestDto : 다이어리 저장할 때 주는 요구사항들.
	 * 여기서 궁금증. 과연 비동기 결과값을 대기했다가 저장하는 로직이긴한데. service에서 service를 주입하는 것이 맞는가.
	 * 결론은 파사드패턴인 듯 하다.
	 */
	public void saveDiary(SaveDiaryRequestDto saveDiaryRequestDto, AiResponseDto aiResponseDto){
		Member member = memberRepository.findById(saveDiaryRequestDto.userId())
			.orElseThrow();
		ClovarResponseDto clovarResponseDto = aiResponseDto.getRes();


		CompletableFuture<String> solutionFuture = aiResponseDto.getSolution();

		try {
			String solution = solutionFuture.get(); // 솔루션이 준비될 때까지 대기합니다.

			Diary diary = Diary.builder()
				.member(member)
				.emotion(
					Emotion.builder()
						.type(clovarEnumConverter(clovarResponseDto.document().sentiment()))
						.point((int)Math.max(
							Math.max(clovarResponseDto.document().confidence().neutral(), clovarResponseDto.document().confidence().negative()),
							clovarResponseDto.document().confidence().positive()))
						.solution(solution) // 여기에 솔루션값을 넣어서 저장하려고 하는거!
						.build())
				.date(LocalDateTime.now().toLocalDate())
				.content(saveDiaryRequestDto.content())
				.emotionSummary(clovarResponseDto.sentences().toString())
				.build();
			diaryRepository.save(diary);
		}catch (InterruptedException | ExecutionException e){
			e.getStackTrace();
		}

	}


	@Transactional(readOnly = true)
	public DiaryListDto getAllDiary(Long memberId){
		Member member = memberRepository.findById(memberId).orElseThrow();
		return DiaryListDto.from(diaryRepository.findAllByMember(member));
	}
	@Transactional(readOnly = true)
	public DiaryDto getDiaryById(Long memberId, Long diaryId){
		Member member = memberRepository.findById(memberId).orElseThrow();
		return DiaryDto.from(diaryRepository.findById(diaryId).orElseThrow());
	}

	private Type clovarEnumConverter(String sentiment) {
		switch (sentiment) {
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
