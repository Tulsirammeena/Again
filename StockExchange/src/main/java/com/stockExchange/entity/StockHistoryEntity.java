package com.stockExchange.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "stock_history")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class StockHistoryEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer historyId;
	
	@ManyToOne
    @JoinColumn(name = "symbol", referencedColumnName = "symbol",nullable = false)	
    private StockEntity stock;
	
	@Column(name = "date", nullable = false)
    private LocalDate date;
	
	@Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
	
	@Column(name = "volume", nullable = false)
    private Long volume;

//	public Integer getHistoryId() {
//		return historyId;
//	}
//
//	public StockEntity getStock() {
//		return stock;
//	}
//
//	public LocalDate getDate() {
//		return date;
//	}
//
//	public BigDecimal getPrice() {
//		return price;
//	}
//
//	public Long getVolume() {
//		return volume;
//	}
//
//	public void setHistoryId(Integer historyId) {
//		this.historyId = historyId;
//	}
//
//	public void setStock(StockEntity stock) {
//		this.stock = stock;
//	}
//
//	public void setDate(LocalDate date) {
//		this.date = date;
//	}
//
//	public void setPrice(BigDecimal price) {
//		this.price = price;
//	}
//
//	public void setVolume(Long volume) {
//		this.volume = volume;
//	}
//	
	
	
	
	
	

}
