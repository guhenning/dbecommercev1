package com.gustavohenning.dbecommercev1.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import java.io.IOException;

public class ExtractUserFromToken {

    private final JwtDecoder jwtDecoder;

    public ExtractUserFromToken(JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    public String extractUserIdentifier(String token) {

        String cleanedToken = token.replaceAll("\\s", "").substring(6);

        Jwt decodedJwt = jwtDecoder.decode(cleanedToken);


        String userIdentifier = decodedJwt.getClaimAsString("sub");

        return userIdentifier;
    }
}
