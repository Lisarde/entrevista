package com.virtualcave.exercise.entity;

import java.util.Objects;

import javax.annotation.Generated;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Currency
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2023-02-12T16:45:47.688664800+01:00[Europe/Madrid]")
public class Currency {

	@JsonProperty("symbol")
	private String symbol;

	@JsonProperty("code")
	private String code;

	@JsonProperty("decimals")
	private Integer decimals;

	public Currency symbol(String symbol) {
		this.symbol = symbol;
		return this;
	}

	/**
	 * Get symbol
	 * 
	 * @return symbol
	 */
	@NotNull
	@Schema(name = "symbol", example = "€", requiredMode = Schema.RequiredMode.REQUIRED)
	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public Currency code(String code) {
		this.code = code;
		return this;
	}

	/**
	 * ISO Code of the currency
	 * 
	 * @return code
	 */
	@NotNull
	@Schema(name = "code", example = "EUR", description = "ISO Code of the currency", requiredMode = Schema.RequiredMode.REQUIRED)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Currency decimals(Integer decimals) {
		this.decimals = decimals;
		return this;
	}

	/**
	 * Get decimals
	 * 
	 * @return decimals
	 */
	@NotNull
	@Schema(name = "decimals", example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
	public Integer getDecimals() {
		return decimals;
	}

	public void setDecimals(Integer decimals) {
		this.decimals = decimals;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Currency currency = (Currency) o;
		return Objects.equals(this.symbol, currency.symbol) && Objects.equals(this.code, currency.code)
				&& Objects.equals(this.decimals, currency.decimals);
	}

	@Override
	public int hashCode() {
		return Objects.hash(symbol, code, decimals);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class Currency {\n");
		sb.append("    symbol: ").append(toIndentedString(symbol)).append("\n");
		sb.append("    code: ").append(toIndentedString(code)).append("\n");
		sb.append("    decimals: ").append(toIndentedString(decimals)).append("\n");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Convert the given object to string with each line indented by 4 spaces
	 * (except the first line).
	 */
	private String toIndentedString(Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}
}
