package com.eswar.tech.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.kafka.annotation.KafkaListener; 
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController; 
import com.eswar.tech.service.PaymentService; 
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/v1")
public class PaymentController {
	
	private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

	
	@Autowired
	private PaymentService paymentService;
	
	
	@KafkaListener(topics = "New-Order",groupId = "orders-group")
	public void processPayment(String event) throws JsonMappingException, JsonProcessingException {
		logger.info("Process payments :: "+event);
		paymentService.processPayment(event);		
	}
}
