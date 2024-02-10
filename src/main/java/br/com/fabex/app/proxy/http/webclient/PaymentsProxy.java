package br.com.fabex.app.proxy.http.webclient;

import br.com.fabex.app.dto.Payment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component("paymentsProxyWebClient")
public class PaymentsProxy {

    @Value("${services.payment.url}")
    private String paymentsServiceUrl;

    private final WebClient webClient;

    public PaymentsProxy(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<Payment> createPayment(String traceId, Payment payment) {
        String uri = "http://" + paymentsServiceUrl;
        return webClient.post()
              .uri(uri)
              .header("traceId", traceId)
              .body(Mono.just(payment), Payment.class)
              .retrieve()
              .bodyToMono(Payment.class);
    }
}
