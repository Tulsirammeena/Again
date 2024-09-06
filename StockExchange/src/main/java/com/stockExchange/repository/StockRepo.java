package com.stockExchange.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.stockExchange.entity.StockEntity;
import com.stockExchange.entity.StockHistoryEntity;
import com.stockExchange.entity.UserEntity;

@Repository
public interface StockRepo extends JpaRepository<StockEntity,String>  {

	StockEntity getBySymbol(String stockId);
	StockEntity findBySymbol(String stockId);

	Optional<StockEntity> findByName(String companyName);

	
	
}
