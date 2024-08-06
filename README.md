# app-http-client-spring
Um exemplo da utilização dos clients http: OpenFeign, RestTemplate e WebClient.

Também, a implementação de requests http encadeadas com usuo de WebClient (Reative) na classe [`br.com.fabex.app.ChainedWebClientApplication`](src/main/java/br/com/fabex/app/ChainedWebClientApplication.java), retentativas de requisições (`retryWhen`) e configurando timeout em requests (`timeout(Duration.ofMillis(<seconds>))`). 

## Usou/Criou:
- OpenFeing (@EnableFeignClients, @FeignClient)
- RestTemplate
- WebClient (`Mono` )

### Lembretes
- #### Não contém servidor de aplicação (like apache tomcat)
  ````yaml
  spring:
      main:
        web-application-type: none
  ````
  
### Dependencias
- OpenFeing:
  ````xml
  <dependencyManagement>
      <dependencies>
          <dependency>
              <groupId>org.springframework.cloud</groupId>
              <artifactId>spring-cloud-dependencies</artifactId>
              <version>${spring-cloud.version}</version>
              <type>pom</type>
              <scope>import</scope>
          </dependency>
      </dependencies>
  </dependencyManagement>
  <!-- ... -->
  <dependencies>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-openfeign</artifactId>
    </dependency>
  </dependencies>
  ````
- WebClient
  ````xml
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-webflux</artifactId>
  </dependency>
  ````