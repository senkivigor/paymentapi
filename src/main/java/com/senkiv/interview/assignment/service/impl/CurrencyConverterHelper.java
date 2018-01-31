package com.senkiv.interview.assignment.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class CurrencyConverterHelper {

	private static final Map<String, Map<String, Double>> EXCHANGE_RATES;

	static {
		Map<String, Double> USD_EXCHANGE_RATE = new HashMap<>();
		USD_EXCHANGE_RATE.put("EUR", 0.8);
		USD_EXCHANGE_RATE.put("UAH", 28d);

		Map<String, Double> EUR_EXCHANGE_RATE = new HashMap<>();
		EUR_EXCHANGE_RATE.put("USD", 1.25);
		EUR_EXCHANGE_RATE.put("UAH", 33d);

		Map<String, Double> UAH_EXCHANGE_RATE = new HashMap<>();
		UAH_EXCHANGE_RATE.put("USD", 0.035);
		UAH_EXCHANGE_RATE.put("EUR", 0.3);

		EXCHANGE_RATES = new HashMap<>();
		EXCHANGE_RATES.put("USD", USD_EXCHANGE_RATE);
		EXCHANGE_RATES.put("EUR", EUR_EXCHANGE_RATE);
		EXCHANGE_RATES.put("UAH", UAH_EXCHANGE_RATE);
	}

	public static BigDecimal convert(final String fromCurrency, final String toCurrency, final BigDecimal amount) {
		Map<String, Double> exchangeRates = EXCHANGE_RATES.get(fromCurrency);
		Double rate = exchangeRates.get(toCurrency);
		if (fromCurrency.equals(toCurrency)) {
			return amount;
		}
		return amount.multiply(new BigDecimal(rate)).setScale(2, BigDecimal.ROUND_UP);
	}

	public static boolean isCurrencySupported(String currency) {
		return EXCHANGE_RATES.containsKey(currency);
	}
}
