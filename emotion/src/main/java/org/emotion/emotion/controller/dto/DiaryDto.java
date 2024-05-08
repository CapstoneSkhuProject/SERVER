package org.emotion.emotion.controller.dto;

import org.emotion.emotion.entity.Diary;

public record DiaryDto(Diary diary) {
	public static DiaryDto from(Diary diary){
		return new DiaryDto(diary);
	}
}
