package com.stockExchange.daoImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class OrderProcessingService {

    @Autowired
    private OrderService orderService;

    // Scheduled task to run daily at 5:30 PM (use cron expression)
    @Scheduled(cron = "5 09 14 * * *")
    public void processPendingOrders() {
        try {
            System.out.println("Running scheduled task to process pending orders...");

            // Process pending orders logic
            orderService.processPendingOrders();

        } catch (Exception e) {
            System.err.println("Error processing pending orders: " + e.getMessage());
        }
    }
}
