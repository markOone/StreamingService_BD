package dev.studentpp1.streamingservice.payments.controller;

import dev.studentpp1.streamingservice.payments.dto.PaymentRequest;
import dev.studentpp1.streamingservice.payments.dto.PaymentResponse;
import dev.studentpp1.streamingservice.payments.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// TODO: get all payment by user (status, date, name of sub, amount)
// TODO: get all payment by user_subscription
@RequestMapping("/api/payments")
@RestController
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/checkout")
    public ResponseEntity<PaymentResponse> checkoutProduct(@RequestBody PaymentRequest request) {
        PaymentResponse response = paymentService.checkoutProduct(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
