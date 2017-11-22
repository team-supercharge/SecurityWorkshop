package io.supercharge.securityworkshop;

import java.util.Arrays;
import java.util.List;

public class AuthenticationRequest {

    public List<String> getScopes() {
        return Arrays.asList("repo", "user");
    }

    public String getNote() {
        return "security-workshop-" + Math.random();
    }
}
