package de.mpickhan.paymentservice.domain;

import lombok.Data;

import java.util.Currency;

/**
 * Created by mpickhan on 20.07.17.
 */
@Data
public class PaymentResource {

  private String id;

  private ConsumerResource consumerResource;

  private Double amount;

  private Currency currency;
}
