package com.talentstream.entity;

import javax.persistence.*;

@Entity
@Table(name = "registrations", indexes = { @Index(columnList = "hackathonId"), @Index(columnList = "userId") })
public class Registration {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long hackathonId;
	private Long userId; 
	private String eligibilitySnapshot;

	public Registration() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getHackathonId() {
		return hackathonId;
	}

	public void setHackathonId(Long hackathonId) {
		this.hackathonId = hackathonId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getEligibilitySnapshot() {
		return eligibilitySnapshot;
	}

	public void setEligibilitySnapshot(String eligibilitySnapshot) {
		this.eligibilitySnapshot = eligibilitySnapshot;
	}
}
