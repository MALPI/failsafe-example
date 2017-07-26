package de.mpickhan.paymentservice.processing;

import de.mpickhan.paymentservice.domain.ConsumerResource;
import lombok.extern.slf4j.Slf4j;
import net.jodah.failsafe.CircuitBreaker;
import net.jodah.failsafe.Failsafe;
import net.jodah.failsafe.RetryPolicy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.zalando.failsafeactuator.service.FailsafeBreaker;

import javax.annotation.PostConstruct;
import java.io.InterruptedIOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by mpickhan on 20.07.17.
 */
@Component
@Slf4j
public class SolvencyClient {

  private static final String BASE_URL = "localhost:8181/solvency";

  private final RestTemplate restTemplate;

  private final CircuitBreaker circuitBreaker;

  private final RetryPolicy retryPolicy;

  public SolvencyClient(final RestTemplate restTemplate,
                        @FailsafeBreaker(value = "solvency-circuit") final CircuitBreaker circuitBreaker) {
    this.restTemplate = restTemplate;
    this.circuitBreaker = circuitBreaker;
    this.retryPolicy = new RetryPolicy();
  }

  @PostConstruct
  public void init() {
    configureCircuit();
    configureRetries();
  }

  private void configureRetries() {
    this.retryPolicy.retryOn(Arrays.asList(InterruptedIOException.class, ResourceAccessException.class))
     .withMaxRetries(3)
            .withBackoff(1,5, TimeUnit.SECONDS)
            .withJitter(10, TimeUnit.MILLISECONDS);

  }

  private void configureCircuit() {
    this.circuitBreaker
            .withFailureThreshold(3, 6)
            .withSuccessThreshold(3)
            .withDelay(10, TimeUnit.SECONDS);

    this.circuitBreaker.failOn(Arrays.asList(InterruptedIOException.class, ResourceAccessException.class));
  }

  public boolean checkSolvency(final ConsumerResource consumerResource) throws URISyntaxException {
    log.debug("Running solvency check for {}", consumerResource.toString());
    ResponseEntity<Boolean> booleanResponseEntity = Failsafe
            .with(this.circuitBreaker)
            .with(this.retryPolicy).withFallback(callAlternativeProvider())
            .get(() -> restTemplate.postForEntity(new URI(BASE_URL), consumerResource, Boolean.class));
    log.debug("Received response {}", booleanResponseEntity.toString());
    return booleanResponseEntity.getBody().booleanValue();
  }

  private ResponseEntity<Boolean> callAlternativeProvider() {
    return ResponseEntity.ok().body(true);
  }
}
