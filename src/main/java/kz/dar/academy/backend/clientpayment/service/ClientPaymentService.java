package kz.dar.academy.backend.clientpayment.service;

import kz.dar.academy.backend.clientpayment.model.PaymentDetails;
import kz.dar.academy.backend.clientpayment.model.PaymentRequest;
import kz.dar.academy.backend.clientpayment.model.PaymentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClientPaymentService {

  PaymentResponse createPayment(PaymentRequest payment);

  PaymentResponse getPaymentById(String paymentId);

  PaymentResponse updatePayment(PaymentRequest payment);

  void deletePaymentById(String paymentId);

  Page<PaymentResponse> getPaymentEntitiesByClientId(String clientId, Pageable pageable);

  PaymentDetails getPaymentDetails(String paymentId);
}
