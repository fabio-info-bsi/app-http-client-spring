package br.com.fabex.app;

import br.com.fabex.app.dto.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@SpringBootApplication
@Slf4j
public class ChainedWebClientApplication {
	public static void main(String[] args) {
		var ctx = SpringApplication.run(ChainedWebClientApplication.class, args);
		String traceId = UUID.randomUUID().toString();

		var proxyWebClient = ctx.getBean("paymentsProxyWebClient",
				br.com.fabex.app.proxy.http.webclient.PaymentsProxy.class);

		Payment response = proxyWebClient.createPayment(traceId, new Payment("", 1))
				//Second request (dependency - first request)
				.flatMap(resp2 -> proxyWebClient.createPayment(traceId, new Payment("", 2)))
				.doOnSuccess(resp2 -> log.info("req2 success."))
				//Third request (dependency - second request)
				.flatMap(resp3 -> proxyWebClient.createPayment(traceId, new Payment("", 3)))
				.doOnSuccess(resp3 -> log.info("req3 success."))
				//Requests interleaved (no dependency)
				.flatMap(rep -> Flux.merge(
								proxyWebClient.createPayment(traceId, new Payment("", 101)),
								proxyWebClient.createPayment(traceId, new Payment("", 102)),
								proxyWebClient.createPayment(traceId, new Payment("", 103)),
								proxyWebClient.createPayment(traceId, new Payment("", 104)),
								proxyWebClient.createPayment(traceId, new Payment("", 105)),
								proxyWebClient.createPayment(traceId, new Payment("", 106)),
								proxyWebClient.createPayment(traceId, new Payment("", 107)),
								proxyWebClient.createPayment(traceId, new Payment("", 108)),
								proxyWebClient.createPayment(traceId, new Payment("", 109)),
								proxyWebClient.createPayment(traceId, new Payment("", 110)),
								proxyWebClient.createPayment(traceId, new Payment("", 111)),
								proxyWebClient.createPayment(traceId, new Payment("", 112))
						).collectList()
						//logic to merge responses interleaved
						.flatMap(multiresp -> Mono.just(multiresp.getFirst()))
				)
				//logic merge all requests
				.map(rep -> rep)
				.block();
		log.info("Response => {}", response);
	}

}
