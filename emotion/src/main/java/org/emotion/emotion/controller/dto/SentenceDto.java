package org.emotion.emotion.controller.dto;

import java.util.List;

public record SentenceDto(String content, int offset, int length, String sentiment, Confidence confidence, List<HighlightDto> highlights) {
}
