package dev.studentpp1.streamingservice.payments.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.param.checkout.SessionCreateParams.LineItem;
import com.stripe.param.checkout.SessionCreateParams.LineItem.PriceData;
import com.stripe.param.checkout.SessionCreateParams.LineItem.PriceData.ProductData;
import dev.studentpp1.streamingservice.auth.persistence.AuthenticatedUser;
import dev.studentpp1.streamingservice.payments.dto.HistoryPaymentResponse;
import dev.studentpp1.streamingservice.payments.dto.PaymentRequest;
import dev.studentpp1.streamingservice.payments.dto.PaymentResponse;
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
    public void connectToStripe() {
        Stripe.apiKey = secretKey;
    }

    // stripe API call
    // Input: productName, amount, quantity, currency
    // Output: sessionId, url (complete payment process)
    @Transactional
    public PaymentResponse checkoutProduct(PaymentRequest request) {
        SubscriptionPlan subscriptionPlan = subscriptionPlanUtils.findById(request.id());
        return getSubscriptionPayment(subscriptionPlan);
    }

    public PaymentResponse checkoutProduct(SubscriptionPlan subscriptionPlan) {
        return getSubscriptionPayment(subscriptionPlan);
    }

    private PaymentResponse getSubscriptionPayment(SubscriptionPlan subscriptionPlan) {
        Long userId = getAuthenticatedUserId();
        ProductData productData = ProductData.builder()
                .setName(subscriptionPlan.getName())
                .build();
        PriceData priceData = PriceData.builder()
                .setCurrency(currency)
                .setUnitAmount(subscriptionPlan.getPrice().longValue())
                .setProductData(productData)
                .build();
        LineItem lineItem = LineItem.builder()
                .setQuantity(QUANTITY)
                .setPriceData(priceData)
                .build();
        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(successUrl)
                .setCancelUrl(cancelUrl)
                .addLineItem(lineItem)
                .setClientReferenceId(userId.toString())
                .setPaymentIntentData(
                        SessionCreateParams.PaymentIntentData.builder()
                                .putMetadata(USER_ID, userId.toString())
                                .putMetadata(PLAN_NAME, subscriptionPlan.getName())
                                .build()
                )
                .build();
        try {
            Session session = Session.create(params);
            return new PaymentResponse(
                    PaymentStatus.PENDING.toString(),
                    SESSION_CREATED,
                    session.getId(),
                    session.getUrl()
            );
        } catch (StripeException e) {
            log.error("Stripe error: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private static Long getAuthenticatedUserId() {
        AppUser appUser = ((AuthenticatedUser) Objects.requireNonNull(
                Objects.requireNonNull(SecurityContextHolder
                                .getContext()
                                .getAuthentication()
                        )
                        .getPrincipal())
        ).getAppUser();
        assert appUser != null;
        return appUser.getId();
    }

    public List<HistoryPaymentResponse> getUserPayments() {
        return paymentRepository.getPaymentByUserId(getAuthenticatedUserId());
    }

    public List<HistoryPaymentResponse> getPaymentsByUserSubscription(Long userSubscriptionId) {
        return paymentRepository.getPaymentByUserSubscription(userSubscriptionId);
    }

    @Scheduled(cron = "0 0 3 * * *")
    public void deleteOldPayments() {
        LocalDateTime lastYear = LocalDateTime.now().minusYears(1);
        int deleted = paymentRepository.deletePaymentsBefore(lastYear);
        log.info("Deleted {} old payments older than {}", deleted, lastYear);
    }

}