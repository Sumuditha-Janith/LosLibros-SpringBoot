// AuthService.java
package lk.ijse.gdse71.loslibros.service;

import lk.ijse.gdse71.loslibros.dto.AuthDTO;
import lk.ijse.gdse71.loslibros.dto.AuthResponseDTO;
import lk.ijse.gdse71.loslibros.dto.RegisterDTO;
import lk.ijse.gdse71.loslibros.entity.Role;
import lk.ijse.gdse71.loslibros.entity.User;
import lk.ijse.gdse71.loslibros.repository.UserRepository;
import lk.ijse.gdse71.loslibros.util.JwtUtil;
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

        User user = User.builder()
                .username(registerDTO.getUsername())
                .password(passwordEncoder.encode(registerDTO.getPassword()))
                .email(registerDTO.getEmail())
                .role(Role.valueOf(registerDTO.getRole().toUpperCase()))
                .build();

        userRepository.save(user);
        return "User registered successfully";
    }
}