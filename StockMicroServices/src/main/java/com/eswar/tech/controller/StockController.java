package com.eswar.tech.controller;
 

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener; 
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController; 
import com.eswar.tech.model.Stock; 
import com.eswar.tech.service.InventoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping("/api/v1")
public class StockController {

	private static final Logger logger = LoggerFactory.getLogger(StockController.class);

	@Autowired
	private InventoryService inventoryService;
	
	@KafkaListener(topics = "New-Payment",groupId = "payments-group")
	public void updateInventory(String paymentEvent) throws JsonMappingException, JsonProcessingException {
		logger.info("StockController :: updateInventory");		
		inventoryService.updateInventory(paymentEvent);
	}
	
	@PostMapping("/addItems")
	public void addItems(@RequestBody Stock stock) {
		logger.info("StockController :: addItems");
		inventoryService.addItems(stock);
	}
}
