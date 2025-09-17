package com.talentstream.service;

import com.talentstream.dto.CreateHackathonRequest;
import com.talentstream.entity.Hackathon;
import com.talentstream.repository.HackathonRepository;
import com.talentstream.repository.JobRecruiterRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

@Service
public class HackathonService {
	private final HackathonRepository repo;
	@Autowired
	private JobRecruiterRepository recruiterRepo;


	public HackathonService(HackathonRepository repo) {
		this.repo = repo;
	}

	public Hackathon createHackathon(CreateHackathonRequest r) {
        if (!recruiterRepo.existsById(r.getRecruiterId())) {
            throw new EntityNotFoundException("Recruiter not found with id: " + r.getRecruiterId());
        }

        LocalDate today = LocalDate.now();

        if (r.getStartAt().isBefore(today)) {
            throw new IllegalArgumentException("Start date must be greater than today's date");
        }
        if (r.getEndAt().isBefore(r.getStartAt())) {
            throw new IllegalArgumentException("End date must be greater than start date");
        }

        Hackathon h = new Hackathon();
        h.setRecruiterId(r.getRecruiterId());
        h.setTitle(r.getTitle());
        h.setDescription(r.getDescription());
        h.setBannerUrl(r.getBannerUrl());
        h.setStartAt(r.getStartAt());
        h.setEndAt(r.getEndAt());
        h.setInstructions(r.getInstructions());
        h.setEligibility(r.getEligibility());
        h.setAllowedTechnologies(r.getAllowedTechnologies());
        h.setCompany(r.getCompany());

        return repo.save(h);
    }

	public List<Hackathon> getAllByCreaterId(Long id) {
		return repo.findByRecruiterId(id).get();
	}
	
	public List<Hackathon> getAll(){
		return repo.findAll();
	}

	public Optional<Hackathon> get(Long id) {
		return repo.findById(id);
	}

	 public Hackathon updateHackathon(Long id, CreateHackathonRequest r) {
	        Hackathon existing = repo.findById(id)
	                .orElseThrow(() -> new EntityNotFoundException("No hackathon found with id: " + id));

	        if (!recruiterRepo.existsById(r.getRecruiterId())) {
	            throw new EntityNotFoundException("Recruiter not found with id: " + r.getRecruiterId());
	        }

	        LocalDate today = LocalDate.now();

	        if (r.getStartAt() != null && r.getStartAt().isBefore(today)) {
	            throw new IllegalArgumentException("Start date must be greater than today's date");
	        }
	        if (r.getStartAt() != null && r.getEndAt() != null && r.getEndAt().isBefore(r.getStartAt())) {
	            throw new IllegalArgumentException("End date must be greater than start date");
	        }
	        if (r.getEndAt() != null && r.getStartAt() == null && r.getEndAt().isBefore(existing.getStartAt())) {
	            throw new IllegalArgumentException("End date must be greater than start date");
	        }

	        if (r.getTitle() != null) existing.setTitle(r.getTitle());
	        if (r.getDescription() != null) existing.setDescription(r.getDescription());
	        if (r.getBannerUrl() != null) existing.setBannerUrl(r.getBannerUrl());
	        if (r.getStartAt() != null) existing.setStartAt(r.getStartAt());
	        if (r.getEndAt() != null) existing.setEndAt(r.getEndAt());
	        if (r.getInstructions() != null) existing.setInstructions(r.getInstructions());
	        if (r.getEligibility() != null) existing.setEligibility(r.getEligibility());
	        if (r.getAllowedTechnologies() != null) existing.setAllowedTechnologies(r.getAllowedTechnologies());
	        if (r.getCompany() != null) existing.setCompany(r.getCompany());

	        return repo.save(existing);
	    }

	public boolean delete(Long id) {
	    if (repo.existsById(id)) {
	        repo.deleteById(id);
	        return true;
	    }
	    return false;
	}

}
