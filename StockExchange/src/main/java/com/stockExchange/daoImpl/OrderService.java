package com.stockExchange.daoImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stockExchange.dto.OrderModel;
import com.stockExchange.dto.OrderStatus;
import com.stockExchange.dto.OrderType;
import com.stockExchange.entity.HoldingEntity;
import com.stockExchange.entity.OrderEntity;
import com.stockExchange.entity.StockEntity;
import com.stockExchange.entity.StockHistoryEntity;
import com.stockExchange.entity.UserEntity;
import com.stockExchange.repository.HoldingRepo;
import com.stockExchange.repository.OrderRepo;
import com.stockExchange.repository.StockHistoryRepo;
import com.stockExchange.repository.StockRepo;
import com.stockExchange.repository.UserRepo;

@Service
public class OrderService {

    @Autowired
    private OrderRepo orderRepository;


    @Autowired
    private StockRepo stockRepo;

    @Autowired
    private HoldingRepo holdingRepo;
 //   @Autowired
//    private CompanyRepository companyRepository; // Assuming you have this to get company names
    
    @Autowired
    private UserRepo userRepo;
    
    @Autowired
    private StockHistoryRepo stockHistoryRepo;

    public List<OrderEntity> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<OrderModel> getOrderHistory(String userName, LocalDateTime beginDate, LocalDateTime endDate) {
        List<OrderEntity> orders = orderRepository.findAll();

        if (userName != null) {
            orders = orders.stream()
                .filter(order -> order.getUser().getUserName().equals(userName))
                .collect(Collectors.toList());
        }

        if (beginDate != null && endDate != null) {
            orders = orders.stream()
                .filter(order -> !order.getCreatedAt().isBefore(beginDate) && !order.getCreatedAt().isAfter(endDate))
                .collect(Collectors.toList());
        }

        return orders.stream().map(order -> {
            OrderModel dto = new OrderModel();
            dto.setTransactionId(order.getOrderId());
            dto.setTransactionType(order.getOrderType());
//            dto.setCompanyName(getCompanyNameById(order.getCompanyId()));
            dto.setDate(order.getCreatedAt());
            dto.setCompanyName(order.getStockDetail().getName());
            dto.setSharePrice(order.getPrice());
            dto.setQuantity(order.getQuantity());
            dto.setTotalPrice(order.getPrice().multiply(BigDecimal.valueOf(order.getQuantity())));
            dto.setStatus(order.getStatus());
            return dto;
        }).collect(Collectors.toList());
    }
    
    public void createOrder(OrderModel orderModel, String userName) throws Exception {
        // Fetch stock details by symbol
    	
    	System.out.println("Company Name: " + orderModel.getCompanyName());
    	System.out.println("Creating order: " + userName);
    	
        StockEntity stock = stockRepo.findByName(orderModel.getCompanyName())
                .orElseThrow(() -> new Exception("Stock not found"));
        
        Optional<StockHistoryEntity> stockHistory = stockHistoryRepo.findByStock_NameAndDate(orderModel.getCompanyName(), LocalDate.now());
        		//stockHistoryRepo.findByStock_NameAndDate(orderModel.getCompanyName(),LocalDate.now()).orElseThrow(() -> new Exception("Stock not found"));
        
     //   System.out.println("stock Name: " + stock.getName());
        // Fetch the user by the passed userName from the frontend
        UserEntity user = userRepo.findByUserName(userName)
                .orElseThrow(() -> new Exception("User not found"));
        
        if(stock == null) {
        	throw new Exception("Stock not found "+orderModel.getCompanyName());
        }
        
        // Create and populate the OrderEntity
        OrderEntity order = new OrderEntity();
        order.setStockDetail(stock);
        order.setUser(user);
        order.setQuantity(orderModel.getQuantity());
        order.setPrice(stockHistory.get().getPrice().multiply(BigDecimal.valueOf(orderModel.getQuantity())));  // Use the current price from the stock
        order.setOrderType(orderModel.getOrderType());
        order.setStatus("PENDING");  // Initially, the order is pending
        order.setCreatedAt(LocalDateTime.now());
        
     //   System.out.println("Testing but not working"+order);

        // Save order based on order type
        if (orderModel.getOrderType().equalsIgnoreCase("SELL")) {
            Optional<HoldingEntity> holding = holdingRepo.findByUser_UserNameAndStock_Symbol(user.getUserName(), stock.getSymbol());
            if (holding.isPresent() && holding.get().getNoOfStocks() >= orderModel.getQuantity()) {
                orderRepository.save(order);
            } else {
                throw new Exception("Not enough stocks to sell");
            }
        } else {
            // Save BUY orders directly
            orderRepository.save(order);
        }
//        orderRepository.save(order);
    }
    
    public void processPendingOrders() {
        // Fetch all pending orders
        List<OrderEntity> pendingOrders = orderRepository.findByStatus("PENDING");

        for (OrderEntity order : pendingOrders) {
            try {
                // Process each order based on its type
                if (order.getOrderType().equalsIgnoreCase("SELL")) {
                    processSellOrder(order);
                } else {
                    processBuyOrder(order);
                }
            } catch (Exception e) {
                System.err.println("Error processing order ID " + order.getOrderId() + ": " + e.getMessage());
            }
        }
    }

    private void processSellOrder(OrderEntity order) throws Exception {
        // Check if user has enough stocks to sell
        Optional<HoldingEntity> holdingOpt = holdingRepo.findByUser_UserNameAndStock_Symbol(
                order.getUser().getUserName(), order.getStockDetail().getSymbol());
        
        if (holdingOpt.isPresent() && holdingOpt.get().getNoOfStocks() >= order.getQuantity()) {
            // Update holding and mark order as SUCCESS
            HoldingEntity holding = holdingOpt.get();
            holding.setNoOfStocks(holding.getNoOfStocks() - order.getQuantity());
            holdingRepo.save(holding);

            order.setStatus("SUCCESS");
            orderRepository.save(order);

            System.out.println("Processed sell order ID " + order.getOrderId());
        } else {
            // Mark order as FAILED
            order.setStatus("FAILED");
            orderRepository.save(order);

            throw new Exception("Not enough stocks to sell for order ID " + order.getOrderId());
        }
    }

    private void processBuyOrder(OrderEntity order) {
        // Mark order as SUCCESS (you can add more complex logic if needed)
        order.setStatus("SUCCESS");
        orderRepository.save(order);

        System.out.println("Processed buy order ID " + order.getOrderId());
    }

//    private UserEntity getCurrentUser() {
////        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
////
////        if (principal instanceof UserDetails) {
////            String username = ((UserDetails) principal).getUsername();
////            return userRepo.findByUserName(username)
////                    .orElseThrow(() -> new RuntimeException("User not found: " + username));
////        } else {
////            throw new RuntimeException("No authenticated user found");
////        }
//    }
    
}


//    private String getCompanyNameById(Long companyId) {
//        // Fetch company name by companyId
//        Optional<Company> company = companyRepository.findById(companyId);
//        return company.map(Company::getName).orElse("Unknown");
//    }

