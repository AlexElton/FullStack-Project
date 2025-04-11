package ntnu.idi.bidata.IDATT2105.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import ntnu.idi.bidata.IDATT2105.dtos.AuthRequestDTO;
import ntnu.idi.bidata.IDATT2105.dtos.AuthResponseDTO;
import ntnu.idi.bidata.IDATT2105.dtos.RegisterRequestDTO;
import ntnu.idi.bidata.IDATT2105.services.auth.AuthService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    
    private final AuthService authService;
    
    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterRequestDTO request) {
        AuthResponseDTO response = authService.register(request);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody AuthRequestDTO request) {
        AuthResponseDTO response = authService.login(request);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/validate")
    public ResponseEntity<Long> validateToken(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7); // Remove "Bearer " prefix
        Long userId = authService.validateTokenAndGetUserId(token);
        return ResponseEntity.ok(userId);
    }
    
    @GetMapping("/check-email/{email}")
    public ResponseEntity<Boolean> checkEmailExists(@PathVariable String email) {
        boolean exists = authService.emailExists(email);
        return ResponseEntity.ok(exists);
    }
    
    @GetMapping("/check-username/{username}")
    public ResponseEntity<Boolean> checkUsernameExists(@PathVariable String username) {
        boolean exists = authService.usernameExists(username);
        return ResponseEntity.ok(exists);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponseDTO> refreshToken(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7); // Remove "Bearer " prefix
        AuthResponseDTO response = authService.refreshToken(token);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7); // Remove "Bearer " prefix
        authService.logout(token);
        return ResponseEntity.ok().build();
    }
} 