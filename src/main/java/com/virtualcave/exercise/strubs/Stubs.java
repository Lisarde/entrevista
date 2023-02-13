package com.virtualcave.exercise.strubs;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.matching.RegexPattern;
import com.github.tomakehurst.wiremock.matching.UrlPattern;
import com.virtualcave.exercise.entity.Currency;

public class Stubs {
	public WireMockServer wireMockServer;
	public JsonNode lstCurrJson;
	public JsonNode eurJson;
	public JsonNode usdJson;

	public Stubs setUp() {
		List<Currency> lstCurr = new ArrayList<>();
		Currency eur = new Currency();
		eur.setCode("EUR");
		eur.setSymbol("€");
		eur.setDecimals(2);
		lstCurr.add(eur);
		Currency usd = new Currency();
		usd.setCode("USD");
		usd.setSymbol("$");
		usd.setDecimals(1);
		lstCurr.add(usd);

		try {
			ObjectMapper mapper = new ObjectMapper();
			String lststrjson = mapper.writeValueAsString(lstCurr);
			lstCurrJson = mapper.readTree(lststrjson);
			String eurstrjson = mapper.writeValueAsString(eur);
			eurJson = mapper.readTree(eurstrjson);
			String usdstrjson = mapper.writeValueAsString(usd);
			usdJson = mapper.readTree(usdstrjson);
		} catch (JsonProcessingException e) {
		}

		wireMockServer = new WireMockServer(8081);
		wireMockServer.start();
		return this;
	}

	public Stubs resetServer() {
		wireMockServer.resetAll();
		return this;
	}

	public Stubs stubForGetCurrenciesOut() {
		wireMockServer.stubFor(get(urlMatching("/v1/currencies/.*")).atPriority(3)
				.willReturn(aResponse().withStatus(404).withBody("Currency not found")));
		return this;
	}

	public Stubs stubForGetCurrencies(String responseFileName) {
		wireMockServer.stubFor(get("/v1/currencies").atPriority(2).willReturn(
				aResponse().withStatus(200).withHeader("Content-Type", "application/json").withJsonBody(lstCurrJson)));
		return this;
	}

	public Stubs stubForGetCurrenciesEUR(String responseFileName) {
		wireMockServer.stubFor(get("/v1/currencies/EUR").atPriority(1).willReturn(
				aResponse().withStatus(200).withHeader("Content-Type", "application/json").withJsonBody(eurJson)));
		return this;
	}

	public Stubs stubForGetCurrenciesUSD(String responseFileName) {
		wireMockServer.stubFor(get("/v1/currencies/USD").atPriority(1).willReturn(
				aResponse().withStatus(200).withHeader("Content-Type", "application/json").withJsonBody(usdJson)));
		return this;
	}

	public Stubs status() {
		System.out.println("Stubs Started!");
		return this;
	}

	private static UrlPattern urlMatching(String urlRegex) {
		return new UrlPattern(new RegexPattern(urlRegex), true);
	}
}