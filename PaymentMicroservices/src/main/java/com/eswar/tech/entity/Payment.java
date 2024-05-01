package com.eswar.tech.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "`Payment`")
public class Payment {

	@Id
	@GeneratedValue
	private long id;

	@Column
	private String mode;

	@Column
	private long orderId;

	@Column
	private double amount;

	@Column
	private String status;
}
