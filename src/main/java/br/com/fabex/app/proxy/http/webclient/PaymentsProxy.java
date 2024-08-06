package br.com.fabex.app.proxy.http.webclient;

import br.com.fabex.app.dto.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import reactor.util.retry.RetryBackoffSpec;

import java.time.Duration;

@Component("paymentsProxyWebClient")
@Slf4j
public class PaymentsProxy {

    @Value("${services.payment.url}")
    private String paymentsServiceUrl;

    private final WebClient webClient;

    public PaymentsProxy(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<Payment> createPayment(String traceId, Payment payment) {
        return webClient.post()
                .uri(paymentsServiceUrl.concat("/payment"))
                .header("traceId", traceId)
                .body(Mono.just(payment), Payment.class)
                .retrieve()
                .bodyToMono(Payment.class)
                .retryWhen(this.retry())
                .doOnError(error -> log.error("#createPayment: {}", error.getMessage()));
    }

    public Mono<Payment> createPaymentWithRetry(String traceId, Payment payment) {
        return webClient.post()
                .uri(paymentsServiceUrl.concat("/payment-bad-request"))
                .header("traceId", traceId)
                .body(Mono.just(payment), Payment.class)
                .retrieve()
                .bodyToMono(Payment.class)
                .retryWhen(this.retry())
                .doOnError(error -> log.error("#createPaymentWithRetry: {}", error.getMessage()))
                .onErrorResume(this::getFallback);
    }

    public Mono<Payment> createPaymentWithTimeout(String traceId, Payment payment) {
        return webClient.post()
                .uri(paymentsServiceUrl.concat("/payment-delay"))
                .header("traceId", traceId)
                .body(Mono.just(payment), Payment.class)
                .retrieve()
                .bodyToMono(Payment.class)
                .timeout(Duration.ofMillis(900));
    }

    private Mono<Payment> getFallback(final Throwable error) {
        log.error("#getFallback: {}", error.getMessage());
        return Mono.empty();
    }

    private RetryBackoffSpec retry() {
        return Retry.backoff(3, Duration.ofSeconds(1));
    }
}
