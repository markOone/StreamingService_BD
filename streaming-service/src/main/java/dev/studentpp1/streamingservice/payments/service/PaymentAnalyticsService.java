package dev.studentpp1.streamingservice.payments.service;

import dev.studentpp1.streamingservice.payments.dto.MonthlyPlanStatisticResponse;
import dev.studentpp1.streamingservice.payments.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentAnalyticsService {

    private final PaymentRepository paymentRepository;

    public List<MonthlyPlanStatisticResponse> getMonthlyPlanStatistics() {
        return paymentRepository.findMonthlyPlanStatistics()
                .stream()
                .map(p -> new MonthlyPlanStatisticResponse(
                        p.getCurrentMonth(),
                        p.getPlanName(),
                        p.getUniqueUsers(),
                        p.getPaymentCount(),
                        p.getTotalPlanAmount(),
                        p.getMonthSum(),
                        p.getPercentInTotalSum()
                ))
                .toList();
    }
}
