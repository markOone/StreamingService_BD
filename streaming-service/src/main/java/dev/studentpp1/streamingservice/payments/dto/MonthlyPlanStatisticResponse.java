package dev.studentpp1.streamingservice.payments.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record MonthlyPlanStatisticResponse(
        LocalDate currentMonth,
        String planName,
        Long uniqueUsers,
        Long paymentCount,
        BigDecimal totalPlanAmount,
        BigDecimal monthSum,
        BigDecimal percentInTotalSum
) {
}
