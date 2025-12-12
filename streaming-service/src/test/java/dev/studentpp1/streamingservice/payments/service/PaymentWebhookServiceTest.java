package dev.studentpp1.streamingservice.payments.service;

import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import dev.studentpp1.streamingservice.payments.entity.Payment;
import dev.studentpp1.streamingservice.payments.entity.PaymentStatus;
import dev.studentpp1.streamingservice.payments.repository.PaymentRepository;
import dev.studentpp1.streamingservice.subscription.entity.UserSubscription;
import dev.studentpp1.streamingservice.subscription.service.SubscriptionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentWebhookServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private SubscriptionService subscriptionService;

    @InjectMocks
    private PaymentWebhookService paymentWebhookService;

    @Test
    void handlePaymentEventWhenSessionCompleted_updatesExistingPayment() {
        Event event = mock(Event.class);
        when(event.getType()).thenReturn("checkout.session.completed");

        EventDataObjectDeserializer deserializer = mock(EventDataObjectDeserializer.class);
        when(event.getDataObjectDeserializer()).thenReturn(deserializer);

        String json = """
                {
                  "id": "cs_123",
                  "metadata": { "userId": "42", "planName": "PREMIUM" }
                }
                """;
        when(deserializer.getRawJson()).thenReturn(json);

        Payment payment = Payment.builder()
                .providerSessionId("cs_123")
                .status(PaymentStatus.PENDING)
                .amount(new BigDecimal("150.00"))
                .build();

        when(paymentRepository.findByProviderPaymentIdForUpdate("cs_123"))
                .thenReturn(Optional.of(payment));

        UserSubscription userSubscription = new UserSubscription();
        when(subscriptionService.createUserSubscription("PREMIUM", "42"))
                .thenReturn(userSubscription);

        paymentWebhookService.handlePaymentEvent(event);

        verify(paymentRepository).findByProviderPaymentIdForUpdate("cs_123");
        verify(subscriptionService).createUserSubscription("PREMIUM", "42");
        verify(paymentRepository, never()).save(any());

        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.COMPLETED);
        assertThat(payment.getUserSubscription()).isEqualTo(userSubscription);
        assertThat(payment.getPaidAt()).isNotNull();
    }

    @Test
    void handlePaymentEventWhenSessionCompleted_andAlreadyCompleted_doesNothing() {
        Event event = mock(Event.class);
        when(event.getType()).thenReturn("checkout.session.completed");

        EventDataObjectDeserializer deserializer = mock(EventDataObjectDeserializer.class);
        when(event.getDataObjectDeserializer()).thenReturn(deserializer);

        String json = """
                {
                  "id": "cs_999",
                  "metadata": { "userId": "42", "planName": "PREMIUM" }
                }
                """;
        when(deserializer.getRawJson()).thenReturn(json);

        LocalDateTime paidAt = LocalDateTime.now().minusMinutes(3);

        Payment payment = Payment.builder()
                .providerSessionId("cs_999")
                .status(PaymentStatus.COMPLETED)
                .paidAt(paidAt)
                .amount(new BigDecimal("150.00"))
                .build();

        when(paymentRepository.findByProviderPaymentIdForUpdate("cs_999"))
                .thenReturn(Optional.of(payment));

        paymentWebhookService.handlePaymentEvent(event);

        verify(paymentRepository).findByProviderPaymentIdForUpdate("cs_999");
        verifyNoInteractions(subscriptionService);
        verify(paymentRepository, never()).save(any());

        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.COMPLETED);
        assertThat(payment.getPaidAt()).isEqualTo(paidAt);
    }

    @Test
    void handlePaymentEventWhenSessionExpired_updatesExistingPayment() {
        Event event = mock(Event.class);
        when(event.getType()).thenReturn("checkout.session.expired");

        EventDataObjectDeserializer deserializer = mock(EventDataObjectDeserializer.class);
        when(event.getDataObjectDeserializer()).thenReturn(deserializer);

        String json = """
                {
                  "id": "cs_456",
                  "metadata": { "userId": "77", "planName": "BASIC" }
                }
                """;
        when(deserializer.getRawJson()).thenReturn(json);

        Payment payment = Payment.builder()
                .providerSessionId("cs_456")
                .status(PaymentStatus.PENDING)
                .amount(new BigDecimal("99.00"))
                .build();

        when(paymentRepository.findByProviderPaymentIdForUpdate("cs_456"))
                .thenReturn(Optional.of(payment));

        paymentWebhookService.handlePaymentEvent(event);

        verify(paymentRepository).findByProviderPaymentIdForUpdate("cs_456");
        verifyNoInteractions(subscriptionService);
        verify(paymentRepository, never()).save(any());

        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.FAILED);
        assertThat(payment.getUserSubscription()).isNull();
        assertThat(payment.getPaidAt()).isNull();
    }

    @Test
    void handlePaymentEventWhenSessionExpired_andAlreadyCompleted_doesNothing() {
        Event event = mock(Event.class);
        when(event.getType()).thenReturn("checkout.session.expired");

        EventDataObjectDeserializer deserializer = mock(EventDataObjectDeserializer.class);
        when(event.getDataObjectDeserializer()).thenReturn(deserializer);

        String json = """
                {
                  "id": "cs_777",
                  "metadata": { "userId": "77", "planName": "BASIC" }
                }
                """;
        when(deserializer.getRawJson()).thenReturn(json);

        LocalDateTime paidAt = LocalDateTime.now().minusMinutes(7);

        Payment payment = Payment.builder()
                .providerSessionId("cs_777")
                .status(PaymentStatus.COMPLETED)
                .paidAt(paidAt)
                .amount(new BigDecimal("99.00"))
                .build();

        when(paymentRepository.findByProviderPaymentIdForUpdate("cs_777"))
                .thenReturn(Optional.of(payment));

        paymentWebhookService.handlePaymentEvent(event);

        verify(paymentRepository).findByProviderPaymentIdForUpdate("cs_777");
        verifyNoInteractions(subscriptionService);
        verify(paymentRepository, never()).save(any());

        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.COMPLETED);
        assertThat(payment.getPaidAt()).isEqualTo(paidAt);
    }

    @Test
    void handlePaymentEventWhenTypeIsUnhandled() {
        Event event = mock(Event.class);
        when(event.getType()).thenReturn("some_other_event");

        paymentWebhookService.handlePaymentEvent(event);

        verifyNoInteractions(subscriptionService);
        verifyNoInteractions(paymentRepository);
    }

    @Test
    void deleteStalePendingPayments_callsRepositoryWithPendingAndThreshold() {
        when(paymentRepository.deleteByStatusAndCreatedAtBefore(
                eq(PaymentStatus.PENDING),
                any(LocalDateTime.class))
        ).thenReturn(3);

        paymentWebhookService.deleteStalePendingPayments();

        verify(paymentRepository, times(1))
                .deleteByStatusAndCreatedAtBefore(
                        eq(PaymentStatus.PENDING),
                        any(LocalDateTime.class)
                );
    }
}
