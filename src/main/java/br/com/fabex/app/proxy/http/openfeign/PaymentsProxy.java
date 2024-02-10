package br.com.fabex.app.proxy.http.openfeign;

import br.com.fabex.app.dto.Payment;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "payments", url = "${services.payment.url}")
public interface PaymentsProxy {

    @PostMapping("")
    Payment createPayment(@RequestHeader String traceId, @RequestBody Payment payment);

}
