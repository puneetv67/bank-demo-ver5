package com.project.demo.bank.exception;

import org.apache.log4j.Logger;

public class AccountNotFoundException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(AccountNotFoundException.class);
	
	public AccountNotFoundException(){
	}

	public AccountNotFoundException(String message) {
		super();
		logger.info(message);
	}
}
