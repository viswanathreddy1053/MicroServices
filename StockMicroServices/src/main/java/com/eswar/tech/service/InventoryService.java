package com.eswar.tech.service;

import org.springframework.web.bind.annotation.RequestBody;

import com.eswar.tech.model.Stock;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

public interface InventoryService {

	public void updateInventory(String paymentEvent) throws JsonMappingException, JsonProcessingException;
	public void addItems(@RequestBody Stock stock);
}
