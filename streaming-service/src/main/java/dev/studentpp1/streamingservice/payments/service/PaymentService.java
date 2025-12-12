package dev.studentpp1.streamingservice.payments.service;

import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.param.checkout.SessionCreateParams.LineItem.PriceData;
import com.stripe.param.checkout.SessionCreateParams.LineItem.PriceData.ProductData;
import dev.studentpp1.streamingservice.auth.persistence.AuthenticatedUser;
import dev.studentpp1.streamingservice.payments.dto.HistoryPaymentResponse;
import dev.studentpp1.streamingservice.payments.dto.PaymentRequest;
import dev.studentpp1.streamingservice.payments.dto.PaymentResponse;
import dev.studentpp1.streamingservice.payments.entity.Payment;
import dev.studentpp1.streamingservice.payments.entity.PaymentStatus;
import dev.studentpp1.streamingservice.payments.repository.PaymentRepository;
import dev.studentpp1.streamingservice.subscription.entity.SubscriptionPlan;
import dev.studentpp1.streamingservice.subscription.service.SubscriptionPlanUtils;
import dev.studentpp1.streamingservice.users.entity.AppUser;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    public static final String SESSION_CREATED = "Payment session created";
    public static final String USER_ID = "userId";
    public static final String PLAN_NAME = "planName";
    public static final long QUANTITY = 1L;

    @Value("${app.payment.key.secret}")
    private String secretKey;

    @Value("${app.payment.url.success}")
    private String successUrl;

    @Value("${app.payment.url.cancel}")
    private String cancelUrl;

    @Value("${app.payment.currency}")
    private String currency;

    private final SubscriptionPlanUtils subscriptionPlanUtils;
    private final PaymentRepository paymentRepository;

    @PostConstruct
    void initStripe() {
        Stripe.apiKey = secretKey;
    }

    @Transactional
    public PaymentResponse checkoutProduct(PaymentRequest request) {
        SubscriptionPlan plan = subscriptionPlanUtils.findById(request.id());
        return checkoutProduct(plan);
    }

    @Transactional
    public PaymentResponse checkoutProduct(SubscriptionPlan plan) {
        Long userId = getAuthenticatedUserId();

        Session session = createCheckoutSession(plan, userId);
        Payment payment = createPendingPayment(plan, session, userId);

        log.info("Created PENDING payment id={}, sessionId={}, userId={}, plan={}",
                payment.getId(), session.getId(), userId, plan.getName());

        return new PaymentResponse(
                PaymentStatus.PENDING.name(),
                SESSION_CREATED,
                session.getId(),
                session.getUrl()
        );
    }

    private Session createCheckoutSession(SubscriptionPlan plan, Long userId) {
        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(successUrl)
                .setCancelUrl(cancelUrl)
                .setClientReferenceId(userId.toString())
                .addLineItem(buildLineItem(plan))
                .putMetadata(USER_ID, userId.toString())
                .putMetadata(PLAN_NAME, plan.getName())
                .build();

        try {
            return Session.create(params);
        } catch (Exception e) {
            log.error("Failed to create Stripe session for userId={}, plan={}", userId, plan.getName(), e);
            throw new RuntimeException(e);
        }
    }

    private SessionCreateParams.LineItem buildLineItem(SubscriptionPlan plan) {
        return SessionCreateParams.LineItem.builder()
                .setQuantity(QUANTITY)
                .setPriceData(
                        PriceData.builder()
                                .setCurrency(currency)
                                .setUnitAmount(plan.getPrice().longValue())
                                .setProductData(
                                        ProductData.builder()
                                                .setName(plan.getName())
                                                .build()
                                )
                                .build()
                )
                .build();
    }

    private Payment createPendingPayment(SubscriptionPlan plan, Session session, Long userId) {
        try {
            return paymentRepository.save(
                    Payment.builder()
                            .providerSessionId(session.getId())
                            .status(PaymentStatus.PENDING)
                            .amount(plan.getPrice())
                            .createdAt(LocalDateTime.now())
                            .build()
            );
        } catch (Exception e) {
            log.error("Failed to save PENDING payment for userId={}, sessionId={}", userId, session.getId(), e);
            throw e;
        }
    }

    private static Long getAuthenticatedUserId() {
        AppUser appUser = ((AuthenticatedUser) Objects.requireNonNull(
                Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication())
                        .getPrincipal()
        )).getAppUser();
        return Objects.requireNonNull(appUser).getId();
    }

    public List<HistoryPaymentResponse> getUserPayments() {
        return paymentRepository.getPaymentByUserId(getAuthenticatedUserId());
    }

    public List<HistoryPaymentResponse> getPaymentsByUserSubscription(Long userSubscriptionId) {
        return paymentRepository.getPaymentByUserSubscription(userSubscriptionId);
    }

    @Scheduled(cron = "0 30 3 * * *")
    public void deleteOldPayments() {
        LocalDateTime lastYear = LocalDateTime.now().minusYears(1);
        int deleted = paymentRepository.deletePaymentsBefore(lastYear);
        log.info("Deleted {} old payments older than {}", deleted, lastYear);
    }
}
