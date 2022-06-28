package kz.dar.academy.backend.clientpayment.service;


import static org.modelmapper.convention.MatchingStrategies.STRICT;

import java.util.UUID;
import kz.dar.academy.backend.clientpayment.feign.ClientFeign;
import kz.dar.academy.backend.clientpayment.model.ClientResponse;
import kz.dar.academy.backend.clientpayment.model.PaymentDetails;
import kz.dar.academy.backend.clientpayment.model.PaymentEntity;
import kz.dar.academy.backend.clientpayment.model.PaymentRequest;
import kz.dar.academy.backend.clientpayment.model.PaymentResponse;
import kz.dar.academy.backend.clientpayment.repository.ClientPaymentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
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
  private ElasticsearchRestTemplate elasticsearchTemplate;

  @Override
  public PaymentResponse createPayment(PaymentRequest payment) {
    payment.setPaymentId(UUID.randomUUID().toString());
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
}
