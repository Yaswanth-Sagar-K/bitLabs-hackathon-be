package com.talentstream.service;

import com.talentstream.entity.Registration;
import com.talentstream.repository.RegistrationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RegistrationService {
	private final RegistrationRepository repo;

	public RegistrationService(RegistrationRepository repo) {
		this.repo = repo;
	}

	public Optional<Registration> findByHackathonAndUser(Long hackathonId, Long userId) {
	    return repo.findByHackathonIdAndUserId(hackathonId, userId);
	}

	public Registration register(Long hackathonId, Long userId, String eligibilitySnapshot) {
	    Registration r = new Registration();
	    r.setHackathonId(hackathonId);
	    r.setUserId(userId);
	    r.setEligibilitySnapshot(eligibilitySnapshot);
	    return repo.save(r);
	}


	public List<Registration> listByHackathon(Long hackathonId) {
		return repo.findByHackathonId(hackathonId);
	}
}
