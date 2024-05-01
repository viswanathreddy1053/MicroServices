package com.eswar.tech.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener; 
import org.springframework.stereotype.Controller;

import com.eswar.tech.service.DeliveryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Controller
public class DeliveryController {

	private static final Logger logger = LoggerFactory.getLogger(DeliveryController.class);

	@Autowired
	private DeliveryService deliveryService;
	
	@KafkaListener(topics = "New-Stock", groupId = "stock-group")
	public void deliverOrder(String event) throws JsonMappingException, JsonProcessingException {
		logger.info("DeliveryController :: deliverOrder");
		
		deliveryService.deliverOrder(event);
	}
}
