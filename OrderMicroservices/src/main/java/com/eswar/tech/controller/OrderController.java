package com.eswar.tech.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.eswar.tech.model.CustomerOrder; 
import com.eswar.tech.service.OrderService;

@RestController
@RequestMapping("/api/v1")
public class OrderController {

	private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
	
	@Autowired
	private OrderService orderService;
	
	@PostMapping("/createOrders")
	public void createOrder(@RequestBody CustomerOrder customerOrder) {
		logger.info("OrderController :: createOrder");
		orderService.createOrder(customerOrder);
	}
}
