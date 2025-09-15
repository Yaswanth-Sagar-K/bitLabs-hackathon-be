package com.talentstream.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

public class CreateHackathonRequest {
	@NotNull
	private Long creatorId;
	
	@NotNull
	private String title;
	
	@NotNull
	private String description;
	
	@NotNull
	private String bannerUrl;
	
	@NotNull
	private LocalDateTime startAt;
	
	@NotNull
	private LocalDateTime endAt;
	private String instructions;
	private String eligibility;
	private String allowedTechnologies;
	private String status;

	public Long getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getBannerUrl() {
		return bannerUrl;
	}

	public void setBannerUrl(String bannerUrl) {
		this.bannerUrl = bannerUrl;
	}

	public LocalDateTime getStartAt() {
		return startAt;
	}

	public void setStartAt(LocalDateTime startAt) {
		this.startAt = startAt;
	}

	public LocalDateTime getEndAt() {
		return endAt;
	}

	public void setEndAt(LocalDateTime endAt) {
		this.endAt = endAt;
	}

	public String getInstructions() {
		return instructions;
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	public String getEligibility() {
		return eligibility;
	}

	public void setEligibility(String eligibility) {
		this.eligibility = eligibility;
	}

	public String getAllowedTechnologies() {
		return allowedTechnologies;
	}

	public void setAllowedTechnologies(String allowedTechnologies) {
		this.allowedTechnologies = allowedTechnologies;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
