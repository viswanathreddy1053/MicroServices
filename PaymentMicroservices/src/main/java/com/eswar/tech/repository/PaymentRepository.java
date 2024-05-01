package com.eswar.tech.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.eswar.tech.entity.Payment; 
 

public interface PaymentRepository extends JpaRepository<Payment, Long>{

	List<Payment> findByOrderId(long orderId);
}
