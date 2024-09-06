package com.stockExchange.dao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stockExchange.entity.StockEntity;
import com.stockExchange.entity.StockHistoryEntity;
import com.stockExchange.repository.StockRepo;
import com.stockExchange.repository.StockHistoryRepo;

@Service
public class AdminService {
    
    @Autowired
    private StockRepo companyRepository;
    
    @Autowired
    private StockHistoryRepo stockRepository;

    public StockEntity findOrCreateCompany(String name, String symbol, String description) {
        StockEntity company = companyRepository.findBySymbol(symbol);
        if (company == null) {
            company = new StockEntity();
            company.setName(name);
            company.setDescription(description);
            company.setSymbol(symbol);
            companyRepository.save(company);
        }
        return company;
    }

    public void saveStockData(String companyName, String symbol, String description, Long volume, String date, BigDecimal stockPrice) {
        // Find or create the company
        StockEntity company = findOrCreateCompany(companyName, symbol, description);
        
        // Create and populate the StockEntity
        StockHistoryEntity stockEntity = new StockHistoryEntity();
        
        stockEntity.setStock(company);
        //stockEntity.setSymbol(symbol);
        stockEntity.setVolume(volume);
        stockEntity.setDate(LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        stockEntity.setPrice(stockPrice);
        
        // Save the StockEntity
        stockRepository.save(stockEntity);
    }

	
}
