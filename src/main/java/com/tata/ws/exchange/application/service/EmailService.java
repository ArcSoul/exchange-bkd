package com.tata.ws.exchange.application.service;

import com.tata.ws.exchange.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JwtUtil jwtUtil;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private final JavaMailSender mailSender;

    @Value("${spring.mail.url-reset-password}")
    private String frontendPath;

    @Async
    public void sendEmail(String email) {
        // Genera el token único y el mensaje de correo electrónico
        String token = jwtUtil.generateResetToken(email);
        String resetUrl = frontendPath + "/pages/auth/reset-password?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Recuperación de contraseña");
        message.setText("Para restablecer tu contraseña, haz clic en el siguiente enlace: \n" + resetUrl);

        // Envía el correo
        mailSender.send(message);
    }
}
