package dev.studentpp1.streamingservice.payments.controller;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import dev.studentpp1.streamingservice.payments.service.PaymentStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/payments/webhook")
@RequiredArgsConstructor
public class PaymentWebhookController {

    @Value("${app.payment.webhook.key}")
    private String endpointSecret;

    private final PaymentStatusService paymentStatusService;

    @PostMapping
    public ResponseEntity<Void> handleStripeEvent(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader
    ) throws SignatureVerificationException {
        Event event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
        paymentStatusService.handlePaymentEvent(event);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
