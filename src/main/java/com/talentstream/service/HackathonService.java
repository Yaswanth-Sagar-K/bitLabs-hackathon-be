package com.talentstream.service;

import com.talentstream.dto.CreateHackathonRequest;
import com.talentstream.entity.Hackathon;
import com.talentstream.repository.HackathonRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HackathonService {
	private final HackathonRepository repo;

	public HackathonService(HackathonRepository repo) {
		this.repo = repo;
	}

	public Hackathon create(Hackathon h) {
		return repo.save(h);
	}

	public List<Hackathon> getAllByCreaterId(Long id) {
		return repo.findByCreatorId(id).get();
	}
	
	public List<Hackathon> getAll(){
		return repo.findAll();
	}

	public Optional<Hackathon> get(Long id) {
		return repo.findById(id);
	}

	public Hackathon update(Long id, CreateHackathonRequest updated) {
	    return repo.findById(id).map(existing -> {
	        if (updated.getTitle() != null) {
	            existing.setTitle(updated.getTitle());
	        }
	        if (updated.getDescription() != null) {
	            existing.setDescription(updated.getDescription());
	        }
	        if (updated.getBannerUrl() != null) {
	            existing.setBannerUrl(updated.getBannerUrl());
	        }
	        if (updated.getStartAt() != null) {
	            existing.setStartAt(updated.getStartAt());
	        }
	        if (updated.getEndAt() != null) {
	            existing.setEndAt(updated.getEndAt());
	        }
	        if (updated.getInstructions() != null) {
	            existing.setInstructions(updated.getInstructions());
	        }
	        if (updated.getEligibility() != null) {
	            existing.setEligibility(updated.getEligibility());
	        }
	        if (updated.getAllowedTechnologies() != null) {
	            existing.setAllowedTechnologies(updated.getAllowedTechnologies());
	        }
	        if (updated.getStatus() != null) {
	            existing.setStatus(updated.getStatus());
	        }
	        return repo.save(existing);
	    }).orElse(null);
	}


	public boolean delete(Long id) {
	    if (repo.existsById(id)) {
	        repo.deleteById(id);
	        return true;
	    }
	    return false;
	}

}
