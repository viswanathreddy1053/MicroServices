package com.eswar.tech.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.eswar.tech.entity.WareHouse;

public interface StockRepository  extends JpaRepository<WareHouse, Long>{

	List<WareHouse> findByItem(String item);
}
