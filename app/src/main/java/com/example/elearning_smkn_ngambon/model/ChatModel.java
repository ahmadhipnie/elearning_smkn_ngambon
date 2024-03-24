package com.example.elearning_smkn_ngambon.model;

public class ChatModel {
    private String message;
    private boolean isBotMessage;

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
