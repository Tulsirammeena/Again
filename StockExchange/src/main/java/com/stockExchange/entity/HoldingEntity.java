package com.stockExchange.entity;

import java.math.BigDecimal;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "holding")
public class HoldingEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer holdingId;
	private Integer noOfStocks;
	private BigDecimal averagePrice;
	private BigDecimal investedValue;
	@ManyToOne
	@JoinColumn(name = "stock_id", referencedColumnName ="symbol")
	private StockEntity stock;
	@ManyToOne(fetch = FetchType.EAGER,optional = false)
	@JoinColumn(name = "user_name",referencedColumnName = "userName")
	private UserEntity user;

}
