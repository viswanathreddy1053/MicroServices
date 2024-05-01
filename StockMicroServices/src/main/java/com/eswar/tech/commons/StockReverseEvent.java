package com.eswar.tech.commons;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RestController;

import com.eswar.tech.entity.WareHouse;
import com.eswar.tech.model.DeliveryEvent;
import com.eswar.tech.model.PaymentEvent;
import com.eswar.tech.repository.StockRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class StockReverseEvent {

	@Autowired
	private StockRepository repository;

	@Autowired
	private KafkaTemplate<String, PaymentEvent> kafkaTemplate;

	@KafkaListener(topics = "reversedStock", groupId = "stock-group")
	public void reverseStock(String event) {
		System.out.println("Inside reverse stock for order "+event);
		
		try {
			DeliveryEvent deliveryEvent = new ObjectMapper().readValue(event, DeliveryEvent.class);

			Iterable<WareHouse> inv = this.repository.findByItem(deliveryEvent.getCustomerOrder().getItem());

			inv.forEach(i -> {
				i.setQuantity(i.getQuantity() + deliveryEvent.getCustomerOrder().getQuantity());
				repository.save(i);
			});

			PaymentEvent paymentEvent = new PaymentEvent();
			paymentEvent.setCustomerOrder(deliveryEvent.getCustomerOrder());
			paymentEvent.setType("PAYMENT_REVERSED");
			kafkaTemplate.send("reversed-payments", paymentEvent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}