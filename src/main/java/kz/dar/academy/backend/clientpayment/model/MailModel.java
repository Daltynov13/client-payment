package kz.dar.academy.backend.clientpayment.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MailModel implements Serializable {
  String from;
  String to;
  String subject;
  String text;
}