package com.eswar.tech.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.eswar.tech.entity.Order;
import com.eswar.tech.model.CustomerOrder;
import com.eswar.tech.model.OrderEvent;
import com.eswar.tech.repository.OrderRepository;
import com.eswar.tech.service.OrderService;
import com.eswar.tech.utility.OrderConstants;

@Service("orderService")
public class OrderServiceImpl implements OrderService {

	private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private KafkaTemplate<String, OrderEvent> kafkaTemplate;
	
	@Override
	public void createOrder(CustomerOrder customerOrder) {
		logger.info("OrderServiceImpl :: createOrder ");
		Order order = new Order();
		order.setItem(customerOrder.getItem());
		order.setQuantity(customerOrder.getQuantity());
		order.setAmount(customerOrder.getAmount());
		order.setStatus(OrderConstants.CREATED);
		
		try {
			order = orderRepository.save(order);
			customerOrder.setOrderId(order.getId());
			
			OrderEvent orderEvent = new OrderEvent();
			orderEvent.setCustomerOrder(customerOrder);
			orderEvent.setType(OrderConstants.ORDER_CREATED);
			
			kafkaTemplate.send(OrderConstants.NEW_ORDER,orderEvent);
			//return new ResponseEntity<String>("Order Created Successfully!..",HttpStatus.OK);
		}catch (Exception e) {
			logger.error("OrderServiceImpl :: Order Failed");
			order.setStatus(OrderConstants.ORDER_FAILED);
			//return new ResponseEntity<Order>(order,HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}	
}
