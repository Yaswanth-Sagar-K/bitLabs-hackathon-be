package com.talentstream.service;

import com.talentstream.entity.Hackathon;
import com.talentstream.entity.HackathonStatus;
import com.talentstream.entity.Registration;
import com.talentstream.repository.ApplicantRepository;
import com.talentstream.repository.HackathonRepository;
import com.talentstream.repository.RegistrationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RegistrationService {
	private final RegistrationRepository repo;

	public RegistrationService(RegistrationRepository repo) {
		this.repo = repo;
	}
	
	 @Autowired
	    public ApplicantRepository appRepo;
	 
	 @Autowired
	    public HackathonRepository hackRepo;

	public Optional<Registration> findByHackathonAndUser(Long hackathonId, Long userId) {
	    return repo.findByHackathonIdAndUserId(hackathonId, userId);
	}

	 public Registration register(Long hackathonId, Long userId) {
	        if (!appRepo.existsById(userId)) {
	            throw new IllegalArgumentException("Applicant not found with id: " + userId);
	        }
	        Hackathon hackathon = hackRepo.findById(hackathonId)
	                .orElseThrow(() -> new IllegalArgumentException("Hackathon not found with id: " + hackathonId));

	        if (hackathon.getStatus() == HackathonStatus.COMPLETED) {
	            throw new IllegalStateException("Hackathon is already completed. Cannot register.");
	        }

	        Optional<Registration> existing = repo.findByHackathonIdAndUserId(hackathonId, userId);
	        if (existing.isPresent()) {
	            throw new IllegalStateException("User already registered for hackathon with id: " + hackathonId);
	        }

	        Registration r = new Registration();
	        r.setHackathonId(hackathonId);
	        r.setUserId(userId);
	        return repo.save(r);
	    }

	public List<Registration> listByHackathon(Long hackathonId) {
		return repo.findByHackathonId(hackathonId);
	}
}
