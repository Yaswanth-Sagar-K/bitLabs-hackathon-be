package com.talentstream.service;

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

	public List<Hackathon> listAll() {
		return repo.findAll();
	}

	public Optional<Hackathon> get(Long id) {
		return repo.findById(id);
	}

	public Hackathon update(Long id, Hackathon updated) {
		return repo.findById(id).map(existing -> {
			existing.setTitle(updated.getTitle());
			existing.setDescription(updated.getDescription());
			existing.setBannerUrl(updated.getBannerUrl());
			existing.setStartAt(updated.getStartAt());
			existing.setEndAt(updated.getEndAt());
			existing.setInstructions(updated.getInstructions());
			existing.setEligibility(updated.getEligibility());
			existing.setAllowedTechnologies(updated.getAllowedTechnologies());
			existing.setPrizes(updated.getPrizes());
			existing.setStatus(updated.getStatus());
			return repo.save(existing);
		}).orElse(null);
	}

	public void delete(Long id) {
		repo.deleteById(id);
	}
}
