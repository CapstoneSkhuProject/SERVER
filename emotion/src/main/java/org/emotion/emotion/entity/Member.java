package org.emotion.emotion.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
	@Id
	@Column(name = "member_id")
	private Long id;

	private String nickname;

	private String email;

	@Enumerated(EnumType.STRING)
	private Grant grant;

	private String token;

	private boolean isOn;

	private int danger;

	private String drug;

	private String loginId;

	private String loginPassword;

	private String phoneNumber;

	@Builder
	private Member(String nickname, String email, Grant grant, String token, boolean isOn, int danger, String drug,
		String loginId, String loginPassword, String phoneNumber) {
		this.nickname = nickname;
		this.email = email;
		this.grant = grant;
		this.token = token;
		this.isOn = isOn;
		this.danger = danger;
		this.drug = drug;
		this.loginId = loginId;
		this.loginPassword = loginPassword;
		this.phoneNumber = phoneNumber;
	}
}
