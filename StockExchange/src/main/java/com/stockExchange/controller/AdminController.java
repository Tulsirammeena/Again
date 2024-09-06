package com.stockExchange.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.stockExchange.dao.AdminService;
import com.stockExchange.daoImpl.OrderProcessingService;

import io.jsonwebtoken.io.IOException;
@CrossOrigin(origins = "*", allowedHeaders ="*")
@RestController
@RequestMapping("/admin")
public class AdminController {

	@Autowired
    private AdminService adminCompanyService;

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file) throws java.io.IOException {
        if (file.isEmpty()) {
            return "No file uploaded";
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
//            reader.readLine(); // Skip header row
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 6) {
                    //Long companyId = Long.parseLong(data[0]);
                    String companyName = data[0];
                    String symbol = data[1];
                    String description = data[2];
                    Long volume = Long.parseLong(data[3]);
                    String date = data[4];
                    BigDecimal stockPrice = new BigDecimal(data[5]);
                 //   Long quantity = Long.parseLong(data[6]);
                    adminCompanyService.saveStockData(companyName, symbol, description, volume, date, stockPrice);
                }
            }
            return "File uploaded and data saved successfully";
        } catch (IOException e) {
            return "Error processing file: " + e.getMessage();
        }
    }

	
    @Autowired
    private OrderProcessingService orderProcessingService;

    @PostMapping("/process-orders")
    public ResponseEntity<String> processOrdersManually() {
        try {
            orderProcessingService.processPendingOrders();
            return ResponseEntity.ok("Pending orders processed successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing orders: " + e.getMessage());
        }
    }
}