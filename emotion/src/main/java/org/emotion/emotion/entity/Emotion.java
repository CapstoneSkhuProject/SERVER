package org.emotion.emotion.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Emotion {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "emotion_id")
	private Long id;

	@Enumerated(EnumType.STRING)
	private Type type;

	@Column(columnDefinition = "TEXT")
	private String solution;

	private int point;

	@Builder
	public Emotion(Type type, String solution, int point) {
		this.type = type;
		this.solution = solution;
		this.point = point;
	}
}
