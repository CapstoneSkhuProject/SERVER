package org.emotion.emotion.facade;

import java.time.LocalDate;

import lombok.Builder;

public record SaveSolutionDto(String solution) {
	public static SaveSolutionDto from(String solution){
		return new SaveSolutionDto(solution);
	}
}
