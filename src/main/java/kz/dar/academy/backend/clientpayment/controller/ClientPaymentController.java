package kz.dar.academy.backend.clientpayment.controller;

import static org.springframework.http.HttpStatus.OK;

import javax.validation.Valid;
import kz.dar.academy.backend.clientpayment.model.PaymentDetails;
import kz.dar.academy.backend.clientpayment.model.PaymentRequest;
import kz.dar.academy.backend.clientpayment.model.PaymentResponse;
import kz.dar.academy.backend.clientpayment.service.ClientPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("client-payment")
public class ClientPaymentController {

  @Autowired
  private Environment env;
  @Autowired
  private ClientPaymentService clientPaymentService;

  @GetMapping("/check")
  public String checkClientCoreApi() {
    return "Client service is WORKING at " + env.getProperty("local.server.port");
  }

  @PostMapping
  public ResponseEntity<PaymentResponse> createPayment(@Valid @RequestBody PaymentRequest client) {
    PaymentResponse payment = clientPaymentService.createPayment(client);
    return new ResponseEntity<>(payment, OK);
  }

  @GetMapping("/details/{paymentId}")
  public PaymentDetails getPaymentDetails(@PathVariable String paymentId) {

    return clientPaymentService.getPaymentDetails(paymentId);
  }

  @GetMapping("/{paymentId}")
  public PaymentResponse getPaymentById(@PathVariable String paymentId) {
    return clientPaymentService.getPaymentById(paymentId);
  }

  @PutMapping()
  public ResponseEntity<PaymentResponse> updatePaymentById(@RequestParam String paymentId,
      @Valid @RequestBody PaymentRequest payment) {
    payment.setPaymentId(paymentId);
    PaymentResponse paymentResponse = clientPaymentService.updatePayment(payment);
    return new ResponseEntity<>(paymentResponse, OK);

  }

  @DeleteMapping("/{paymentId}")
  public ResponseEntity<String> deletePaymentById(@PathVariable String paymentId) {
    clientPaymentService.deletePaymentById(paymentId);
    return new ResponseEntity<>("Successful deleted", OK);
  }

  @GetMapping("/client/{clientId}")
  public Page<PaymentResponse> getPaymentsByClientIdAndPage(@PathVariable String clientId,
      @RequestParam int size, @RequestParam int page) {

    Pageable pageable = PageRequest.of(page, size);
    return clientPaymentService.getPaymentEntitiesByClientId(
        clientId, pageable);
  }

  @PutMapping("/{paymentId}/pay")
  public ResponseEntity<String> closePaymentById(@PathVariable String paymentId) {
    clientPaymentService.closePaymentById(paymentId);
    return new ResponseEntity<>("Successful paid", OK);
  }

}
