package com.project.demo.bank.exception;

import org.apache.log4j.Logger;

public class CustomerNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(CustomerNotFoundException.class);
	
	public CustomerNotFoundException(){
	}
	
	public CustomerNotFoundException(String message) {
		super();
		logger.info(message);
	}
}
