package com.talentstream.dto;

import javax.validation.constraints.NotNull;

public class RegisterRequest {
	@NotNull
	private Long userId;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}
