package lk.ijse.gdse71.loslibros.util;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    public void sendOTPEmail(String toEmail, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Your OTP for LosLibros Registration");
        message.setText("Your OTP for registration is: " + otp +
                "\nThis OTP will expire in 5 minutes." +
                "\n\nIf you didn't request this, please ignore this email.");

        mailSender.send(message);
    }

    public void sendPasswordResetEmail(String toEmail, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Password Reset Request for LosLibros");
        message.setText("Your OTP for password reset is: " + otp +
                "\nThis OTP will expire in 5 minutes." +
                "\n\nIf you didn't request a password reset, please ignore this email.");

        mailSender.send(message);
    }
}