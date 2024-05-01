package com.eswar.tech.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

public interface PaymentService {

	public void processPayment(String event) throws JsonMappingException, JsonProcessingException;
}
