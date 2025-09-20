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

	    Submission s = new Submission();
	    s.setRegistrationId(req.getRegistrationId());

	    s.setProjectTitle(req.getProjectTitle());
	    s.setProjectSummary(req.getProjectSummary());
	    s.setUseCase(req.getUseCase());
	    s.setTechnologiesUsed(req.getTechnologiesUsed());
	    s.setGithubLink(req.getGithubLink());
	    s.setDemoLink(req.getDemoLink());


	    return repo.save(s);
	}


	public List<Submission> listByHackathon(Long hackathonId) {
		return repo.findByHackathonId(hackathonId);
	}

	public Submission get(Long id) {
		return repo.findById(id).orElse(null);
	}
}
