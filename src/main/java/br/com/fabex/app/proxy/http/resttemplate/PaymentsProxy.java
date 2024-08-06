package br.com.fabex.app.proxy.http.resttemplate;

import br.com.fabex.app.dto.Payment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class PaymentsProxy {

    @Value("${services.payment.url}")
    private String paymentsServiceUrl;

    private final RestTemplate rest;

    public PaymentsProxy(final RestTemplate restParam) {
        this.rest = restParam;
    }

    public Payment createPayment(String traceId, Payment payment){

        HttpHeaders headers = new HttpHeaders();
        headers.add("requestId", traceId);

        HttpEntity<Payment> httpEntity = new HttpEntity<>(payment, headers);

        ResponseEntity<Payment> response = rest.exchange(paymentsServiceUrl.concat("/payment"),
                HttpMethod.POST,
                httpEntity,
                Payment.class);
        return response.getBody();
    }
}
