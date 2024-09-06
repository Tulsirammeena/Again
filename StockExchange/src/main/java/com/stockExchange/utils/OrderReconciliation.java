package com.stockExchange.utils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.stockExchange.entity.HoldingEntity;
import com.stockExchange.entity.OrderEntity;
import com.stockExchange.entity.StockEntity;
import com.stockExchange.entity.StockHistoryEntity;
import com.stockExchange.repository.HoldingRepo;
import com.stockExchange.repository.OrderRepo;
import com.stockExchange.repository.StockHistoryRepo;
import com.stockExchange.repository.StockRepo;

@Component
public class OrderReconciliation {

	@Autowired
	private HoldingRepo holdingRepo;

	@Autowired
	private OrderRepo orderRepo;

	@Autowired
	private StockHistoryRepo stockHistoryRepo;

	@Autowired
	private StockRepo stockRepo;

	@Scheduled(cron = "05 09 14 * * *")
	public void makeOrderRecon() {
		LocalDate date = LocalDate.now();
		LocalDateTime startOfDay = date.atTime(LocalTime.of(9, 00));
		LocalDateTime endOfDay = date.atTime(LocalTime.of(19, 30));
	
		List<StockEntity> stocks = stockRepo.findAll();
		List<String> symbols = stocks.stream().map(StockEntity::getSymbol).collect(Collectors.toList());
	for(String stock : symbols) {	
		StockHistoryEntity stockInfo = stockHistoryRepo.findByStock_SymbolAndDate(stock, LocalDate.now());
		List<OrderEntity> orders = orderRepo.findByStockDetail_SymbolAndStatusAndCreatedAtBetweenOrderByCreatedAt(stock,"PENDING",startOfDay,endOfDay);
		if(orders == null)
			continue;
		List<OrderEntity> buyList = addToBuyList(orders);
		List<OrderEntity> sellList = addToSellList(orders);//orders.stream().filter(x-> (OrderType.SELL).equals(x.getOrderType())).collect(Collectors.toList());
		
		Integer buyListCount = buyList.stream().mapToInt(OrderEntity::getQuantity).sum();
		Integer sellListCount = sellList.stream().mapToInt(OrderEntity::getQuantity).sum();
		
		
		if(buyListCount == sellListCount) {
			for(OrderEntity buyOrder : buyList) {
				Optional<HoldingEntity> holding = holdingRepo.findByUser_UserNameAndStock_Symbol(buyOrder.getUser().getUserName(),buyOrder.getStockDetail().getName());
				if(holding.isPresent()) {
					holding.get().setNoOfStocks(holding.get().getNoOfStocks()+buyOrder.getQuantity());
					holding.get().setAveragePrice(calculateAveragePrice(holding.get().getNoOfStocks(),
							holding.get().getAveragePrice(),stockInfo.getPrice().multiply(BigDecimal.valueOf(buyOrder.getQuantity()))));
					holding.get().setInvestedValue(holding.get().getAveragePrice().multiply(BigDecimal.valueOf(buyOrder.getQuantity())));
					holdingRepo.save(holding.get());
					buyOrder.setStatus("COMPLETED");
					orderRepo.save(buyOrder);
				} else {
					HoldingEntity newHolding = new HoldingEntity();
             
                    newHolding.setUser(buyOrder.getUser());
                    newHolding.setNoOfStocks(buyOrder.getQuantity());
                    newHolding.setInvestedValue(stockInfo.getPrice().multiply(BigDecimal.valueOf(buyOrder.getQuantity())));
                    newHolding.setAveragePrice(newHolding.getInvestedValue().divide(BigDecimal.valueOf(newHolding.getNoOfStocks())));
                    newHolding = holdingRepo.save(newHolding);
                    newHolding.setStock(buyOrder.getStockDetail());
                    holdingRepo.save(newHolding);
                    buyOrder.setStatus("COMPLETED");
					orderRepo.save(buyOrder);
				} 
			}
				
				for(OrderEntity sellOrder : sellList) {
					Optional<HoldingEntity> holding = holdingRepo.findByUser_UserNameAndStock_Symbol(sellOrder.getUser().getUserName(),sellOrder.getStockDetail().getName());
					if(holding.isPresent()) {
						holding.get().setNoOfStocks(holding.get().getNoOfStocks()-sellOrder.getQuantity());
						holding.get().setInvestedValue(holding.get().getAveragePrice().multiply(BigDecimal.valueOf(holding.get().getNoOfStocks())));
						holdingRepo.save(holding.get());	
					} 
					sellOrder.setStatus("COMPLETED");
					orderRepo.save(sellOrder);
				}
		}else if(buyListCount<sellListCount) {
			for(OrderEntity buyOrder : buyList) {
				Optional<HoldingEntity> holding = holdingRepo.findByUser_UserNameAndStock_Symbol(buyOrder.getUser().getUserName(),buyOrder.getStockDetail().getName());
				if(holding.isPresent()) {
					holding.get().setNoOfStocks(holding.get().getNoOfStocks()+buyOrder.getQuantity());
					holding.get().setAveragePrice(calculateAveragePrice(holding.get().getNoOfStocks(),
							holding.get().getAveragePrice(),stockInfo.getPrice().multiply(BigDecimal.valueOf(buyOrder.getQuantity()))));
					holding.get().setInvestedValue(holding.get().getAveragePrice().multiply(BigDecimal.valueOf(buyOrder.getQuantity())));
					holdingRepo.save(holding.get());
					buyOrder.setStatus("COMPLETED");
					orderRepo.save(buyOrder);
				} else {
					HoldingEntity newHolding = new HoldingEntity();
                    newHolding.setUser(buyOrder.getUser());
                    newHolding.setNoOfStocks(buyOrder.getQuantity());
                    newHolding.setInvestedValue(stockInfo.getPrice().multiply(BigDecimal.valueOf(buyOrder.getQuantity())));
                    newHolding = holdingRepo.save(newHolding);
                    newHolding.setStock(buyOrder.getStockDetail());
                    holdingRepo.save(newHolding);
                    buyOrder.setStatus("COMPLETED");
					orderRepo.save(buyOrder);
				} 
				
			}
			  
			int totalSuccessfulSell = 0;
			for (OrderEntity sellOrder : sellList) {
				totalSuccessfulSell += sellOrder.getQuantity();

				Optional<HoldingEntity> holding = holdingRepo.findByUser_UserNameAndStock_Symbol(sellOrder.getUser().getUserName(),sellOrder.getStockDetail().getName());

				if (totalSuccessfulSell < buyListCount) {
					holding.get().setNoOfStocks(holding.get().getNoOfStocks()-sellOrder.getQuantity());
					holding.get().setInvestedValue(holding.get().getAveragePrice().multiply(BigDecimal.valueOf(holding.get().getNoOfStocks())));
					holdingRepo.save(holding.get());

					sellOrder.setStatus("COMPLETED");
					orderRepo.save(sellOrder);
				} else {
					 int difference = totalSuccessfulSell - buyListCount;
					 
					 if (buyListCount == (totalSuccessfulSell - difference)) {
						 
	                        sellOrder.setQuantity(sellOrder.getQuantity() - difference);
	                        sellOrder.setStatus("COMPLETED");
	                  if(holding.isPresent()) {
	                        holding.get().setNoOfStocks(holding.get().getNoOfStocks() - sellOrder.getQuantity());
	                        holding.get().setInvestedValue(holding.get().getAveragePrice().multiply(BigDecimal.valueOf(holding.get().getNoOfStocks())));
	    				
	                        holdingRepo.save(holding.get());
	                  }
	                        orderRepo.save(sellOrder);

	                        OrderEntity cancelOrder = new OrderEntity();

	                        cancelOrder.setUser(sellOrder.getUser());
	                        cancelOrder.setQuantity(difference);
	                        cancelOrder.setStatus("REJECTED");
	                        cancelOrder.setOrderType(sellOrder.getOrderType());
	                        cancelOrder.setPrice(stockInfo.getPrice().multiply(BigDecimal.valueOf(sellOrder.getQuantity())));
	                        cancelOrder = orderRepo.save(cancelOrder);
	                        cancelOrder.setStockDetail(sellOrder.getStockDetail());
	                        orderRepo.save(cancelOrder);
	                        break;
	                     
	                    }
				}
			} 
			
			for(OrderEntity order : sellList) {
				if("PENDING".equalsIgnoreCase(order.getStatus())) {
					order.setStatus("REJECTED");
					orderRepo.save(order);
				}
			}
	
			/**
			 *  Scenario: if buy record is more than sell record
			 */
			
		} else if(sellListCount<buyListCount) {
			for(OrderEntity sellOrder : sellList) {
				Optional<HoldingEntity> holding = holdingRepo.findByUser_UserNameAndStock_Symbol(sellOrder.getUser().getUserName(),sellOrder.getStockDetail().getName());
				if(holding.isPresent()) {
					holding.get().setNoOfStocks(holding.get().getNoOfStocks()-sellOrder.getQuantity());
					holding.get().setInvestedValue(holding.get().getAveragePrice().multiply(BigDecimal.valueOf(holding.get().getNoOfStocks())));
					holdingRepo.save(holding.get());
					sellOrder.setStatus("COMPLETED");
					//orderRepo.save(sellOrder); //needed
				} 
				sellOrder.setStatus("COMPLETED");
				orderRepo.save(sellOrder);
			}
			
			 int totalSuccessfulBuy = 0;
	            for (OrderEntity buyOrder : buyList) {
	            	totalSuccessfulBuy += buyOrder.getQuantity();
	                Optional<HoldingEntity> holding = holdingRepo.findByUser_UserNameAndStock_Symbol(buyOrder.getUser().getUserName(),buyOrder.getStockDetail().getName());

	                if (totalSuccessfulBuy < sellListCount) {
	                    if (holding.isPresent()) {
	                    	
	                    	holding.get().setNoOfStocks(holding.get().getNoOfStocks()+buyOrder.getQuantity());
	                    	holding.get().setAveragePrice(calculateAveragePrice(buyOrder.getQuantity(),holding.get().getAveragePrice() , stockInfo.getPrice().multiply(BigDecimal.valueOf(buyOrder.getQuantity()))));
	                        holding.get().setInvestedValue(holding.get().getAveragePrice().multiply(BigDecimal.valueOf(buyOrder.getQuantity())));
	                        holdingRepo.save(holding.get());

	                        buyOrder.setStatus("COMPLETED");
	                        orderRepo.save(buyOrder);
	                    } else {
	                    	HoldingEntity newHolding = new HoldingEntity();
	                   
	                        newHolding.setUser(buyOrder.getUser());
	                        newHolding.setNoOfStocks(buyOrder.getQuantity());
	                        newHolding.setAveragePrice(stockInfo.getPrice().divide(BigDecimal.valueOf(buyOrder.getQuantity())));
	                        newHolding.setInvestedValue(stockInfo.getPrice().multiply(BigDecimal.valueOf(buyOrder.getQuantity())));
	                        newHolding = holdingRepo.save(newHolding);
	                        newHolding.setStock(buyOrder.getStockDetail());
	                        holdingRepo.save(newHolding);
	                        buyOrder.setStatus("COMPLETED");
	    					orderRepo.save(buyOrder);
	                    }
	                
	                } else {
	                	
	                	int difference = totalSuccessfulBuy - sellListCount;

	                    if (sellListCount == (totalSuccessfulBuy - difference)) {
	                        buyOrder.setQuantity(buyOrder.getQuantity() - difference);
	                        buyOrder.setStatus("COMPLETED");

	                        if (holding.isPresent()) {
	                        	holding.get().setNoOfStocks(holding.get().getNoOfStocks()+buyOrder.getQuantity());
		                    	holding.get().setAveragePrice(calculateAveragePrice(buyOrder.getQuantity(),holding.get().getAveragePrice() , stockInfo.getPrice().multiply(BigDecimal.valueOf(buyOrder.getQuantity()))));
		                        holding.get().setInvestedValue(holding.get().getAveragePrice().multiply(BigDecimal.valueOf(buyOrder.getQuantity())));
		                        holdingRepo.save(holding.get());

		                        buyOrder.setStatus("COMPLETED");
		                        orderRepo.save(buyOrder);

		                        OrderEntity cancelOrder = new OrderEntity();
		                        
		                        
		                        cancelOrder.setUser(buyOrder.getUser());
		                        cancelOrder.setQuantity(difference);
		                        cancelOrder.setStatus("REJECTED");
		                        cancelOrder = orderRepo.save(cancelOrder);
		                        cancelOrder.setStockDetail(buyOrder.getStockDetail());
		                        orderRepo.save(cancelOrder);
		                        break;
	                        } else {
	                          
	                            HoldingEntity newHolding = new HoldingEntity();
		                        
		                        newHolding.setUser(buyOrder.getUser());
		                        newHolding.setNoOfStocks(buyOrder.getQuantity());
		                        newHolding.setAveragePrice(stockInfo.getPrice().divide(BigDecimal.valueOf(buyOrder.getQuantity())));
		                        newHolding.setInvestedValue(stockInfo.getPrice().multiply(BigDecimal.valueOf(buyOrder.getQuantity())));
		                        newHolding = holdingRepo.save(newHolding);
		                        newHolding.setStock(buyOrder.getStockDetail());
		                        holdingRepo.save(newHolding);

		                        buyOrder.setStatus("COMPLETED");
		                        orderRepo.save(buyOrder);

	                            OrderEntity cancelOrder = new OrderEntity();

	                            cancelOrder.setUser(buyOrder.getUser());
		                        cancelOrder.setQuantity(difference);
		                        cancelOrder.setStatus("REJECTED");
		                        cancelOrder.setOrderType(buyOrder.getOrderType());
		                        cancelOrder.setPrice(stockInfo.getPrice().multiply(BigDecimal.valueOf(cancelOrder.getQuantity())));
		                        cancelOrder = orderRepo.save(cancelOrder);
		                        cancelOrder.setStockDetail(buyOrder.getStockDetail());
		                        orderRepo.save(cancelOrder);

	                            orderRepo.save(cancelOrder);
	                            break;
	                        }
	                    }
	                   	
						 }
	
	                	                	
	                }					//successful buy transaction ends here
	            
	            for(OrderEntity order : buyList) {
					if("PENDING".equals(order.getStatus())) {
						order.setStatus("REJECTED");
						orderRepo.save(order);
					}
				}
	            
	            
	             }
			
			
			
		}
		
		
//		for(StockHistoryEntity s : stocks) {
//			
//			StockEntity stockDetail = s.getStock();
//			
////			List<OrderEntity> stockOrders = orderRepo.findByCreatedAtBetweenAndStatusAndStockId(startOfDay, endOfDay,"pending",s.getStock().getName());
//			
//			
//			
//			
//		}
		
		
		
		
	}

	private List<OrderEntity> addToBuyList(List<OrderEntity> orders) {
		List<OrderEntity> buyList = new ArrayList<>();
		for(OrderEntity order: orders) {
			if("BUY".equals(order.getOrderType())) 
				buyList.add(order);
			
		}
		
		return buyList;
	}
	
	private List<OrderEntity> addToSellList(List<OrderEntity> orders) {
		List<OrderEntity> sellList = new ArrayList<>();
		for(OrderEntity order: orders) {
			if("SELL".equals(order.getOrderType())) 
				sellList.add(order);
			
		}
		return sellList;
	}

	private BigDecimal calculateAveragePrice(int noOfStocks, BigDecimal averagePrice, BigDecimal price) {

		BigDecimal sum = averagePrice.add(price);
		BigDecimal newAvg = sum.divide(BigDecimal.valueOf(noOfStocks));

		return newAvg;
	}

}
