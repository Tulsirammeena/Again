package com.stockExchange.daoImpl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.stockExchange.dto.ResponseDto;
import com.stockExchange.dto.StockModel;
import com.stockExchange.entity.StockEntity;
import com.stockExchange.entity.StockHistoryEntity;
import com.stockExchange.repository.StockHistoryRepo;

@Service
public class StockListingService {

    @Autowired
    private StockHistoryRepo stockHistoryRepo;

    /**
     * Fetches all available stocks from the database and converts them to StockModel.
     *
     * @return List of StockModel
     */
    public ResponseDto getAvailableStocks() {
        // Fetch stocks from the repository
      //  List<StockEntity> stockEntities = stockRepository.findAll();
    	LocalDate today = LocalDate.now();
    	List<StockHistoryEntity> stockEntities = stockHistoryRepo.findByDate(today);
        
        List<StockModel> stocksList =  stockEntities.stream()
                .map(this::convertToModel)
                .collect(Collectors.toList());
        
       if(stocksList.isEmpty()) {
    	   return new ResponseDto(HttpStatus.OK, "Failed on fetching stock details",null,"No records found today");
       }
        
        return new ResponseDto(HttpStatus.OK,"Stock List is fetched",stocksList);
        
        
    }

    /**
     * Converts a StockEntity to a StockModel.
     *
     * @param stockEntity the StockEntity to convert
     * @return the converted StockModel
     */
    private StockModel convertToModel(StockHistoryEntity stockHistoryEntity) {
        StockModel stockModel = new StockModel();
        
        StockEntity stockDetail = stockHistoryEntity.getStock();
        stockModel.setName(stockDetail.getName());
        stockModel.setDescription(stockDetail.getDescription());
        stockModel.setPrice(stockHistoryEntity.getPrice());
        stockModel.setVolume(stockHistoryEntity.getVolume());
        stockModel.setSymbol(stockDetail.getSymbol());
        BigDecimal percentageChange = calculatePercentageChange(stockHistoryEntity);
        stockModel.setPercentageChange(percentageChange);
        return stockModel;
    }
    
    private BigDecimal calculatePercentageChange(StockHistoryEntity currentStock) {
        LocalDate tDay = currentStock.getDate();
        LocalDate tMinusOneDay = tDay.minusDays(1);

        // Find the stock price from the previous day
        StockHistoryEntity yesterdayStock = stockHistoryRepo.findByStockAndDate(
                currentStock.getStock(), tMinusOneDay);

        if (yesterdayStock != null) {
            BigDecimal yesterdayPrice = yesterdayStock.getPrice();
            BigDecimal todayPrice = currentStock.getPrice();

            // Calculate percentage change
            if (yesterdayPrice.compareTo(BigDecimal.ZERO) != 0) {
                return todayPrice.subtract(yesterdayPrice).divide(yesterdayPrice, 4, RoundingMode.HALF_UP) // Precision of 4 decimal places
                        .multiply(BigDecimal.valueOf(100)); //
                                    }
        }

        return BigDecimal.ZERO; // No change if no previous price is available
    }
    
    public LocalDate getPreviousDate(LocalDate date) {
    	LocalDate yesterday = date.minusDays(1);
    	return yesterday;
    }
}
