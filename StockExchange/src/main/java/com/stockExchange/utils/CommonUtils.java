package com.stockExchange.utils;

import java.security.SecureRandom;

public class CommonUtils {
	
	public static long generateDAN() {
		SecureRandom rand = new SecureRandom();
        long randomNumber = (long) (rand.nextDouble() * 10000000000000000L);
     //   System.out.println(String.format("%016d", randomNumber));
       long l = Long.parseLong(String.format("%016d", randomNumber));
       return l;      
	}
}
