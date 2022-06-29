package kz.dar.academy.backend.clientpayment.service;

import kz.dar.academy.backend.clientpayment.model.MailModel;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {

  @Autowired
  private KafkaTemplate<String, MailModel> kafkaTemplate;

  @Override
  @SneakyThrows
  public void sendClientMail(MailModel message) {
    kafkaTemplate.send("clients", message);
  }

  @Override
  @SneakyThrows
  public void sendEmployeeMail(MailModel message) {
    kafkaTemplate.send("employees", message);
  }
}
