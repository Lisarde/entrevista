package com.virtualcave.exercise.api.rest.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.format.DateTimeParseException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.NativeWebRequest;

import com.virtualcave.exercise.controller.V1ApiDelegate;
import com.virtualcave.exercise.entity.Currency;
import com.virtualcave.exercise.entity.TRates;
import com.virtualcave.exercise.model.Rate;
import com.virtualcave.exercise.model.RatePost;
import com.virtualcave.exercise.model.RateSearch;
import com.virtualcave.exercise.repository.TRatesRepository;
import com.virtualcave.exercise.utils.JsonUtil;

@Service
public class V1ApiDelegateImpl implements V1ApiDelegate {

	@Autowired
	TRatesRepository repository;

	@Autowired
	NativeWebRequest nativeWebRequest;

	@Override
	public ResponseEntity<Rate> getRateById(Integer id) {
		RestTemplate restTemplate = new RestTemplate();
		if (!JsonUtil.validateUser(getRequest().get().getHeader(HttpHeaders.AUTHORIZATION))) {
			// Se devuelve 401 si no se autoriza la peticion
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		} else {
			Optional<TRates> rateRepo = repository.findById(id);
			if (rateRepo.isPresent()) {
				Rate rate = JsonUtil.buildRate(rateRepo.get());
				Currency currency = restTemplate
						.getForObject("http://localhost:8081/v1/currencies/" + rate.getCurrencyCode(), Currency.class);
				rate.setPrice(JsonUtil.getPriceFormat(rateRepo.get().getPrice(), currency.getSymbol(),
						currency.getCode(), currency.getDecimals()));
				return ResponseEntity.ok().body(rate);
			} else {
				// Se devuelve 404 si no se encuentra
				return ResponseEntity.notFound().build();
			}
		}
	}

	@Override
	public ResponseEntity<Void> deleteRateById(Integer id) {
		if (!JsonUtil.validateUser(getRequest().get().getHeader(HttpHeaders.AUTHORIZATION))) {
			// Se devuelve 401 si no se autoriza la peticion
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		} else {
			Optional<TRates> rateRepo = repository.findById(id);
			if (rateRepo.isPresent()) {
				repository.delete(rateRepo.get());
				return ResponseEntity.ok(null);
			} else {
				// Se devuelve 404 si no se encuentra
				return ResponseEntity.notFound().build();
			}
		}
	}

	@Override
	public ResponseEntity<Void> createRate(RatePost rate) {
		if (!JsonUtil.validateUser(getRequest().get().getHeader(HttpHeaders.AUTHORIZATION))) {
			// Se devuelve 401 si no se autoriza la peticion
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		} else {
			TRates rateRepo = repository.save(JsonUtil.buildTRate(rate));
			URI uri = null;
			try {
				uri = new URI("http://localhost:8080/v1/rate/" + rateRepo.getId());
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
			return ResponseEntity.created(uri).build();
		}
	}

	@Override
	public ResponseEntity<Rate> searchRate(RateSearch param) {
		RestTemplate restTemplate = new RestTemplate();
		if (!JsonUtil.validateUser(getRequest().get().getHeader(HttpHeaders.AUTHORIZATION))) {
			// Se devuelve 401 si no se autoriza la peticion
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		} else {
			Optional<TRates> rateRepo;
			if (param.getBrandId() == null || param.getProductId() == null) {
				// Se devuelve 400 Bad request faltan parametros
				return ResponseEntity.badRequest().build();
			} else if (param.getStartDate() == null && param.getEndDate() == null) {
				// Se devuelve 400 Bad request faltan parametros
				return ResponseEntity.badRequest().build();
			} else if (param.getStartDate() != null && param.getEndDate() == null) {
				try {
					rateRepo = repository.findByBrandIdAndProductIdAndStartDate(param.getBrandId(),
							param.getProductId(), JsonUtil.convertStringToLocalDate(param.getStartDate()));
				} catch (DateTimeParseException e) {
					// Se devuelve 400 Bad request falla parametro fecha
					return ResponseEntity.badRequest().build();
				}

			} else if (param.getEndDate() != null && param.getStartDate() == null) {
				try {
					rateRepo = repository.findByBrandIdAndProductIdAndEndDate(param.getBrandId(), param.getProductId(),
							JsonUtil.convertStringToLocalDate(param.getEndDate()));
				} catch (DateTimeParseException e) {
					// Se devuelve 400 Bad request falla parametro fecha
					return ResponseEntity.badRequest().build();
				}
			} else {
				try {
					rateRepo = repository.findByBrandIdAndProductIdAndStartDateAndEndDate(param.getBrandId(),
							param.getProductId(), JsonUtil.convertStringToLocalDate(param.getStartDate()),
							JsonUtil.convertStringToLocalDate(param.getEndDate()));
				} catch (DateTimeParseException e) {
					// Se devuelve 400 Bad request falla parametro fecha
					return ResponseEntity.badRequest().build();
				}
			}
			if (rateRepo.isPresent()) {
				Rate rate = JsonUtil.buildRate(rateRepo.get());
				Currency currency = restTemplate
						.getForObject("http://localhost:8081/v1/currencies/" + rate.getCurrencyCode(), Currency.class);
				rate.setPrice(JsonUtil.getPriceFormat(rateRepo.get().getPrice(), currency.getSymbol(),
						currency.getCode(), currency.getDecimals()));
				return ResponseEntity.ok().body(rate);
			} else {
				// Se devuelve 404 si no se encuentra
				return ResponseEntity.notFound().build();
			}

		}

	}

	@Override
	public Optional<NativeWebRequest> getRequest() {
		return Optional.ofNullable(nativeWebRequest);
	}

}
