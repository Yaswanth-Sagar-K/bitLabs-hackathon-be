package com.talentstream.controller;

import com.talentstream.dto.CreateHackathonRequest;
import com.talentstream.entity.Hackathon;
import com.talentstream.service.HackathonService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/recruiter/hackathons")
@CrossOrigin
public class RecruiterController {
	private final HackathonService service;

	public RecruiterController(HackathonService service) {
		this.service = service;
	}

	@PostMapping
	public Hackathon create(@Valid @RequestBody CreateHackathonRequest r) {
		Hackathon h = new Hackathon();
		h.setCreatorId(r.getCreatorId());
		h.setTitle(r.getTitle());
		h.setDescription(r.getDescription());
		h.setBannerUrl(r.getBannerUrl());
		h.setStartAt(r.getStartAt());
		h.setEndAt(r.getEndAt());
		h.setInstructions(r.getInstructions());
		h.setEligibility(r.getEligibility());
		h.setAllowedTechnologies(r.getAllowedTechnologies());
		h.setPrizes(r.getPrizes());
		h.setStatus(r.getStatus());
		return service.create(h);
	}

	@PutMapping("/{id}")
	public Hackathon update(@PathVariable Long id, @Valid @RequestBody CreateHackathonRequest r) {
		Hackathon h = new Hackathon();
		h.setCreatorId(r.getCreatorId());
		h.setTitle(r.getTitle());
		h.setDescription(r.getDescription());
		h.setBannerUrl(r.getBannerUrl());
		h.setStartAt(r.getStartAt());
		h.setEndAt(r.getEndAt());
		h.setInstructions(r.getInstructions());
		h.setEligibility(r.getEligibility());
		h.setAllowedTechnologies(r.getAllowedTechnologies());
		h.setPrizes(r.getPrizes());
		h.setStatus(r.getStatus());
		return service.update(id, h);
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		service.delete(id);
	}
}
