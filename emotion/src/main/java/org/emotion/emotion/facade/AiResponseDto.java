package org.emotion.emotion.facade;

import java.util.concurrent.CompletableFuture;

import org.emotion.emotion.controller.dto.ClovarResponseDto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AiResponseDto {
	private CompletableFuture<String> solution;
	private ClovarResponseDto res;
	public static AiResponseDto of(CompletableFuture<String> solution, ClovarResponseDto res){
		return new AiResponseDto(solution,res);
	}
}
