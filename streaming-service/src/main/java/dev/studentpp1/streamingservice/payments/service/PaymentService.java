package dev.studentpp1.streamingservice.payments.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.param.checkout.SessionCreateParams.LineItem;
import com.stripe.param.checkout.SessionCreateParams.LineItem.PriceData;
import com.stripe.param.checkout.SessionCreateParams.LineItem.PriceData.ProductData;
import dev.studentpp1.streamingservice.auth.persistence.AuthenticatedUser;
import dev.studentpp1.streamingservice.payments.dto.PaymentRequest;
import dev.studentpp1.streamingservice.payments.dto.PaymentResponse;
import dev.studentpp1.streamingservice.payments.entity.PaymentStatus;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {
    public static final String SESSION_CREATED = "Payment session created";
    @Value("${app.payment.key.secret}")
    private String secretKey;

    @Value("${app.payment.key.public}")
    private String publicKey;

    @Value("${app.payment.url.success}")
    private String successUrl;

    @Value("${app.payment.url.cancel}")
    private String cancelUrl;

    @PostConstruct
    public void connectToStripe() {
        Stripe.apiKey = secretKey;
    }
    // TODO: request -> must be subscription
    // stripe API call
    // Input: productName, amount, quantity, currency
    // Output: sessionId, url (complete payment process)
    public PaymentResponse checkoutProduct(PaymentRequest request) {
        AuthenticatedUser user = (AuthenticatedUser) Objects.requireNonNull(SecurityContextHolder
                        .getContext()
                        .getAuthentication())
                .getPrincipal();
        assert user != null;
        Long userId = user.getAppUser().getId();
        String planName = request.productName();
        ProductData productData = ProductData.builder()
                .setName(planName)
                .build();
        PriceData priceData = PriceData.builder()
                .setCurrency(request.currency() == null ? "uah" : request.currency())
                .setUnitAmount(request.amount())
                .setProductData(productData)
                .build();
        LineItem lineItem = LineItem.builder()
                .setQuantity(request.quantity())
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
                                .putMetadata("userId", userId.toString())
                                .putMetadata("planName", planName)
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

}
