package com.virtualcave.exercise.utils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

import com.virtualcave.exercise.entity.TRates;
import com.virtualcave.exercise.model.Rate;
import com.virtualcave.exercise.model.RatePost;

public class JsonUtil {

	public static Rate buildRate(TRates rateBD) {
		Rate rate = new Rate();
		rate.setBrandId(rateBD.getBrandId());
		rate.setCurrencyCode(rateBD.getCurrencyCode());
		rate.setEndDate(rateBD.getEndDate());
		rate.setId(rateBD.getId());
//		rate.setPrice(null);
		rate.setProductId(rateBD.getProductId());
		rate.setStartDate(rateBD.getStartDate());
		return rate;
	}

	public static TRates buildTRate(RatePost rate) {
		TRates rateBD = new TRates();
		rateBD.setBrandId(rate.getBrandId());
		rateBD.setCurrencyCode(rate.getCurrencyCode());
		rateBD.setEndDate(rate.getEndDate());
		rateBD.setPrice(rate.getPrice());
		rateBD.setProductId(rate.getProductId());
		rateBD.setStartDate(rate.getStartDate());

		return rateBD;
	}

//En caso de que se refiera a añadir unicamente el numero de decimales en ceros
//	public static String getPriceFormat(int price, String currency, int decimals) {
//		return String.format("%." + decimals + "f", Float.valueOf(price)).concat(" " + currency);
//	}

	// recorre los decimales el número indicado de decimales
	public static String getPriceFormat(int price, String currency, String code, int decimals) {

		String precio = String.valueOf(price);
		String entero = precio.substring(0, precio.length() - decimals);
		String decimales = precio.substring(precio.length() - decimals, precio.length());

		return entero + "." + decimales + " " + currency + " (" + code + ")";
	}

	public static LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
		return dateToConvert.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

	public static LocalDate convertStringToLocalDate(String date) throws DateTimeParseException {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		return LocalDate.parse(date, formatter);
	}

	public static boolean validateUser(String authent) {
		return "Basic ZW50cmV2aXN0YTplbnRyZXZpc3Rh".equals(authent);
	}
}
