package com.casestudy.calculation.test;

import static com.casestudy.calculation.constants.CalculationConstant.EMPTY_INPUT;
import static com.casestudy.calculation.constants.CalculationConstant.INVALID_INPUT;
import static com.casestudy.calculation.constants.CalculationConstant.MISSING_MANDATORY_PARAM;
import static com.casestudy.calculation.constants.CalculationConstant.WRONG_EXPRESSION;
import static io.restassured.RestAssured.get;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CalculationServiceApplicationTests {

	@LocalServerPort
	private int port;

	private String url;

	@Test
	void greetMessageTest() {
		url = String.format("http://localhost:%d/greet", port);
		get(url).then().assertThat().statusCode(HttpStatus.OK.value());
	}

	@Test
	void missingMandatoryQueryParamTest() {
		url = String.format("http://localhost:%d/calculus", port);
		get(url).then().statusCode(HttpStatus.BAD_REQUEST.value()).body("error", Matchers.equalTo(true), "message",
				Matchers.containsString(MISSING_MANDATORY_PARAM));
	}

	@Test
	void emptyQueryParamTest() {
		url = String.format("http://localhost:%d/calculus?query=", port);
		get(url).then().statusCode(HttpStatus.BAD_REQUEST.value()).body("error", Matchers.equalTo(true), "message",
				Matchers.equalTo(EMPTY_INPUT));
	}

	@Test
	void successCalculationTest() {
		url = String.format("http://localhost:%d/calculus?query=Misz", port);
		get(url).then().statusCode(HttpStatus.OK.value()).body("error", Matchers.equalTo(false), "result",
				Matchers.equalTo(5.0f));
	}

	@Test
	void wrongQueryExpressionTest() {
		url = String.format("http://localhost:%d/calculus?query=MisoMyo1KSk=", port);
		get(url).then().statusCode(HttpStatus.BAD_REQUEST.value()).body("error", Matchers.equalTo(true), "message",
				Matchers.equalTo(WRONG_EXPRESSION));
	}

	@Test
	void invalidInputTest() {
		url = String.format("http://localhost:%d/calculus?query=$", port);
		get(url).then().statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).body("error", Matchers.equalTo(true),
				"message", Matchers.equalTo(INVALID_INPUT));
	}
}