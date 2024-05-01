package com.eswar.tech.service;

import org.springframework.http.ResponseEntity;
import com.eswar.tech.model.CustomerOrder; 

public interface OrderService {

	public void createOrder(CustomerOrder customerOrder);
}
