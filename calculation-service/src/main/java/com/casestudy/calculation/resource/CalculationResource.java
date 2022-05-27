package com.casestudy.calculation.resource;

import static com.casestudy.calculation.constants.CalculationConstant.EMPTY_INPUT;
import static com.casestudy.calculation.constants.CalculationConstant.GREET_MESSAGE;
import static com.casestudy.calculation.constants.CalculationConstant.INVALID_INPUT;
import static com.casestudy.calculation.constants.CalculationConstant.MISSING_MANDATORY_PARAM;
import static com.casestudy.calculation.constants.CalculationConstant.WRONG_EXPRESSION;

import javax.script.ScriptException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.casestudy.calculation.dto.CalculationResponse;
import com.casestudy.calculation.service.CalculationService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

/**
 * This api has endpoint for performing calculation
 * @author
 *
 */
@RestController
@RequestMapping("/")
@Slf4j
@Api(value = "Calculation Service Api")
public class CalculationResource {

	@Autowired
	private CalculationService calculatorService;

	@ApiOperation(value = "Returns Hello Message")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success response") })
	@GetMapping("/greet")
	public String greet() {
		return GREET_MESSAGE;
	}

	@ApiOperation(value = "Calculates a given arithmetic expression")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success response"),
			@ApiResponse(code = 400, message = "Bad Request"), @ApiResponse(code = 500, message = "Invalid Input") })
	@GetMapping("/calculus")
	public ResponseEntity<CalculationResponse> calculate(
			@RequestParam(value = "query", required = true) String queryInput) {
		log.info("input====> {}", queryInput);
		CalculationResponse calculatorResponse;
		if (!StringUtils.hasText(queryInput)) {
			calculatorResponse = CalculationResponse.builder().error(true).message(EMPTY_INPUT).build();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(calculatorResponse);
		}
		try {
			double result = calculatorService.calculate(calculatorService.transformInput(queryInput));
			calculatorResponse = CalculationResponse.builder().error(false).result(result).build();
		} catch (ScriptException se) {
			calculatorResponse = CalculationResponse.builder().error(true).message(WRONG_EXPRESSION).build();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(calculatorResponse);
		} catch (Exception e) {
			calculatorResponse = CalculationResponse.builder().error(true).message(INVALID_INPUT).build();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(calculatorResponse);
		}
		return ResponseEntity.status(HttpStatus.OK).body(calculatorResponse);
	}
	
	/**
	 * Handles the missing mandatory parameters and returns the appropriate error message
	 * @param ex MissingServletRequestParameterException
	 * @return
	 */
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<CalculationResponse> handleMissingParams(MissingServletRequestParameterException ex) {
		String paramName = ex.getParameterName();
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(CalculationResponse.builder().error(true).message(paramName + MISSING_MANDATORY_PARAM).build());
	}

}
