package com.talentstream.entity;

import javax.persistence.*;

@Entity
@Table(name = "scores", indexes = { @Index(columnList = "hackathonId"), @Index(columnList = "submissionId") })
public class Score {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long hackathonId;
	private Long submissionId;
	private Long judgeId;
	private double scoreValue; 
	@Column(length = 2000)
	private String comment;

	public Score() {
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

	public Long getSubmissionId() {
		return submissionId;
	}

	public void setSubmissionId(Long submissionId) {
		this.submissionId = submissionId;
	}

	public Long getJudgeId() {
		return judgeId;
	}

	public void setJudgeId(Long judgeId) {
		this.judgeId = judgeId;
	}

	public double getScoreValue() {
		return scoreValue;
	}

	public void setScoreValue(double scoreValue) {
		this.scoreValue = scoreValue;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}
