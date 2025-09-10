package com.talentstream.controller;

import com.talentstream.entity.Hackathon;
import com.talentstream.service.HackathonService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hackathons")
@CrossOrigin
public class HackathonController {
	private final HackathonService service;

	public HackathonController(HackathonService service) {
		this.service = service;
	}

	@GetMapping
	public List<Hackathon> list() {
		return service.listAll();
	}

	@GetMapping("/{id}")
	public Hackathon get(@PathVariable Long id) {
		return service.get(id).orElse(null);
	}
}
