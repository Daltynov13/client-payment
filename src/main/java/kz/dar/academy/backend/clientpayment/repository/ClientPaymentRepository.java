package kz.dar.academy.backend.clientpayment.repository;

import kz.dar.academy.backend.clientpayment.model.PaymentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientPaymentRepository extends ElasticsearchRepository<PaymentEntity, String> {

  PaymentEntity getPaymentEntityByClientId(String clientId);

  Page<PaymentEntity> getPaymentEntitiesByClientId(String clientId, Pageable pageable);

  void deletePaymentEntityByClientId(String clientId);

  PaymentEntity getPaymentEntityByPaymentId(String paymentId);

  void deletePaymentByPaymentId(String paymentId);
}
