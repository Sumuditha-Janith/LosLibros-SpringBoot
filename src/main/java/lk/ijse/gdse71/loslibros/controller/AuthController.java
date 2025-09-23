package lk.ijse.gdse71.loslibros.controller;

import lk.ijse.gdse71.loslibros.dto.ApiResponse;
import lk.ijse.gdse71.loslibros.dto.AuthDTO;
import lk.ijse.gdse71.loslibros.dto.AuthResponseDTO;
import lk.ijse.gdse71.loslibros.dto.RegisterDTO;
import lk.ijse.gdse71.loslibros.util.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerUser(@RequestBody RegisterDTO registerDTO) {
        return ResponseEntity.ok(
                new ApiResponse(
                        200,
                        "OTP sent to email",
                        authService.register(registerDTO)
                )
        );
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse> verifyOtpAndRegister(
            @RequestParam String email,
            @RequestParam String otp,
            @RequestBody RegisterDTO registerDTO) {
        try {
            String result = authService.verifyOtpAndRegister(email, otp, registerDTO);
            return ResponseEntity.ok(new ApiResponse(200, "User registered successfully", result));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(400, e.getMessage(), null));
        }
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<ApiResponse> resendOtp(@RequestParam String email) {
        try {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(400, "Please submit the registration form again", null));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(500, "Error resending OTP", null));
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse> forgotPassword(@RequestParam String email) {
        try {
            String result = authService.initiatePasswordReset(email);
            return ResponseEntity.ok(new ApiResponse(200, "OTP sent to email", result));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(400, e.getMessage(), null));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse> resetPassword(
            @RequestParam String email,
            @RequestParam String otp,
            @RequestParam String newPassword) {
        try {
            String result = authService.resetPassword(email, otp, newPassword);
            return ResponseEntity.ok(new ApiResponse(200, "Password reset successfully", result));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(400, e.getMessage(), null));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody AuthDTO authDTO) {
        try {
            AuthResponseDTO auth = authService.authenticate(authDTO);
            return ResponseEntity.ok(new ApiResponse(200, "OK", auth));
        } catch (BadCredentialsException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse(401, "Invalid username or password", null));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(500, "Server error", null));
        }
    }

    @PostMapping("/admin/create-user")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> createUserByAdmin(@RequestBody RegisterDTO registerDTO) {
        try {
            String result = authService.registerByAdmin(registerDTO);
            return ResponseEntity.ok(new ApiResponse(200, "User created successfully", result));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(400, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(500, "Server error", null));
        }
    }

}