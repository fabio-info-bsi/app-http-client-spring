package br.com.fabex.app;

import br.com.fabex.app.dto.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.UUID;

@SpringBootApplication
@Slf4j
public class ClientApplication {
	public static void main(String[] args) {
		var ctx = SpringApplication.run(ClientApplication.class, args);
		var proxyOpenFeing = ctx.getBean(br.com.fabex.app.proxy.http.openfeign.PaymentsProxy.class);
		Payment response;
		String traceId = UUID.randomUUID().toString();
		var pay = new Payment("", 1);

		log.info("OpenFeign call rquest ...");
		response = proxyOpenFeing.createPayment(traceId, pay);
		log.info("Response => {}", response);

		log.info("RestTemplate call rquest ...");
		var proxyRestTemplate = ctx.getBean(br.com.fabex.app.proxy.http.resttemplate.PaymentsProxy.class);
		response = proxyRestTemplate.createPayment(traceId, pay);
		log.info("Response => {}", response);

		log.info("WebClient call rquest ...");
		var proxyWebClient = ctx.getBean("paymentsProxyWebClient",
				br.com.fabex.app.proxy.http.webclient.PaymentsProxy.class);
		response = proxyWebClient.createPayment(traceId, pay).block();
		log.info("Response => {}", response);
	}

}
