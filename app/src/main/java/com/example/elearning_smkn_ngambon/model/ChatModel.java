package com.example.elearning_smkn_ngambon.model;

public class ChatModel {
    private final String message;
    private final boolean isBotMessage;

    public ChatModel(String message, boolean isBotMessage) {
        this.message = message;
        this.isBotMessage = isBotMessage;
    }

    public String getMessage() {
        return message;
    }

    public boolean isBotMessage() {
        return isBotMessage;
    }
}
