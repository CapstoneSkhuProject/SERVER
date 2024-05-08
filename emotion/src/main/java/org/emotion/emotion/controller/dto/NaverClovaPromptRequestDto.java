package org.emotion.emotion.controller.dto;

public record NaverClovaPromptRequestDto(String content) {
	public static NaverClovaPromptRequestDto from(String content){
		return new NaverClovaPromptRequestDto(content);
	}
}
