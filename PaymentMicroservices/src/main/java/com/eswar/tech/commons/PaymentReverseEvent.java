package com.eswar.tech.commons;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.eswar.tech.entity.Payment;
import com.eswar.tech.model.OrderEvent;
import com.eswar.tech.model.PaymentEvent;
import com.eswar.tech.repository.PaymentRepository;
import com.eswar.tech.utility.PaymentConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
 

@Component
public class PaymentReverseEvent {

	private static final Logger logger = LoggerFactory.getLogger(PaymentReverseEvent.class);
	
	@Autowired
	private PaymentRepository paymentRepository;
	
	@Autowired
	private KafkaTemplate<String,OrderEvent> kafkaTemplate;
	
	@KafkaListener(topics = "reversedPayments", groupId = "payments-group")
	public void reversePaymentDetails(String event) {
		logger.info("Payment Reverse Event :: "+event);
		try {
			PaymentEvent paymentEvent = new ObjectMapper().readValue(event, PaymentEvent.class); 
			List<Payment> paymentList = paymentRepository.findByOrderId(paymentEvent.getCustomerOrder().getOrderId());
			paymentList.forEach(payment ->{
				payment.setStatus(PaymentConstants.PAYMENT_FAILED);
				paymentRepository.save(payment);
			});
			
			OrderEvent orderEvent = new OrderEvent();
			orderEvent.setCustomerOrder(paymentEvent.getCustomerOrder());
			orderEvent.setType(PaymentConstants.ORDER_REVERSED);
			kafkaTemplate.send(PaymentConstants.ORDER_REVERSE,orderEvent);
		}catch (Exception e) {
			logger.error("Exception Occurred in reverseOrderDetails " +e.getMessage());
		}
	}
}
