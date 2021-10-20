package com.project.demo.bank.exception;

import org.apache.log4j.Logger;

public class InsufficientFundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(InsufficientFundException.class);

	public InsufficientFundException(){
	}
	public InsufficientFundException(String message) {
		super();
		logger.info(message);
	}
	
}
