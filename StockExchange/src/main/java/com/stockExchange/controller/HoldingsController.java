package com.stockExchange.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.stockExchange.dao.HoldingService;
import com.stockExchange.dto.ResponseDto;
import com.stockExchange.dto.UserResponseModel;

import io.jsonwebtoken.io.IOException;
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class HoldingsController {

	@Autowired
	private HoldingService holdingService;

	@PostMapping("/holdings")
	public ResponseEntity<ResponseDto> getUserHoldings(@RequestBody UserResponseModel user) {
		ResponseDto response = holdingService.getUserHoldings(user);
		return new ResponseEntity<ResponseDto>(response, HttpStatus.valueOf(response.getStatusCode()));
	}
	
//	@PostMapping("/upload")
//    public String handleFileUpload(@RequestParam("file") MultipartFile file) throws java.io.IOException {
//        if (file.isEmpty()) {
//            return "No file uploaded";
//        }
//
//        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
//            String line;
////            reader.readLine(); // Skip header row
//            while ((line = reader.readLine()) != null) {
//                String[] data = line.split(",");
//                if (data.length == 6) {
//                    //Long companyId = Long.parseLong(data[0]);
//                    String companyName = data[0];
//                    String symbol = data[1];
//                    String description = data[2];
//                    Long volume = Long.parseLong(data[3]);
//                    String date = data[4];
//                    BigDecimal stockPrice = new BigDecimal(data[5]);
//                    holdingService.saveStockData(companyName, symbol, description, volume, date, stockPrice);
//                }
//            }
//            return "File uploaded and data saved successfully";
//        } catch (IOException e) {
//            return "Error processing file: " + e.getMessage();
//        }
//    }
	
	@PostMapping("/place-order")
	public ResponseEntity<ResponseDto>placeOrders(@RequestParam String stockId, @RequestParam String userId,
			Integer quantity,String orderType){
		ResponseDto response = holdingService.placeOrder(stockId, userId,quantity,orderType);
		return new ResponseEntity<ResponseDto>(response, HttpStatus.valueOf(response.getStatusCode())); 
		
	}
	
}