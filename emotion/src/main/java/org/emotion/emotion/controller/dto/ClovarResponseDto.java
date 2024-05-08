package org.emotion.emotion.controller.dto;

import java.util.List;

public record ClovarResponseDto(Document document, List<SentenceDto> sentences) {
}
