package com.stockExchange.daoImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.stockExchange.dao.HoldingService;
import com.stockExchange.dto.OrderType;
import com.stockExchange.dto.ResponseDto;
import com.stockExchange.dto.UserResponseModel;
import com.stockExchange.entity.HoldingEntity;
import com.stockExchange.entity.StockEntity;
import com.stockExchange.entity.StockHistoryEntity;
import com.stockExchange.entity.UserEntity;
import com.stockExchange.repository.HoldingRepo;
import com.stockExchange.repository.OrderRepo;
import com.stockExchange.repository.StockHistoryRepo;
import com.stockExchange.repository.StockRepo;
import com.stockExchange.repository.UserRepo;


@Service
public class HoldingServiceImplementation implements HoldingService {
	
	@Autowired
	private HoldingRepo holdingRepo;
	@Autowired
	private StockHistoryRepo stockHistoryRepo;
	@Autowired
	private StockRepo stockRepo;
	@Autowired
	private OrderRepo orderRepo;
	@Autowired
	private UserRepo userRepo;

	@Override
	public ResponseDto getUserHoldings( UserResponseModel user) {
		Set<HoldingEntity> userHoldings = holdingRepo.getHoldingsByUser_UserName(user.getUserName());
		if(userHoldings.isEmpty())
			return new ResponseDto(HttpStatus.OK, "no holdings", userHoldings);
		
		return new ResponseDto(HttpStatus.OK,"Holdings",userHoldings);
	}

//	@Override
//	public void saveStockData(String companyName, String symbol, String description, Long volume, String date,
//			BigDecimal stockPrice) {
//		StockEntity stock = new StockEntity(companyName, symbol, description, stockPrice);
//		stockRepo.save(stock);
//		StockHistoryEntity priceDetail = new StockHistoryEntity();
//	    priceDetail.setStock(stock);
//	    priceDetail.setPrice(stockPrice);
//	    priceDetail.setDate(LocalDate.now());
//	    priceDetail.setVolume(0l);
//	    
//	    stockHistoryRepo.save(priceDetail);
//	}

	@Override
	public ResponseDto placeOrder(String stockId, String userId,Integer quantity, String orderType) {
		StockEntity stock = stockRepo.getBySymbol(stockId);
		UserEntity user = userRepo.getByUserName(userId);
		OrderType type = orderType.equalsIgnoreCase("buy")?OrderType.BUY:OrderType.SELL; 
		return null;
		
	}
	
	
																																																																																																																																																																																																																																																																																																							
	

}
