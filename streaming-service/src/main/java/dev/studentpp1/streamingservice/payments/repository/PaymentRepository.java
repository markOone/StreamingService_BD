package dev.studentpp1.streamingservice.payments.repository;

import dev.studentpp1.streamingservice.payments.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
