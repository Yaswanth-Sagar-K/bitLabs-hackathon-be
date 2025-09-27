package com.talentstream.service;

import com.talentstream.dto.HackathonSubmitRequestDTO;
import com.talentstream.entity.HackathonRegister;
import com.talentstream.entity.HackathonSubmit;
import com.talentstream.repository.ApplicantRepository;
import com.talentstream.repository.HackathonRepository;
import com.talentstream.repository.HackathonRegisterRepository;
import com.talentstream.repository.HackathonSubmitRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HackathonSubmitService {
	private final HackathonSubmitRepository repo;

	public HackathonSubmitService(HackathonSubmitRepository repo) {
		this.repo = repo;
	}
	
	@Autowired
	private HackathonRegisterRepository regRepo;
	 @Autowired
	    public ApplicantRepository appRepo;
	 
	 @Autowired
	    public HackathonRepository hackRepo;
	 
	public HackathonSubmit submit(Long hackathonId, HackathonSubmitRequestDTO req) {
		
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

	 

	    HackathonSubmit s = new HackathonSubmit();
	    s.setHackathonId(hackathonId);
	    s.setRegistrationId(req.getRegistrationId());
	    s.setUserId(req.getUserId());
	    s.setProjectTitle(req.getProjectTitle());
	    s.setProjectSummary(req.getProjectSummary());
	    s.setTechnologiesUsed(req.getTechnologiesUsed());
	    s.setGithubLink(req.getGithubLink());
	    s.setDemoLink(req.getDemoLink());
	    
	    HackathonRegister r = regRepo.findById(req.getRegistrationId()).get();
	    r.setSubmitStatus(true);
	    regRepo.save(r);

	    return repo.save(s);
	}


	public List<HackathonSubmit> listByHackathon(Long hackathonId) {
		return repo.findByHackathonId(hackathonId);
	}

	public HackathonSubmit get(Long id) {
		return repo.findById(id).orElse(null);
	}
}
