package ntnu.idi.bidata.IDATT2105.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import ntnu.idi.bidata.IDATT2105.dtos.PaymentResponseDTO;
import ntnu.idi.bidata.IDATT2105.services.auth.AuthService;
import ntnu.idi.bidata.IDATT2105.services.payment.PaymentService;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*")
public class PaymentController {
  
  private final PaymentService paymentService;
  private final AuthService authService;

  @Autowired
  public PaymentController(PaymentService paymentService, AuthService authService) {
    this.paymentService = paymentService;
    this.authService = authService;
  }

  @PostMapping
  public ResponseEntity<PaymentResponseDTO> processPayment(
    @RequestHeader("Authorization") String authHeader,
    @Valid @RequestBody PaymentResponseDTO paymentDTO
  ) {
    String token = authHeader.substring(7); // Remove "Bearer " prefix
    Long userId = authService.validateTokenAndGetUserId(token);
    
    // PaymentResponseDTO response = paymentService.processPayment(userId, paymentDTO);
    // return ResponseEntity.ok(response);
    return null; // Placeholder for actual implementation
  }
}
