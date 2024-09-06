package com.stockExchange.dao;

import java.math.BigDecimal;

import com.stockExchange.dto.ResponseDto;
import com.stockExchange.dto.UserResponseModel;

public interface HoldingService {

	public ResponseDto getUserHoldings(UserResponseModel user);

//	public void saveStockData(String companyName, String symbol, String description, Long volume, String date,
//			BigDecimal stockPrice);

	public ResponseDto placeOrder(String stockId, String userId,Integer quantity, String orderType);

//	ResponseDto addStocks(StockModel stock);
	
	
}
