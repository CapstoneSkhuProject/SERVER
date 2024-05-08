package org.emotion.emotion.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Routine {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "routine_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	private Member member;

	private LocalDate date;

	@Column(columnDefinition = "TEXT")
	private String description;

	@Builder
	private Routine(Member member, LocalDate date, String description) {
		this.member = member;
		this.date = date;
		this.description = description;
	}
}
