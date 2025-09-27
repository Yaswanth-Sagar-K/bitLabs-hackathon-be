package com.talentstream.service;

import com.talentstream.entity.Hackathon;
import com.talentstream.entity.HackathonStatus;
import com.talentstream.entity.HackathonRegister;
import com.talentstream.repository.ApplicantRepository;
import com.talentstream.repository.HackathonRepository;
import com.talentstream.repository.HackathonRegisterRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HackathonRegisterService {
	private final HackathonRegisterRepository repo;

	public HackathonRegisterService(HackathonRegisterRepository repo) {
		this.repo = repo;
	}
	
	 @Autowired
	    public ApplicantRepository appRepo;
	 
	 @Autowired
	    public HackathonRepository hackRepo;

	public HackathonRegister findByHackathonAndUser(Long hackathonId, Long userId) {
		 if (!appRepo.existsById(userId)) {
	            throw new IllegalArgumentException("Applicant not found with id: " + userId);
	        }
		 if (!hackRepo.existsById(hackathonId)) {
	            throw new IllegalArgumentException("Hackathon not found with id: " + hackathonId);
	        }
		
		HackathonRegister result = repo.findByHackathonIdAndUserId(hackathonId, userId).get();
	    return result;
	}

	 public HackathonRegister register(Long hackathonId, Long userId) {
	        if (!appRepo.existsById(userId)) {
	            throw new IllegalArgumentException("Applicant not found with id: " + userId);
	        }
	        Hackathon hackathon = hackRepo.findById(hackathonId)
	                .orElseThrow(() -> new IllegalArgumentException("Hackathon not found with id: " + hackathonId));

	        if (hackathon.getStatus() == HackathonStatus.COMPLETED) {
	            throw new IllegalStateException("Hackathon is already completed. Cannot register.");
	        }

	        Optional<HackathonRegister> existing = repo.findByHackathonIdAndUserId(hackathonId, userId);
	        if (existing.isPresent()) {
	            throw new IllegalStateException("User already registered for hackathon with id: " + hackathonId);
	        }

	        HackathonRegister r = new HackathonRegister();
	        r.setHackathonId(hackathonId);
	        r.setUserId(userId);
	        r.setRegistaratinStatus(true);
	        r.setSubmitStatus(false);
	        return repo.save(r);
	    }

	public List<HackathonRegister> listByHackathon(Long hackathonId) {
		return repo.findByHackathonId(hackathonId);
	}
	
	public List<HackathonRegister> listByApplicant(Long applicantId) {
		return repo.findByUserId(applicantId);
	}
}
