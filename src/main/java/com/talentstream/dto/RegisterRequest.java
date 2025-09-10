package com.talentstream.dto;

import javax.validation.constraints.NotNull;

public class RegisterRequest {
	@NotNull
	private Long userId;
	private String eligibility;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getEligibility() {
		return eligibility;
	}

	public void setEligibility(String eligibility) {
		this.eligibility = eligibility;
	}
}
