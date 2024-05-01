package com.eswar.tech.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
 
import com.eswar.tech.entity.Delivery;
import com.eswar.tech.model.CustomerOrder;
import com.eswar.tech.model.DeliveryEvent;
import com.eswar.tech.repositoy.DeliveryRepository;
import com.eswar.tech.service.DeliveryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service("deliveryService")
public class DeliveryServiceImpl implements DeliveryService{
	
	private static final Logger logger = LoggerFactory.getLogger(DeliveryServiceImpl.class);


	@Autowired
	private DeliveryRepository deliveryRepository;

	@Autowired
	private KafkaTemplate<String, DeliveryEvent> kafkaTemplate;
	
	@Override
	public void deliverOrder(String event) throws JsonMappingException, JsonProcessingException {
		logger.info("DeliveryServiceImpl :: deliverOrder");
		Delivery shipment = new Delivery();
		DeliveryEvent inventoryEvent = new ObjectMapper().readValue(event, DeliveryEvent.class);
		CustomerOrder order = inventoryEvent.getCustomerOrder();

		try {
			if (order.getAddress() == null) {
				throw new Exception("Address not present");
			}

			shipment.setAddress(order.getAddress());
			shipment.setOrderId(order.getOrderId());

			shipment.setStatus("success");

			deliveryRepository.save(shipment);
		} catch (Exception e) {
			logger.error("DeliveryServiceImpl :: Exception Occurred while deliverOrder "+e.getMessage());
			shipment.setOrderId(order.getOrderId());
			shipment.setStatus("failed");
			deliveryRepository.save(shipment);

			logger.info("DeliveryServiceImpl :: Order Details :: "+order);

			DeliveryEvent reverseEvent = new DeliveryEvent();
			reverseEvent.setType("STOCK_REVERSED");
			reverseEvent.setCustomerOrder(order);
			kafkaTemplate.send("reversedStock", reverseEvent);
		}
	}

}
