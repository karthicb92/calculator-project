package com.casestudy.calculation.service;

import java.util.Base64;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Calculation service does the base64 decode and perform calculation on the given
 * input
 * @author
 *
 */
@Service
public class CalculationService {

	@Autowired
	private ScriptEngine scriptEngine;

	/**
	 * Converts Base64 encoded input into a plain string
	 * @param input Base64 String
	 * @return
	 * @throws Exception
	 */
	public String transformInput(String input) throws Exception {
		byte[] byteArr = Base64.getDecoder().decode(input.getBytes("UTF-8"));
		return new String(byteArr);
	}

	/**
	 * This method perform calculation and return the result
	 * @param input
	 * @return
	 * @throws ScriptException
	 */
	public Double calculate(String input) throws ScriptException {
		return Double.valueOf(scriptEngine.eval(input).toString());
	}
}
