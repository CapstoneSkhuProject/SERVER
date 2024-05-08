package org.emotion.emotion.controller.dto;

import java.util.List;

import org.emotion.emotion.entity.Diary;

import lombok.Builder;

@Builder
public record DiaryListDto(List<Diary> diaryDtoList) {

	public static DiaryListDto from(List<Diary> diaryDtoList){
		return new DiaryListDto(diaryDtoList);
	}
}
