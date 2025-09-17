package com.talentstream.dto;

import java.util.List;

public class GroqResponse {
    private List<Choice> choices;
    private Error error;

    public static class Choice {
        private GroqMessage message;

        public GroqMessage getMessage() {
            return message;
        }

        public void setMessage(GroqMessage message) {
            this.message = message;
        }
    }

    public static class Error {
        private String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public List<Choice> getChoices() {
        return choices;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }
}
