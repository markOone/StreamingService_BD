package dev.studentpp1.streamingservice.payments.controller;

import dev.studentpp1.streamingservice.payments.dto.HistoryPaymentResponse;
import dev.studentpp1.streamingservice.payments.dto.MonthlyPlanStatisticResponse;
import dev.studentpp1.streamingservice.payments.dto.PaymentRequest;
import dev.studentpp1.streamingservice.payments.dto.PaymentResponse;
import dev.studentpp1.streamingservice.payments.service.PaymentAnalyticsService;
import dev.studentpp1.streamingservice.payments.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/payments")
@RestController
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    private final PaymentAnalyticsService paymentAnalyticsService;

    @PostMapping("/checkout")
    public ResponseEntity<PaymentResponse> checkoutProduct(@RequestBody PaymentRequest request) {
        PaymentResponse response = paymentService.checkoutProduct(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/user")
    public ResponseEntity<List<HistoryPaymentResponse>> getPaymentsByUser() {
        List<HistoryPaymentResponse> response = paymentService.getUserPayments();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/user/subscription/{id}")
    public ResponseEntity<List<HistoryPaymentResponse>> getPaymentsByUserSubscription(@PathVariable("id") Long id) {
        List<HistoryPaymentResponse> response = paymentService.getPaymentsByUserSubscription(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/monthly-plans")
    public List<MonthlyPlanStatisticResponse> getMonthlyPlansAnalytics() {
        return paymentAnalyticsService.getMonthlyPlanStatistics();
    }
}
