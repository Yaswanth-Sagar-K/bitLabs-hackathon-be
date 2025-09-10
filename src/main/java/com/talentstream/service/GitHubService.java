package com.talentstream.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Map;

@Service
public class GitHubService {
	@Value("${app.github.token:}")
	private String githubToken;

	private final ObjectMapper mapper = new ObjectMapper();

	private HttpHeaders headers() {
		HttpHeaders h = new HttpHeaders();
		h.set("Accept", "application/vnd.github+json");
		if (githubToken != null && !githubToken.isBlank())
			h.setBearerAuth(githubToken);
		return h;
	}

	public String fetchRepoMetadataJson(String repoUrl) {
		// expects https://github.com/{owner}/{repo}
		String cleaned = repoUrl.replace("https://github.com/", "").replace("http://github.com/", "");
		String[] parts = cleaned.split("/");
		if (parts.length < 2)
			throw new IllegalArgumentException("Invalid GitHub URL");
		String owner = parts[0], repo = parts[1].replaceAll(".git$", "");

		RestTemplate rt = new RestTemplate();
		HttpEntity<Void> req = new HttpEntity<>(headers());
		try {
			ResponseEntity<String> repoRes = rt.exchange(
					URI.create("https://api.github.com/repos/" + owner + "/" + repo), HttpMethod.GET, req,
					String.class);
			ResponseEntity<String> langRes = rt.exchange(
					URI.create("https://api.github.com/repos/" + owner + "/" + repo + "/languages"), HttpMethod.GET,
					req, String.class);
			ResponseEntity<String> contribRes = rt.exchange(
					URI.create("https://api.github.com/repos/" + owner + "/" + repo + "/contributors?per_page=100"),
					HttpMethod.GET, req, String.class);

			JsonNode repoNode = mapper.readTree(repoRes.getBody());
			JsonNode langsNode = mapper.readTree(langRes.getBody());
			JsonNode contribNode = mapper.readTree(contribRes.getBody());

		
			ObjectNode out = mapper.createObjectNode();
			out.put("name", repoNode.path("name").asText());
			out.put("owner", repoNode.path("owner").path("login").asText());
			out.put("stars", repoNode.path("stargazers_count").asInt());
			out.put("forks", repoNode.path("forks_count").asInt());
			out.put("lastPushedAt", repoNode.path("pushed_at").asText());
			out.set("languages", langsNode);
			out.put("contributorsCount", contribNode.isArray() ? contribNode.size() : 0);
			return mapper.writeValueAsString(out);
		} catch (Exception e) {
			throw new RuntimeException("Failed to fetch GitHub metadata: " + e.getMessage(), e);
		}
	}
}
