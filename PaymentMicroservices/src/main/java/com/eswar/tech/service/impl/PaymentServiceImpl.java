package com.eswar.tech.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.eswar.tech.entity.Payment;
import com.eswar.tech.model.CustomerOrder;
import com.eswar.tech.model.OrderEvent;
import com.eswar.tech.model.PaymentEvent;
import com.eswar.tech.repository.PaymentRepository;
import com.eswar.tech.service.PaymentService;
import com.eswar.tech.utility.PaymentConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service("paymentService")
public class PaymentServiceImpl  implements PaymentService {

	private static final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

	@Autowired
	private PaymentRepository paymentRepository;
	
	@Autowired
	private KafkaTemplate<String, PaymentEvent> paymentKafkaTemplate;
	
	@Autowired
	private KafkaTemplate<String,OrderEvent> orderKafkaTemplate;
	
	@Override
	public void processPayment(String event) throws JsonMappingException, JsonProcessingException {
		
		logger.info("PaymentServiceImpl :: processPayment ");
		OrderEvent orderEvent = new ObjectMapper().readValue(event, OrderEvent.class);
		CustomerOrder customerOrder = orderEvent.getCustomerOrder();
		
		Payment payment = new Payment();
		payment.setMode(customerOrder.getPaymentMethod());
		payment.setOrderId(customerOrder.getOrderId());
		payment.setAmount(customerOrder.getAmount());
		payment.setStatus(PaymentConstants.SUCCESS);
		
		try {
			
			paymentRepository.save(payment);
			PaymentEvent paymentEvent = new PaymentEvent();
			paymentEvent.setType(PaymentConstants.PAYMENT_CREATED);
			paymentEvent.setCustomerOrder(customerOrder);
			paymentKafkaTemplate.send(PaymentConstants.NEW_PAYMENTS,paymentEvent);
			
		}catch (Exception e) {
			logger.error("Exception Occurred while processing Payment "+e.getMessage());
			payment.setStatus(PaymentConstants.PAYMENT_FAILED);
			payment.setOrderId(customerOrder.getOrderId());
			paymentRepository.save(payment);
			
			OrderEvent orderEventMs = new OrderEvent();
			orderEventMs.setCustomerOrder(customerOrder);
			orderEventMs.setType(PaymentConstants.ORDER_REVERSED);
			
			orderKafkaTemplate.send(PaymentConstants.ORDER_REVERSE,orderEventMs);
			
		}
	}

}
