package kz.dar.academy.backend.clientpayment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDetails {

  private String paymentId;
  private ClientResponse client;
  private Long amount;
}
