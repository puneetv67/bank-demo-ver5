package com.project.demo.bank.exception;

import org.apache.log4j.Logger;

public class AccountAlreadyExistsWithCustomerException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(AccountAlreadyExistsWithCustomerException.class);
	
	public AccountAlreadyExistsWithCustomerException() {
	}
	
	public AccountAlreadyExistsWithCustomerException(String message) {
		super();
		logger.info(message);
	}
}
