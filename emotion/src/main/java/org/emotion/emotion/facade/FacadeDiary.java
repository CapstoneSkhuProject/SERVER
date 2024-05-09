package org.emotion.emotion.facade;

import org.emotion.emotion.controller.dto.SaveDiaryRequestDto;
import org.emotion.emotion.service.DiaryService;
import org.emotion.emotion.service.GptService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
@Service
public class FacadeDiary {
	private final DiaryService diaryService;
	private final GptService gptService;
	public FacadeDiary(DiaryService diaryService, GptService gptService) {
		this.diaryService = diaryService;
		this.gptService = gptService;
	}

	@Transactional
	public void saveDiary(SaveDiaryRequestDto saveDiaryRequestDto){
		diaryService.saveDiary(saveDiaryRequestDto,gptService.saveSolutionByGpt(saveDiaryRequestDto));
	}


}
