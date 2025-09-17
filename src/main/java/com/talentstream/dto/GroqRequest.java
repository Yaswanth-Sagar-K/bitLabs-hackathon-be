package com.talentstream.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
 
public class GroqRequest {
    private String model;
    private List<GroqMessage> messages;
    @JsonProperty("max_tokens")
    private int maxTokens;
    private double temperature;
 
    public GroqRequest() {}
 
    public GroqRequest(String model, List<GroqMessage> messages, int maxTokens, double temperature) {
        this.model = model;
        this.messages = messages;
        this.maxTokens = maxTokens;
        this.temperature = temperature;
    }
 
    public String getModel() {
        return model;
    }
 
    public void setModel(String model) {
        this.model = model;
    }
 
    public List<GroqMessage> getMessages() {
        return messages;
    }
 
    public void setMessages(List<GroqMessage> messages) {
        this.messages = messages;
    }
 
    public int getMaxTokens() {
        return maxTokens;
    }
 
    public void setMaxTokens(int maxTokens) {
        this.maxTokens = maxTokens;
    }
 
    public double getTemperature() {
        return temperature;
    }
 
    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }
}
 