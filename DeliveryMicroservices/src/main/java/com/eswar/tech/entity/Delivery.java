package com.eswar.tech.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "`Delivery`")
public class Delivery {

	@Id
	@GeneratedValue
	private Long id;

	@Column
	private String address;

	@Column
	private String status;

	@Column
	private long orderId;

}
