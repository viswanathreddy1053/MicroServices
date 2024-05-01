package com.eswar.tech.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.eswar.tech.entity.WareHouse;
import com.eswar.tech.model.CustomerOrder;
import com.eswar.tech.model.DeliveryEvent;
import com.eswar.tech.model.PaymentEvent;
import com.eswar.tech.model.Stock;
import com.eswar.tech.repository.StockRepository;
import com.eswar.tech.service.InventoryService;
import com.eswar.tech.utility.StockConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service("inventoryService")
public class InventoryServiceImpl implements InventoryService{
	
	private static final Logger logger = LoggerFactory.getLogger(InventoryServiceImpl.class);

	
	@Autowired
	private StockRepository stockRepository;
	
	@Autowired
	private KafkaTemplate<String,DeliveryEvent> kafkaTemplate;
	
	@Autowired
	private KafkaTemplate<String,PaymentEvent> paymentKafkaTemplate;
	
	@Override
	public void updateInventory(String paymentEvent) throws JsonMappingException, JsonProcessingException {
		logger.info("InventoryServiceImpl :: updateInventory");
		DeliveryEvent deliveryEvent = new DeliveryEvent();
		PaymentEvent  payment = new ObjectMapper().readValue(paymentEvent, PaymentEvent.class);
		CustomerOrder order = payment.getCustomerOrder();
		
		try {
			List<WareHouse> inventories =  stockRepository.findByItem(order.getItem());
			
			if(inventories.isEmpty()) {
				System.out.println("Stock not exist and reverting the order");
				throw new Exception("Stock not available");
			}
			inventories.forEach(i -> {
				i.setQuantity(i.getQuantity() - order.getQuantity());

				stockRepository.save(i);
			});

			deliveryEvent.setType(StockConstants.STOCKUPDATED);
			deliveryEvent.setCustomerOrder(payment.getCustomerOrder());
			kafkaTemplate.send(StockConstants.NEWSTOCK, deliveryEvent);
			
		}catch (Exception e) {
			logger.error("InventoryServiceImpl :: Exception Occurred while updating Inventory "+e.getMessage());
			PaymentEvent pe = new PaymentEvent();
			pe.setCustomerOrder(order);
			pe.setType("PAYMENT_REVERSED");
			paymentKafkaTemplate.send("reversedPayments", pe);
		}
	}

	@Override
	public void addItems(Stock stock) {
		logger.info("InventoryServiceImpl :: addItems");

		Iterable<WareHouse> items = stockRepository.findByItem(stock.getItem());

		if (items.iterator().hasNext()) {
			items.forEach(i -> {
				i.setQuantity(stock.getQuantity() + i.getQuantity());
				stockRepository.save(i);
			});
		} else {
			WareHouse i = new WareHouse();
			i.setItem(stock.getItem());
			i.setQuantity(stock.getQuantity());
			stockRepository.save(i);
		}
	}

}
