package lk.ijse.gdse71.loslibros.controller;

import lk.ijse.gdse71.loslibros.dto.ApiResponse;
import lk.ijse.gdse71.loslibros.dto.AuthDTO;
import lk.ijse.gdse71.loslibros.dto.AuthResponseDTO;
import lk.ijse.gdse71.loslibros.dto.RegisterDTO;
import lk.ijse.gdse71.loslibros.util.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
                        "User registered successfully",
                        authService.register(registerDTO)
                )
        );
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
}