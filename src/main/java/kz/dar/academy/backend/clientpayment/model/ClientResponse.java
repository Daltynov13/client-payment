package kz.dar.academy.backend.clientpayment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientResponse {

  private String clientId;
  private String name;
  private String surname;
  private String email;
}
