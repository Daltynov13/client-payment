package kz.dar.academy.backend.clientpayment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {

  private String paymentId;
  private String clientId;
  private Long amount;
}
