package org.emotion.emotion.controller.dto;

import java.time.LocalDate;

public record SaveDiaryRequestDto(Long userId, LocalDate date, String content) {
}
