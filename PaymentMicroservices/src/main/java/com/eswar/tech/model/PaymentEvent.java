package com.eswar.tech.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PaymentEvent {

	private CustomerOrder customerOrder;
	private String type;
	
}
