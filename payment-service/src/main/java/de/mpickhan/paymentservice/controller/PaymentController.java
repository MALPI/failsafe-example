package de.mpickhan.paymentservice.controller;

import de.mpickhan.paymentservice.domain.PaymentResource;
import de.mpickhan.paymentservice.processing.PaymentProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by mpickhan on 20.07.17.
 */
@RestController
@Slf4j
@RequestMapping(value = "/payments")
public class PaymentController {

  private PaymentProcessor processor;

  public PaymentController(PaymentProcessor processor) {
    this.processor = processor;
  }

  @PostMapping
  public ResponseEntity createPayment(@RequestBody final PaymentResource paymentResource) {

    log.debug("Processing payment {}", paymentResource);
    boolean processingResult = processor.processPayment(paymentResource);

    if(processingResult) {
      return ResponseEntity.ok(paymentResource);
    } else {
      return ResponseEntity.badRequest().build();
    }

  }
}
