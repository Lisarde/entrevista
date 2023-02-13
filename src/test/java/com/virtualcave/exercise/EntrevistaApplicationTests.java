package com.virtualcave.exercise;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.Base64Utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.virtualcave.exercise.api.rest.controller.V1ApiDelegateImpl;
import com.virtualcave.exercise.controller.V1ApiController;
import com.virtualcave.exercise.entity.Currency;
import com.virtualcave.exercise.entity.TRates;
import com.virtualcave.exercise.repository.TRatesRepository;
import com.virtualcave.exercise.utils.JsonUtil;

@ExtendWith(SpringExtension.class)
@ComponentScan(basePackages = { "com.virtualcave.exercise", "org.openapitools" })
@WebAppConfiguration
@WebMvcTest(V1ApiController.class)
@Import(V1ApiDelegateImpl.class)
class EntrevistaApplicationTests {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private TRatesRepository rep;

	static Optional<TRates> dummyTRates;

	private static boolean setUpIsDone = false;

	@BeforeEach
	public void init() {
		if (setUpIsDone) {
			return;
		}
		JsonNode eurJson = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			Currency eur = new Currency();
			eur.setCode("EUR");
			eur.setSymbol("€");
			eur.setDecimals(2);
			String eurstrjson = mapper.writeValueAsString(eur);
			eurJson = mapper.readTree(eurstrjson);
		} catch (JsonProcessingException e) {
		}
		WireMockServer wireMockServer = new WireMockServer(8081);
		wireMockServer.start();
		wireMockServer.stubFor(get("/v1/currencies/EUR").atPriority(1).willReturn(
				aResponse().withStatus(200).withHeader("Content-Type", "application/json").withJsonBody(eurJson)));

		dummyTRates = Optional.of(new TRates(1, 1, 1, JsonUtil.convertStringToLocalDate("2022-01-01"),
				JsonUtil.convertStringToLocalDate("2022-05-31"), 1550, "EUR"));

		setUpIsDone = true;
	}

	@Test
	public void testGetRateByIdBadCredent() throws Exception {
		Mockito.when(rep.findById(1)).thenReturn(dummyTRates);
		// Se comprueba que da 401 al no enviar authenticacion
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("http://localhost:8080/v1/rates/get/1");
		this.mvc.perform(requestBuilder).andDo(print()).andExpect(status().is(401));
	}

	@Test
	public void testGetRateByIdOk() throws Exception {
		Mockito.when(rep.findById(1)).thenReturn(dummyTRates);
		// Se comprueba que da 200 al no enviar authenticacion
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/v1/rates/get/1").header(
				HttpHeaders.AUTHORIZATION, "Basic " + Base64Utils.encodeToString("entrevista:entrevista".getBytes()));
		this.mvc.perform(requestBuilder).andDo(print()).andExpect(status().isOk());
	}

	@Test
	public void testGetRateByIdNotFound() throws Exception {
		Mockito.when(rep.findById(1)).thenReturn(dummyTRates);
		// Se comprueba que da 404 al no encontrar el elemento
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("http://localhost:8080/v1/rates/get/999").header(HttpHeaders.AUTHORIZATION,
						"Basic " + Base64Utils.encodeToString("entrevista:entrevista".getBytes()));
		this.mvc.perform(requestBuilder).andDo(print()).andExpect(status().is(404));
	}

}
