package io.supercharge.securityworkshop;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AppVerificationResponse {

    @JsonProperty("isValidSignature")
    private boolean isValidSignature;

    @JsonProperty("isValidSignature")
    public boolean isValidSignature() {
        return isValidSignature;
    }

    @JsonProperty("isValidSignature")
    public void setValidSignature(boolean validSignature) {
        isValidSignature = validSignature;
    }
}
