package com.tata.ws.exchange.application.service;

import com.tata.ws.exchange.application.model.request.ForgotPasswordRequest;
import com.tata.ws.exchange.application.model.request.ResetPasswordRequest;
import com.tata.ws.exchange.application.model.request.UserSignInRequest;
import com.tata.ws.exchange.application.model.request.UserSignUpRequest;
import com.tata.ws.exchange.application.model.response.ForgotPasswordResponse;
import com.tata.ws.exchange.application.model.response.ResetPasswordResponse;
import com.tata.ws.exchange.application.model.response.UserSignInResponse;
import com.tata.ws.exchange.application.model.response.UserSignUpResponse;
import com.tata.ws.exchange.domain.model.User;
import com.tata.ws.exchange.infrastructure.config.MessageProperties;
import com.tata.ws.exchange.infrastructure.persistence.UserJpaRepository;
import com.tata.ws.exchange.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserJpaRepository userRepository;
    private final EmailService emailService;

    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    private final MessageProperties messageProperties;

    public UserSignUpResponse signUp(UserSignUpRequest request) {
        User userDuplicated = userRepository.findByUsername(request.getEmail()).orElse(null);

        if (userDuplicated != null) {
            throw new RuntimeException("El usuario ya existe!");
        }

        User newUser = new User();
        newUser.setUsername(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setEnabled(true);

        User userSaved = userRepository.save(newUser);

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        String jwtToken = jwtUtil.generateToken(userSaved);

        UserSignUpResponse response = new UserSignUpResponse();
        response.setEmail(userSaved.getUsername());
        response.setToken(jwtToken);

        return response;
    }

    public UserSignInResponse signIn(UserSignInRequest request) {
        User currentUser = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException(messageProperties.getAuth().getUserNotFound()));

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        String jwtToken = jwtUtil.generateToken(currentUser);

        UserSignInResponse response = new UserSignInResponse();
        response.setEmail(currentUser.getUsername());
        response.setToken(jwtToken);

        return response;
    }

    public ForgotPasswordResponse forgotPassword(ForgotPasswordRequest request) {
        User currentUser = userRepository.findByUsername(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException(messageProperties.getAuth().getUserNotFound()));

        emailService.sendEmail(request.getEmail());

        ForgotPasswordResponse response = new ForgotPasswordResponse();
        response.setResult(true);

        return response;
    }

    public ResetPasswordResponse resetPassword(ResetPasswordRequest request) {
        if (!jwtUtil.isTokenExpired(request.getToken())) {
            String emailCurrentUser = jwtUtil.extractUsername(request.getToken());

            User currentUser = userRepository.findByUsername(emailCurrentUser)
                    .orElseThrow(() -> new UsernameNotFoundException(messageProperties.getAuth().getUserNotFound()));
            currentUser.setPassword(passwordEncoder.encode(request.getPassword()));

            User userSaved = userRepository.save(currentUser);

            boolean isUpdated = userSaved.getUsername() != null;

            ResetPasswordResponse response = new ResetPasswordResponse();
            response.setResult(isUpdated);

            return response;
        } else {
            throw new RuntimeException("Ya pasaron las 24h desde que se generó el token asi que ya expiro vuelve a pedirlo");
        }
    }
}
