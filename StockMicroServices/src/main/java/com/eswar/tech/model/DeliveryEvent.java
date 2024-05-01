package com.eswar.tech.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DeliveryEvent {
	
	private CustomerOrder customerOrder;
	private String type;
	
}
