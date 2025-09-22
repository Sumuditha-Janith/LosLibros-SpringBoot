package lk.ijse.gdse71.loslibros.util;

import lk.ijse.gdse71.loslibros.dto.AuthDTO;
import lk.ijse.gdse71.loslibros.dto.AuthResponseDTO;
import lk.ijse.gdse71.loslibros.dto.RegisterDTO;
import lk.ijse.gdse71.loslibros.entity.Role;
import lk.ijse.gdse71.loslibros.entity.User;
import lk.ijse.gdse71.loslibros.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final OTPService otpService;
    private final EmailService emailService;

    public AuthResponseDTO authenticate(AuthDTO authDTO) {
        User user = userRepository.findByUsername(authDTO.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));

        if (!passwordEncoder.matches(authDTO.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid Password");
        }

        String token = jwtUtil.generateToken(authDTO.getUsername());

        return new AuthResponseDTO(token, user.getRole().name(), user.getUsername());
    }

    public String register(RegisterDTO registerDTO) {
        if (userRepository.existsByUsername(registerDTO.getUsername())) {
            throw new RuntimeException("Username is already exist");
        }

        if (userRepository.existsByEmail(registerDTO.getEmail())) {
            throw new RuntimeException("Email is already registered");
        }

        // Generate and send OTP
        String otp = otpService.generateOTP(registerDTO.getEmail());
        emailService.sendOTPEmail(registerDTO.getEmail(), otp);

        return "OTP sent to your email. Please verify to complete registration.";
    }

    public String verifyOtpAndRegister(String email, String otp, RegisterDTO registerDTO) {
        // Validate that the email matches
        if (!email.equals(registerDTO.getEmail())) {
            throw new RuntimeException("Email mismatch");
        }

        if (!otpService.validateOTP(email, otp)) {
            throw new RuntimeException("Invalid or expired OTP");
        }

        User user = User.builder()
                .username(registerDTO.getUsername())
                .password(passwordEncoder.encode(registerDTO.getPassword()))
                .address(registerDTO.getAddress())
                .email(registerDTO.getEmail())
                .role(Role.valueOf(registerDTO.getRole().toUpperCase()))
                .build();

        userRepository.save(user);
        return "User registered successfully";
    }

    public String initiatePasswordReset(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        String otp = otpService.generateOTP(email);
        emailService.sendPasswordResetEmail(email, otp);

        return "Password reset OTP sent to your email";
    }

    public String resetPassword(String email, String otp, String newPassword) {
        if (!otpService.validateOTP(email, otp)) {
            throw new RuntimeException("Invalid or expired OTP");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return "Password reset successfully";
    }
}