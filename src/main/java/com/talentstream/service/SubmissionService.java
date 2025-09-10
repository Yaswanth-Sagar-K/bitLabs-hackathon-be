package com.talentstream.service;

import com.talentstream.dto.SubmitProjectRequest;
import com.talentstream.entity.Submission;
import com.talentstream.repository.SubmissionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubmissionService {
	private final SubmissionRepository repo;
	private final GitHubService gitHubService;

	public SubmissionService(SubmissionRepository repo, GitHubService gitHubService) {
		this.repo = repo;
		this.gitHubService = gitHubService;
	}

	public Submission submit(Long hackathonId, SubmitProjectRequest req) {
		String meta = gitHubService.fetchRepoMetadataJson(req.getRepoUrl());
		Submission s = new Submission();
		s.setHackathonId(hackathonId);
		s.setRegistrationId(req.getRegistrationId());
		s.setTitle(req.getTitle());
		s.setDescription(req.getDescription());
		s.setRepoUrl(req.getRepoUrl());
		s.setDocUrl(req.getDocUrl());
		s.setDemoUrl(req.getDemoUrl());
		s.setRepoMetadataJson(meta);
		return repo.save(s);
	}

	public List<Submission> listByHackathon(Long hackathonId) {
		return repo.findByHackathonId(hackathonId);
	}

	public Submission get(Long id) {
		return repo.findById(id).orElse(null);
	}
}
