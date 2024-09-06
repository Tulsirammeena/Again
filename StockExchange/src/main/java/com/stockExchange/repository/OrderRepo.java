package com.stockExchange.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.stockExchange.dto.OrderStatus;
import com.stockExchange.entity.OrderEntity;

@Repository
public interface OrderRepo extends JpaRepository<OrderEntity, Integer> {

//	List<OrderEntity> findByTimeRange(LocalDateTime startOfDay, LocalDateTime endOfDay);

//	List<OrderEntity> findByTimeStampBetweenAndStatus(LocalDateTime startOfDay, LocalDateTime endOfDay,String status);

//	List<OrderEntity> findByCreatedAtBetweenAndStatus(LocalDateTime startOfDay, LocalDateTime endOfDay, String string);

	//List<OrderEntity> findByStockDetail_StockId(String string);

	List<OrderEntity> findByStockDetail_Symbol(String string);
	
//	List<OrderEntity> findByStockDetail_SymbolAndCreatedAtBetween(String string, LocalDateTime startOfDay, LocalDateTime endOfDay);

//	List<OrderEntity> findByStockDetail_SymbolAndStatusAndCreatedAtBetween(String string, LocalDateTime startOfDay,
//			LocalDateTime endOfDay, String string2);



	List<OrderEntity> findByStockDetail_SymbolAndCreatedAtBetweenAndStatus(String string, LocalDateTime startOfDay,
			LocalDateTime endOfDay, OrderStatus pending);
	List<OrderEntity> findByStatus(String status);

	List<OrderEntity> findByStockDetail_SymbolAndStatusAndCreatedAtBetweenOrderByCreatedAt(String stock, String string,
			LocalDateTime startOfDay, LocalDateTime endOfDay);

//	List<OrderEntity> findByCreatedAtBetweenAndStatusAndStockId(LocalDateTime startOfDay, LocalDateTime endOfDay,
//			String string, String name);	

}
