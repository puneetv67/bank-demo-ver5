package com.project.demo.bank.account;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.project.demo.bank.exception.AccountNotFoundException;
import com.project.demo.bank.exception.CustomExceptionMessage;

@RestController
@RequestMapping(value = "bank-demo")
public class AccountController{

	@Autowired
	private AccountService accountService;

	public AccountService getAccountService() {
		return accountService;
	}

	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}
	static Logger logger = Logger.getLogger(AccountController.class);
	private static final String PREFIX = LocalDateTime.now() + "****\t" + logger.getName() + ":\t";
	
	
	@GetMapping("/accounts")
	public ResponseEntity<List<Account>> getAllAccounts() throws AccountNotFoundException {
		HttpHeaders header = new HttpHeaders();
		header.add("desc", "request to return all accounts");
		logger.info( PREFIX + "Inside getAllAccounts() -> retrieving all available accounts");
		List<Account> accounts = accountService.getAllAccounts();
		if (accounts != null && !accounts.isEmpty()) {
			return ResponseEntity.status(HttpStatus.OK).headers(header).body(accounts);
		} else
			throw new AccountNotFoundException(CustomExceptionMessage.NO_ACCOUNTS_EXISTS_WITH_BANK.name());
	}

	@GetMapping("/accounts/{id}")
	public ResponseEntity<Account> getAccountByID(@PathVariable int id) throws AccountNotFoundException {
		HttpHeaders header = new HttpHeaders();
		header.add("desc", "request to return account associated with provided id");
		logger.info(PREFIX + "Inside getAccountByID() -> retrieving account associated with provided id");
		Optional<Account> accountByID = accountService.getAccountByID(id);
		if (accountByID.isPresent()) {
			return new ResponseEntity<>(accountByID.get(), header, HttpStatus.OK);
		} else
			throw new AccountNotFoundException(CustomExceptionMessage.NO_ACCOUNT_EXISTS_WITH_PROVIDED_ID.name());
	}

	@PostMapping("/accounts")
	public ResponseEntity<Void> addAccount(@RequestBody Account account) {
		accountService.addAccount(account);
		HttpHeaders header = new HttpHeaders();
		header.add("desc", "request to add an account");
		logger.info(PREFIX + "Inside addAccount() -> adding an account to repository");
		return ResponseEntity.status(HttpStatus.CREATED).headers(header).build();
	}

	@PutMapping("/accounts/{id}")
	public ResponseEntity<Void> updateAccount(@RequestBody Account account, @PathVariable int id) {
		accountService.updateAccount(account, id);
		HttpHeaders header = new HttpHeaders();
		header.add("desc", "this request will either update existing account or create a new account");
		logger.info(PREFIX + "Inside updateAccount() -> adding/updating an account to repository");
		return ResponseEntity.status(HttpStatus.CREATED).headers(header).build();
	}

	@DeleteMapping("/accounts")
	public ResponseEntity<Void> deleteAll() {
		accountService.deleteAll();
		HttpHeaders header = new HttpHeaders();
		header.add("desc", "request to delete all accounts");
		logger.info(PREFIX+ "Inside deleteAll() -> deleting all accounts from repository");
		return ResponseEntity.status(HttpStatus.OK).headers(header).build();
	}

	@DeleteMapping("/accounts/{id}")
	public ResponseEntity<Void> deleteAccountByID(@PathVariable int id) {
		accountService.deleteAccountByID(id);
		HttpHeaders header = new HttpHeaders();
		header.add("desc", "request to delete account associated with provided id");
		logger.info(PREFIX+"Inside deleteAccountByID() -> deleting account associated with provided it from repository");
		return ResponseEntity.status(HttpStatus.OK).headers(header).build();
	}
}
