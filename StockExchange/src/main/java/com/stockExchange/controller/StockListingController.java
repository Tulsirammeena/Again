	package com.stockExchange.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stockExchange.daoImpl.StockListingService;
import com.stockExchange.dto.ResponseDto;
import com.stockExchange.dto.StockModel;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/stocks")
public class StockListingController {

	@Autowired
	private StockListingService stockService;

	/*
	 * End-point to fetch all available stocks.
	 *
	 * @return ResponseEntity with a list of StockModel and HTTP status
	 */
	
	@GetMapping("/list")
	public ResponseEntity<ResponseDto> getAllStocks() {
			// Fetch the list of stocks from the service
			ResponseDto response = stockService.getAvailableStocks();
			return new ResponseEntity<ResponseDto>(response, HttpStatus.valueOf(response.getStatusCode()));
	}
}
