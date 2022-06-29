package kz.dar.academy.backend.clientpayment.service;

import kz.dar.academy.backend.clientpayment.model.MailModel;

public interface MailService {

  void sendClientMail(MailModel message);

  void sendEmployeeMail(MailModel message);
}
