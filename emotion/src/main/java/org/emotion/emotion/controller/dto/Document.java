package org.emotion.emotion.controller.dto;

import lombok.Builder;

@Builder
public record Document(String sentiment, Confidence confidence) {
}
