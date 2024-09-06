package com.stockExchange.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class StockModel {
	
	private String symbol;
    private String name;
    private String description;
    private BigDecimal price;
    private Long volume;
    private BigDecimal percentageChange;

}
