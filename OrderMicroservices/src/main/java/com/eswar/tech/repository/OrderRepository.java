package com.eswar.tech.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.eswar.tech.entity.Order;
 

public interface OrderRepository extends JpaRepository<Order, Long>{

}
