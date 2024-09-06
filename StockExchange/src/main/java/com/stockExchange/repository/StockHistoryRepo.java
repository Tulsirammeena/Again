package com.stockExchange.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stockExchange.entity.StockEntity;
import com.stockExchange.entity.StockHistoryEntity;

@Repository
public interface StockHistoryRepo extends JpaRepository<StockHistoryEntity, Integer> {

	List<StockHistoryEntity> findByDate(LocalDate minusDays);

	StockHistoryEntity findByStockAndDate(StockEntity stock, LocalDate tMinusOneDay);

	List<StockEntity> findStocksByDate(LocalDate now);

	Optional<StockHistoryEntity> findByStock_NameAndDate(String companyName, LocalDate now);

	StockHistoryEntity findByStock_SymbolAndDate(String stock, LocalDate now);

	

//	StockHistoryEntity findByStockEntity_SymbolAndDate(String stock, LocalDate now);

//	List<StockHistoryEntity> findDistinctSymbolsOrderedByLatestDate();

//	StockHistoryEntity findBySymbolAndDate(String symbol, LocalDate yesterday);

}
