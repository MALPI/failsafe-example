package de.mpickhan.paymentservice.processing;

import de.mpickhan.paymentservice.domain.PaymentResource;

/**
 * Created by mpickhan on 20.07.17.
 */
public class PaymentProcessor {

  private SolvencyClient solvencyClient;

  public PaymentProcessor(final SolvencyClient solvencyClient) {
    this.solvencyClient = solvencyClient;
  }

  public boolean processPayment(final PaymentResource resource) {

    /**
     * In a real world example here would probably happen other things as well,
     * like persisting the data or mapping the entity to internal model and so on.
     */
    return solvencyClient.checkSolvency(resource.getConsumerResource());
  }
}
