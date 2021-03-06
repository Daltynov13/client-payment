package kz.dar.academy.backend.clientpayment.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "payment")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentEntity implements Serializable {

  @Id
  private String paymentId;

  @Field(type = FieldType.Auto)
  private String clientId;

  @Field(type = FieldType.Auto)
  private Long amount;

  @Field(type = FieldType.Auto)
  private Map<String, Double> paymentList = new HashMap<>();

  @Field(type = FieldType.Auto)
  private PaymentState state;
}
