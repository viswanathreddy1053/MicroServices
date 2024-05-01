package com.eswar.tech.commons;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.eswar.tech.entity.Order;
import com.eswar.tech.model.OrderEvent;
import com.eswar.tech.repository.OrderRepository;
import com.eswar.tech.utility.OrderConstants;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class OrderReverseEvent {

	private static final Logger logger = LoggerFactory.getLogger(OrderReverseEvent.class);
	
	@Autowired
	private OrderRepository orderRepository;
	
	@KafkaListener(topics = "OrdersReversed",groupId = "orders-group")
	public void reverseOrderDetails(String event) {
		logger.info("Calling Order Reverse Event :: "+event);
		try {
			OrderEvent orderEvent = new ObjectMapper().readValue(event, OrderEvent.class);
			Optional<Order> order =	orderRepository.findById(orderEvent.getCustomerOrder().getOrderId());
			order.ifPresent(ord -> {
				ord.setStatus(OrderConstants.ORDER_FAILED);
				orderRepository.save(ord);
			});
		}catch (Exception e) {
			logger.error("Exception Occurred in reverseOrderDetails " +e.getMessage());
		}
	}
}
