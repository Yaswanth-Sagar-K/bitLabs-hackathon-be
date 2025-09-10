package com.talentstream.entity;

import javax.persistence.*;

@Entity
@Table(name = "submissions", indexes = { @Index(columnList = "hackathonId"), @Index(columnList = "registrationId") })
public class Submission {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long hackathonId;
	private Long registrationId;
	private String title;
	@Column(length = 4000)
	private String description;
	private String repoUrl;
	private String docUrl;
	private String demoUrl;
	@Column(length = 8000)
	private String repoMetadataJson; 

	public Submission() {
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

	public Long getRegistrationId() {
		return registrationId;
	}

	public void setRegistrationId(Long registrationId) {
		this.registrationId = registrationId;
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

	public String getRepoUrl() {
		return repoUrl;
	}

	public void setRepoUrl(String repoUrl) {
		this.repoUrl = repoUrl;
	}

	public String getDocUrl() {
		return docUrl;
	}

	public void setDocUrl(String docUrl) {
		this.docUrl = docUrl;
	}

	public String getDemoUrl() {
		return demoUrl;
	}

	public void setDemoUrl(String demoUrl) {
		this.demoUrl = demoUrl;
	}

	public String getRepoMetadataJson() {
		return repoMetadataJson;
	}

	public void setRepoMetadataJson(String repoMetadataJson) {
		this.repoMetadataJson = repoMetadataJson;
	}
}
