package com.casestudy.gateway;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * This controller has fall back methods to be called for each service
 * 
 * @author
 */
@RestController
@RequestMapping("/")
public class FallBackResource {
	
	/**
	 * Prints Hello World message
	 * @return
	 */
	@GetMapping("/")
	public String greet() {
		return "Hello from Gateway Service";
	}

	/**
	 * When Calculation service is done, this method is executed as a fallback
	 * 
	 * @return
	 */
	@GetMapping("/calculationServiceFallBack")
	public String calculationServiceFallBackMethod() {
		return "Calculation Service is either down or taking longer time." + " Please try again later";
	}
}
