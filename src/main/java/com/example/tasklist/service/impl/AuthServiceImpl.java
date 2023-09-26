package com.example.tasklist.service.impl;

import com.example.tasklist.domain.user.User;
import com.example.tasklist.service.AuthService;
import com.example.tasklist.service.UserService;
import com.example.tasklist.web.dto.auth.JwtRequest;
import com.example.tasklist.web.dto.auth.JwtResponse;
import com.example.tasklist.web.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Override
    public JwtResponse login(JwtRequest loginRequest) {
        var jwtResponse = new JwtResponse();
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                        loginRequest.getPassword());
        try {
            authenticationManager.authenticate(authenticationToken);
        } catch (BadCredentialsException e) {

        }
        var user = userService.getByUserName(loginRequest.getUsername());
        jwtResponse.setId(user.getId());
        jwtResponse.setUsername(user.getUsername());
        jwtResponse.setAccessToken(jwtUtil.generateAccessToken(user.getId(), user.getUsername(), user.getRoles()));
        jwtResponse.setRefreshToken(jwtUtil.generateRefreshToken(user.getId(), user.getUsername()));
        return jwtResponse;
    }


    @Override
    public JwtResponse refresh(String refreshToken) {
        return jwtUtil.refreshUserTokens(refreshToken);
    }
}
