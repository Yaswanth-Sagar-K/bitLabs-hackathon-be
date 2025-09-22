package com.talentstream.service;

import com.talentstream.dto.SubmitProjectRequest;
import com.talentstream.entity.Registration;
import com.talentstream.entity.Submission;
import com.talentstream.repository.ApplicantRepository;
import com.talentstream.repository.HackathonRepository;
import com.talentstream.repository.RegistrationRepository;
import com.talentstream.repository.SubmissionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubmissionService {
	private final SubmissionRepository repo;

	public SubmissionService(SubmissionRepository repo) {
		this.repo = repo;
	}
	
	@Autowired
	private RegistrationRepository regRepo;
	 @Autowired
	    public ApplicantRepository appRepo;
	 
	 @Autowired
	    public HackathonRepository hackRepo;
	 
	public Submission submit(Long hackathonId, SubmitProjectRequest req) {
		
		if (!appRepo.existsById(req.getUserId())) {
            throw new IllegalArgumentException("Applicant not found with id: " + req.getUserId());
        }
	 if (!hackRepo.existsById(hackathonId)) {
            throw new IllegalArgumentException("Hackathon not found with id: " + hackathonId);
        }
	 
	 if (!regRepo.existsById(req.getRegistrationId())) {
         throw new IllegalArgumentException("No registratins found with id: " + req.getRegistrationId());
     }
	 if(repo.findByHackathonIdAndUserId(hackathonId, req.getUserId()).isPresent()) {
		    throw new IllegalArgumentException("The applicant has already submitted the response");
		}

	 

	    Submission s = new Submission();
	    s.setHackathonId(hackathonId);
	    s.setRegistrationId(req.getRegistrationId());
	    s.setUserId(req.getUserId());
	    s.setProjectTitle(req.getProjectTitle());
	    s.setProjectSummary(req.getProjectSummary());
	    s.setUseCase(req.getUseCase());
	    s.setTechnologiesUsed(req.getTechnologiesUsed());
	    s.setGithubLink(req.getGithubLink());
	    s.setDemoLink(req.getDemoLink());
	    
	    Registration r = regRepo.findById(req.getRegistrationId()).get();
	    r.setSubmitStatus(true);
	    regRepo.save(r);

	    return repo.save(s);
	}


	public List<Submission> listByHackathon(Long hackathonId) {
		return repo.findByHackathonId(hackathonId);
	}

	public Submission get(Long id) {
		return repo.findById(id).orElse(null);
	}
}
