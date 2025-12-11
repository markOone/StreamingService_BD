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
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentStatusServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private SubscriptionService subscriptionService;

    @InjectMocks
    private PaymentStatusService paymentStatusService;

    @Test
    void handlePaymentEventWhenPaymentSucceeded() {
        Event event = mock(Event.class);
        when(event.getType()).thenReturn("payment_intent.succeeded");

        EventDataObjectDeserializer deserializer = mock(EventDataObjectDeserializer.class);
        when(event.getDataObjectDeserializer()).thenReturn(deserializer);

        String json = """
                {
                  "id": "pi_123",
                  "metadata": {
                    "userId": "42",
                    "planName": "PREMIUM"
                  },
                  "amount": 15000,
                  "currency": "usd"
                }
                """;

        when(deserializer.getRawJson()).thenReturn(json);

        UserSubscription userSubscription = new UserSubscription();
        when(subscriptionService.createUserSubscription("PREMIUM", "42"))
                .thenReturn(userSubscription);

        paymentStatusService.handlePaymentEvent(event);

        verify(subscriptionService).createUserSubscription("PREMIUM", "42");

        ArgumentCaptor<Payment> paymentCaptor = ArgumentCaptor.forClass(Payment.class);
        verify(paymentRepository).save(paymentCaptor.capture());

        Payment saved = paymentCaptor.getValue();
        assertThat(saved.getStatus()).isEqualTo(PaymentStatus.COMPLETED);
        assertThat(saved.getUserSubscription()).isEqualTo(userSubscription);
        assertThat(saved.getAmount()).isEqualTo(15000);
    }

    @Test
    void handlePaymentEventWhenPaymentFailed() {
        Event event = mock(Event.class);
        when(event.getType()).thenReturn("payment_intent.payment_failed");

        EventDataObjectDeserializer deserializer = mock(EventDataObjectDeserializer.class);
        when(event.getDataObjectDeserializer()).thenReturn(deserializer);

        String json = """
                {
                  "id": "pi_456",
                  "metadata": {
                    "userId": "77",
                    "planName": "BASIC"
                  },
                  "amount": 9900,
                  "currency": "usd",
                  "last_payment_error": {
                    "message": "Card declined"
                  }
                }
                """;

        when(deserializer.getRawJson()).thenReturn(json);

        paymentStatusService.handlePaymentEvent(event);

        verifyNoInteractions(subscriptionService);

        ArgumentCaptor<Payment> paymentCaptor = ArgumentCaptor.forClass(Payment.class);
        verify(paymentRepository).save(paymentCaptor.capture());

        Payment saved = paymentCaptor.getValue();
        assertThat(saved.getStatus()).isEqualTo(PaymentStatus.FAILED);
        assertThat(saved.getAmount()).isEqualTo(9900);
        assertThat(saved.getUserSubscription()).isNull();
    }

    @Test
    void handlePaymentEventWhenTypeIsUnhandled() {
        Event event = mock(Event.class);
        when(event.getType()).thenReturn("some_other_event");

        paymentStatusService.handlePaymentEvent(event);

        verifyNoInteractions(subscriptionService);
        verifyNoInteractions(paymentRepository);
    }
}
