package com.example.elearning_smkn_ngambon.model;
import com.google.gson.annotations.SerializedName;

public class UploadResponse {
    @SerializedName("message")
    private String message;

    // Buatlah konstruktor, getter, dan setter sesuai kebutuhan
    // Misalnya:

    public UploadResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
