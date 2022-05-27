package com.casestudy.calculation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CalculationResponse {
	@ApiModelProperty(notes = "Indicates whether response has an error or not")
	@JsonInclude(Include.NON_NULL)
	private boolean error;
	@ApiModelProperty(notes = "The result of calculation is stored in this variable")
	@JsonInclude(Include.NON_NULL)
	private Double result;
	@ApiModelProperty(notes = "Shows message when there is an error")
	@JsonInclude(Include.NON_NULL)
	private String message;
}
