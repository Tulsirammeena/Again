package com.stockExchange.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stockExchange.daoImpl.OrderService;
import com.stockExchange.dto.OrderModel;


@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/history")
    public List<OrderModel> getOrderHistory(
            @RequestParam(value = "userName", required = false) String userName,
            @RequestParam(value = "beginDate", required = false) String beginDateStr,
            @RequestParam(value = "endDate", required = false) String endDateStr) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime beginDate = beginDateStr != null ? LocalDate.parse(beginDateStr, formatter).atStartOfDay() : null;
        LocalDateTime endDate = endDateStr != null ? LocalDate.parse(endDateStr, formatter).atTime(23, 59, 59) : null;

        return orderService.getOrderHistory(userName, beginDate, endDate);
    }
    @CrossOrigin(origins = "*	")
    @PostMapping("/create")
    public ResponseEntity<String> createOrder(@RequestBody OrderModel orderModel) {
        try {
            // Call the service to process the order
        	System.out.println("Recieved orderModel: " + orderModel);
        	
        	System.out.println("Recieved userName: " + orderModel.getCompanyName());
            orderService.createOrder(orderModel,orderModel.getUserName());
            return ResponseEntity.ok("Order placed successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error creating order: " + e.getMessage());
        }
    }
}



