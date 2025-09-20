package com.talentstream.service;

import com.talentstream.dto.CreateHackathonRequest;
import com.talentstream.dto.UpdateHackathonRequest;
import com.talentstream.entity.ApplicantProfile;
import com.talentstream.entity.ApplicantSkills;
import com.talentstream.entity.Hackathon;
import com.talentstream.entity.HackathonStatus;
import com.talentstream.entity.Registration;
import com.talentstream.repository.ApplicantProfileRepository;
import com.talentstream.repository.ApplicantRepository;
import com.talentstream.repository.HackathonRepository;
import com.talentstream.repository.JobRecruiterRepository;
import com.talentstream.repository.RegistrationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

@Service
public class HackathonService {
	@Autowired
	private JobRecruiterRepository recruiterRepo;
	
	@Autowired
    private ApplicantProfileRepository profileRepository;
	
	@Autowired
	private RegistrationRepository registrationRepo;
	
	 @Autowired
	    public ApplicantRepository appRepo;


    private final HackathonRepository repo;
	public HackathonService(HackathonRepository repo) {
		this.repo = repo;
	}

	 public List<Hackathon> getRecommendedHackathons(Long applicantId) {
		 if (!appRepo.existsById(applicantId)) {
		        throw new IllegalArgumentException("Applicant not found with id: " + applicantId);
		    }
        ApplicantProfile profile = profileRepository.findByApplicantId(applicantId);
                
        Set<String> applicantSkills = profile.getSkillsRequired()
                                             .stream()
                                             .map(ApplicantSkills::getSkillName)
                                             .collect(Collectors.toSet());

        List<Hackathon> allHackathons = repo.findAll();

        List<Hackathon> recommended = allHackathons.stream()
        		.filter(h -> h.getStatus() == HackathonStatus.ACTIVE 
                || h.getStatus() == HackathonStatus.UPCOMING)
        	    .filter(h -> Arrays.stream(h.getAllowedTechnologies().split(",")) 
        	                       .anyMatch(applicantSkills::contains))
        	    .collect(Collectors.toList());

        return recommended;
    }
	 
	 public List<Hackathon> getActiveHackathons() {
	        List<Hackathon> allHackathons = repo.findAll();

	        return allHackathons.stream()
	                .filter(h -> h.getStatus() == HackathonStatus.ACTIVE)
	                .collect(Collectors.toList());
	    }
	 
	 public List<Hackathon> getUpcomingHackathons() {
	        List<Hackathon> allHackathons = repo.findAll();

	        return allHackathons.stream()
	                .filter(h -> h.getStatus() == HackathonStatus.UPCOMING)
	                .collect(Collectors.toList());
	    }
	 
	 public List<Hackathon> getCompletedHackathons() {
	        List<Hackathon> allHackathons = repo.findAll();

	        return allHackathons.stream()
	                .filter(h -> h.getStatus() == HackathonStatus.COMPLETED)
	                .collect(Collectors.toList());
	    }
	 
	 
	public Hackathon createHackathon(CreateHackathonRequest r) {
        if (!recruiterRepo.existsById(r.getRecruiterId())) {
            throw new EntityNotFoundException("Recruiter not found with id: " + r.getRecruiterId());
        }

        LocalDate today = LocalDate.now();

        if (r.getStartAt().isBefore(today)) {
            throw new IllegalArgumentException("Start date must be greater than today's date");
        }
        if (!r.getEndAt().isAfter(r.getStartAt()))  {
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
		List<Hackathon> allHackathons = repo.findAll();

        return allHackathons.stream()
                .filter(h -> h.getStatus() == HackathonStatus.ACTIVE || h.getStatus() == HackathonStatus.UPCOMING)
                .collect(Collectors.toList());
	}

	public Optional<Hackathon> get(Long hackId, Long userId) {
	    boolean applicantExists = appRepo.existsById(userId);
	    boolean recruiterExists = recruiterRepo.existsById(userId);
	    
	    if(!applicantExists && !recruiterExists) {
	    	throw new EntityNotFoundException("candidate or recruiter is not found with the id" + userId);
	    }

		return repo.findById(hackId);
	}

	 public Hackathon updateHackathon(Long id, UpdateHackathonRequest r) {
	        Hackathon existing = repo.findById(id)
	                .orElseThrow(() -> new EntityNotFoundException("No hackathon found with id: " + id));

	        if (!recruiterRepo.existsById(r.getRecruiterId())) {
	            throw new EntityNotFoundException("Recruiter not found with id: " + r.getRecruiterId());
	        }

	        LocalDate today = LocalDate.now();

	        if (r.getStartAt() != null && r.getStartAt().isBefore(today)) {
	            throw new IllegalArgumentException("Start date must be greater than today's date");
	        }
	        if (r.getEndAt() != null && !r.getEndAt().isAfter(r.getStartAt()))  {
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

	 @Transactional
	public boolean delete(Long id) {
	    if (repo.existsById(id)) {
	    	 registrationRepo.deleteByHackathonId(id);
	        repo.deleteById(id);
	        return true;
	    }
	    return false;
	}
	 
	 public List<Hackathon> getRegisteredHackathons(Long applicantId) {
		    if (!appRepo.existsById(applicantId)) {
		        throw new IllegalArgumentException("Applicant not found with id: " + applicantId);
		    }

		    List<Registration> registrations = registrationRepo.findByUserId(applicantId);

		    return registrations.stream()
		            .map((Registration r) -> repo.findById(r.getHackathonId())) // explicitly type
		            .filter((Optional<Hackathon> o) -> o.isPresent())
		            .map((Optional<Hackathon> o) -> o.get())
		            .collect(Collectors.toList());
		}


}
