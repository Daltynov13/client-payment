package kz.dar.academy.backend.clientpayment.service;


import static kz.dar.academy.backend.clientpayment.model.PaymentState.OPEN;
import static kz.dar.academy.backend.clientpayment.model.PaymentState.PAID;
import static org.modelmapper.convention.MatchingStrategies.STRICT;

import java.util.Map.Entry;
import java.util.UUID;
import kz.dar.academy.backend.clientpayment.feign.ClientFeign;
import kz.dar.academy.backend.clientpayment.model.ClientResponse;
import kz.dar.academy.backend.clientpayment.model.MailModel;
import kz.dar.academy.backend.clientpayment.model.PaymentDetails;
import kz.dar.academy.backend.clientpayment.model.PaymentEntity;
import kz.dar.academy.backend.clientpayment.model.PaymentRequest;
import kz.dar.academy.backend.clientpayment.model.PaymentResponse;
import kz.dar.academy.backend.clientpayment.repository.ClientPaymentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ClientPaymentServiceImpl implements ClientPaymentService {

  static ModelMapper modelMapper = new ModelMapper();

  static {
    modelMapper.getConfiguration().setMatchingStrategy(STRICT);
  }

  @Autowired
  private ClientFeign clientFeign;
  @Autowired
  private ClientPaymentRepository clientPaymentRepository;
  @Autowired
  private MailService mailService;

  @Value("${employee.mail}")
  private String employeeSender;

  @Value("${sender}")
  private String mailSender;

  @Override
  public PaymentResponse createPayment(PaymentRequest payment) {
    payment.setPaymentId(UUID.randomUUID().toString());
    payment.setState(OPEN);
    PaymentEntity entity = modelMapper.map(payment, PaymentEntity.class);
    clientPaymentRepository.save(entity);
    return modelMapper.map(entity, PaymentResponse.class);
  }

  @Override
  public PaymentResponse getPaymentById(String paymentId) {
    PaymentEntity entity = clientPaymentRepository.getPaymentEntityByPaymentId(
        paymentId);
    return modelMapper.map(entity, PaymentResponse.class);
  }

  @Override
  public PaymentResponse updatePayment(PaymentRequest payment) {
    PaymentEntity entity = clientPaymentRepository.getPaymentEntityByPaymentId(
        payment.getPaymentId());

    entity.setClientId(payment.getClientId());
    entity.setAmount(payment.getAmount());
    PaymentEntity paymentEntity = clientPaymentRepository.save(entity);
    return modelMapper.map(paymentEntity, PaymentResponse.class);
  }

  @Override
  public void deletePaymentById(String paymentId) {
    clientPaymentRepository.deletePaymentByPaymentId(paymentId);
  }

  @Override
  public Page<PaymentResponse> getPaymentEntitiesByClientId(String clientId, Pageable pageable) {
    return clientPaymentRepository.getPaymentEntitiesByClientId(
        clientId, pageable).map(payment -> modelMapper.map(payment, PaymentResponse.class));
  }

  @Override
  public PaymentDetails getPaymentDetails(String paymentId) {

    PaymentDetails paymentDetails = new PaymentDetails();
    PaymentEntity paymentEntityByPaymentId = clientPaymentRepository.getPaymentEntityByPaymentId(
        paymentId);

    paymentDetails.setPaymentId(paymentEntityByPaymentId.getPaymentId());
    paymentDetails.setAmount(paymentEntityByPaymentId.getAmount());

    ClientResponse client = clientFeign.getClientById(paymentEntityByPaymentId.getClientId());
    paymentDetails.setClient(client);

    return paymentDetails;
  }

  @Override
  public void closePaymentById(String paymentId) {
    PaymentEntity entity = clientPaymentRepository.getPaymentEntityByPaymentId(
        paymentId);
    entity.setState(PAID);
    PaymentEntity paymentEntity = clientPaymentRepository.save(entity);
    ClientResponse client = clientFeign.getClientById(paymentEntity.getClientId());

    sendClientMessage(paymentEntity, client);

    sendEmployeeMessage(paymentEntity, client);
  }

  private void sendEmployeeMessage(PaymentEntity paymentEntity, ClientResponse client) {
    StringBuilder employeeText = getEmployeeText(paymentEntity, client);

    MailModel employeeMail = MailModel.builder()
        .from(mailSender)
        .to(employeeSender)
        .subject("Тема: Оплата квитанции №" + paymentEntity.getPaymentId())
        .text(employeeText.toString())
        .build();

    mailService.sendEmployeeMail(employeeMail);
  }

  private void sendClientMessage(PaymentEntity paymentEntity, ClientResponse client) {
    StringBuilder clientText = getClientText(paymentEntity, client);

    MailModel clientMail = MailModel.builder()
        .from(mailSender)
        .to(client.getEmail())
        .subject("Тема: Оплата квитанции №" + paymentEntity.getPaymentId())
        .text(clientText.toString())
        .build();
    mailService.sendClientMail(clientMail);
  }

  private StringBuilder getEmployeeText(PaymentEntity paymentEntity, ClientResponse client) {
    StringBuilder employeeText = new StringBuilder();
    employeeText.append("Клиент ")
        .append(client.getName())
        .append(" успешно оплатил квитанцию №")
        .append(paymentEntity.getPaymentId())
        .append(".")
        .append("Общая сумма оплаты составила ")
        .append(paymentEntity.getAmount())
        .append(" тенге.");
    return employeeText;
  }

  private StringBuilder getClientText(PaymentEntity paymentEntity, ClientResponse client) {
    StringBuilder sb = new StringBuilder();
    sb.append("Благодарим вас, ")
        .append(client.getName())
        .append(", за оплату коммунальных услуг с помощью нашего сервиса. \n")
        .append("Оплачены услуги: ");

    for (Entry<String, Double> data : paymentEntity.getPaymentList().entrySet()) {
      sb.append(data.getKey()).append(" - ").append(data.getValue()).append(" тенге\n");
    }
    sb.append("Всего: ").append(paymentEntity.getAmount()).append(" тенге");
    return sb;
  }
}
