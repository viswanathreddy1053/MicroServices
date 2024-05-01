package com.eswar.tech.repositoy;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eswar.tech.entity.Delivery;

public interface DeliveryRepository  extends JpaRepository<Delivery, Long>{
	

}
