package kz.dar.academy.backend.clientpayment.model;

import java.util.HashMap;
import java.util.Map;
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
  private Map<String, Double> paymentList = new HashMap<>();
  private PaymentState state;
}
