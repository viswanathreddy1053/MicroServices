package com.eswar.tech.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

public interface DeliveryService {
	
	public void deliverOrder(String event) throws JsonMappingException, JsonProcessingException;
	
}
