package com.talentstream.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class SubmitProjectRequest {

    @NotNull(message = "Registration ID is required")
    private Long registrationId;

    @NotBlank(message = "Project title cannot be empty")
    @Size(min = 5, max = 255, message = "Project title must be between 5 and 255 characters")
    private String projectTitle;

    @NotBlank(message = "Project summary cannot be empty")
    @Size(min = 5, max = 4000, message = "Project summary must be between 5 and 4000 characters")
    private String projectSummary;

    @NotBlank(message = "Use case cannot be empty")
    @Size(min = 5, max = 2000, message = "Use case must be between 5 and 2000 characters")
    private String useCase;

    @NotBlank(message = "Technologies used cannot be empty")
    @Size(min = 5, max = 2000, message = "Technologies used must be between 5 and 2000 characters")
    private String technologiesUsed;

    @NotBlank(message = "GitHub repository link is required")
    private String githubLink;

    private String demoLink;

    public Long getRegistrationId() {
        return registrationId;
    }
    public void setRegistrationId(Long registrationId) {
        this.registrationId = registrationId;
    }

    public String getProjectTitle() {
        return projectTitle;
    }
    public void setProjectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
    }

    public String getProjectSummary() {
        return projectSummary;
    }
    public void setProjectSummary(String projectSummary) {
        this.projectSummary = projectSummary;
    }

    public String getUseCase() {
        return useCase;
    }
    public void setUseCase(String useCase) {
        this.useCase = useCase;
    }

    public String getTechnologiesUsed() {
        return technologiesUsed;
    }
    public void setTechnologiesUsed(String technologiesUsed) {
        this.technologiesUsed = technologiesUsed;
    }

    public String getGithubLink() {
        return githubLink;
    }
    public void setGithubLink(String githubLink) {
        this.githubLink = githubLink;
    }

    public String getDemoLink() {
        return demoLink;
    }
    public void setDemoLink(String demoLink) {
        this.demoLink = demoLink;
    }
}
