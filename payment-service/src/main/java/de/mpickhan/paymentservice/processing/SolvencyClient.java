package de.mpickhan.paymentservice.processing;

import de.mpickhan.paymentservice.domain.ConsumerResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by mpickhan on 20.07.17.
 */
@Component
@Slf4j
public class SolvencyClient {

  private static final String BASE_URL = "localhost:8181/solvency";

  private final RestTemplate restTemplate;

  public SolvencyClient(final RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public boolean checkSolvency(final ConsumerResource consumerResource) throws URISyntaxException {
    log.debug("Running solvency check for {}", consumerResource.toString());
    ResponseEntity<Boolean> booleanResponseEntity = restTemplate.postForEntity(new URI(BASE_URL), consumerResource, Boolean.class);
    log.debug("Received response {}", booleanResponseEntity.toString());
    return booleanResponseEntity.getBody().booleanValue();
  }
}
