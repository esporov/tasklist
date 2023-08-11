package com.example.tasklist.web.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.RegisteredClaims;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.tasklist.domain.exception.AccessDeniedException;
import com.example.tasklist.domain.user.Role;
import com.example.tasklist.domain.user.User;
import com.example.tasklist.service.UserService;
import com.example.tasklist.service.props.JwtProperties;
import com.example.tasklist.web.dto.auth.JwtResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtUtil {
    private final JwtProperties jwtProperties;
    private final UserService userService;
    private final JwtUserDetailsService jwtUserDetailsService;

    public String generateAccessToken(Long userId, String username, Set<Role> roles) {
        var duration = Duration.ofHours(jwtProperties.getAccess());
        var expirationDate = Instant.now().plus(duration);

        return JWT.create()
                .withSubject("User details")
                .withClaim("id", userId)
                .withClaim("username", username)
                .withClaim("roles", resolveRoles(roles))
                .withIssuedAt(Instant.now())
                .withIssuer("spring")
                .withExpiresAt(expirationDate)
                .sign(Algorithm.HMAC256(jwtProperties.getSecret()));
    }

    public String generateRefreshToken(Long userId, String username) {
        var duration = Duration.ofHours(jwtProperties.getRefresh());
        var expirationDate = Instant.now().plus(duration);

        return JWT.create()
                .withSubject("User details")
                .withClaim("id", userId)
                .withClaim("username", username)
                .withIssuedAt(Instant.now())
                .withIssuer("spring")
                .withExpiresAt(expirationDate)
                .sign(Algorithm.HMAC256(jwtProperties.getSecret()));

    }

    public JwtResponse refreshUserTokens(String refreshToken) {
        var jwtResponse = new JwtResponse();
        if (!validateDateToken(refreshToken)) {
            throw new AccessDeniedException();
        }
        Long userId = Long.valueOf(getId(refreshToken));
        User user = userService.getById(userId);
        jwtResponse.setUsername(user.getUsername());
        jwtResponse.setAccessToken(generateAccessToken(userId, user.getUsername(), user.getRoles()));
        jwtResponse.setRefreshToken(generateRefreshToken(userId, user.getUsername()));
        return jwtResponse;
    }

    public UserDetails getUserDetails(String username) {
        return jwtUserDetailsService.loadUserByUsername(username);
    }

    public boolean validateDateToken(String token) {
        return decodedJWT(token).getClaim(RegisteredClaims.EXPIRES_AT).asInstant().isAfter(Instant.now());
    }

    public String validateTokenAndRetrieveClaim(String token) throws JWTVerificationException {
        return decodedJWT(token).getClaim("username").asString();
    }

    private String getId(String token) {
        return decodedJWT(token).getClaim("id").asString();
    }

    private DecodedJWT decodedJWT(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(jwtProperties.getSecret()))
                    .withSubject("User details")
                    .withIssuer("spring")
                    .build();
            return verifier.verify(token);
        } catch (JWTVerificationException e) {
            // LoggerFactory.getLogger(JWTVerificationException.class).error(e.getMessage(),e);
        }
        return null;
    }
    private List<String> resolveRoles(Set<Role> roles) {
        return roles.stream()
                .map(Enum::name)
                .collect(Collectors.toList());
    }
}
