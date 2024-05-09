package org.emotion.emotion.controller;

import org.emotion.emotion.controller.dto.DiaryDto;
import org.emotion.emotion.controller.dto.DiaryListDto;
import org.emotion.emotion.facade.FacadeDiary;
import org.emotion.emotion.service.DiaryService;
import org.emotion.emotion.controller.dto.SaveDiaryRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;

@RestController
public class DiaryController {

	private final DiaryService diaryService;
	private final FacadeDiary facadeDiary;

	@Autowired
	public DiaryController(DiaryService diaryService, FacadeDiary facadeDiary) {
		this.diaryService = diaryService;
		this.facadeDiary = facadeDiary;
	}

	@PostMapping("/diary")
	public ResponseEntity<String> saveDiary(@RequestBody SaveDiaryRequestDto saveDiaryRequestDto){
		// diaryService.saveDiary(saveDiaryRequestDto);
		facadeDiary.saveDiary(saveDiaryRequestDto);
		return new ResponseEntity<>("다이어리가 저장되었습니다.",HttpStatus.CREATED);
	}

	@GetMapping("/diary")
	public ResponseEntity<DiaryListDto> getAllDiary(@RequestHeader("memberId") Long memberId){
		return new ResponseEntity<>(diaryService.getAllDiary(memberId), HttpStatus.OK);
	}

	@GetMapping("/diary/{diaryId}")
	public ResponseEntity<DiaryDto> getDiaryById(@RequestHeader("memberId") Long memberId, @PathVariable("diaryId") Long diaryId){
		return new ResponseEntity<>(diaryService.getDiaryById(memberId, diaryId), HttpStatus.OK);
	}



}
