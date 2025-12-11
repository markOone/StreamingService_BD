package dev.studentpp1.streamingservice.payments.service;

import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import dev.studentpp1.streamingservice.auth.persistence.AuthenticatedUser;
import dev.studentpp1.streamingservice.payments.dto.PaymentRequest;
import dev.studentpp1.streamingservice.payments.dto.PaymentResponse;
import dev.studentpp1.streamingservice.payments.entity.PaymentStatus;
import dev.studentpp1.streamingservice.payments.repository.PaymentRepository;
import dev.studentpp1.streamingservice.subscription.entity.SubscriptionPlan;
import dev.studentpp1.streamingservice.subscription.service.SubscriptionPlanUtils;
import dev.studentpp1.streamingservice.users.entity.AppUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private SubscriptionPlanUtils subscriptionPlanUtils;

    @InjectMocks
    private PaymentService paymentService;

    private void mockAuthenticatedUser(Long userId) {
        AppUser appUser = AppUser.builder()
                .id(userId)
                .build();
        AuthenticatedUser principal = new AuthenticatedUser(appUser);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(principal);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void checkoutProduct_createsStripeSessionAndReturnsResponse() throws Exception {
        Long userId = 42L;
        mockAuthenticatedUser(userId);
        PaymentRequest request = new PaymentRequest(1L);
        SubscriptionPlan plan = SubscriptionPlan.builder()
                .id(1L)
                .name("PREMIUM")
                .price(new BigDecimal("20000"))
                .duration(30)
                .description("Premium plan")
                .build();

        when(subscriptionPlanUtils.findById(1L)).thenReturn(plan);
        ReflectionTestUtils.setField(paymentService, "currency", "usd");
        ReflectionTestUtils.setField(paymentService, "successUrl", "https://example.com/success");
        ReflectionTestUtils.setField(paymentService, "cancelUrl", "https://example.com/cancel");

        Session fakeSession = mock(Session.class);
        when(fakeSession.getId()).thenReturn("sess_123");
        when(fakeSession.getUrl()).thenReturn("https://stripe.com/checkout/sess_123");

        try (MockedStatic<Session> sessionMock = mockStatic(Session.class)) {
            sessionMock.when(() -> Session.create(any(SessionCreateParams.class)))
                    .thenReturn(fakeSession);
            PaymentResponse response = paymentService.checkoutProduct(request);
            assertThat(response.status()).isEqualTo(PaymentStatus.PENDING.toString());
            assertThat(response.message()).isEqualTo(PaymentService.SESSION_CREATED);
            assertThat(response.sessionId()).isEqualTo("sess_123");
            assertThat(response.sessionUrl()).isEqualTo("https://stripe.com/checkout/sess_123");
            verify(subscriptionPlanUtils).findById(1L);
            sessionMock.verify(() -> Session.create(any(SessionCreateParams.class)));
            verifyNoInteractions(paymentRepository);
        }
    }
}