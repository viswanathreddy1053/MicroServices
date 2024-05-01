package com.eswar.tech.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "`WareHouse`")
public class WareHouse {

	@Id
	@GeneratedValue
	private long id;

	@Column
	private int quantity;

	@Column
	private String item;

}
