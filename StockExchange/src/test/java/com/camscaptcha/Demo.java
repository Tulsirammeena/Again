package com.camscaptcha;

import java.time.LocalDate;

public class Demo {

	 public static void main(String[] args) {
	       String date = "2024-09-03";
	       try {
	       LocalDate value = LocalDate.parse(date);
	       System.out.println(value);
	       }catch(Exception e) {
	    	   e.printStackTrace();
	       }
	       
	    }
}
