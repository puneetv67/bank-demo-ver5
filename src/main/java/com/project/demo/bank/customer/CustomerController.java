package com.project.demo.bank.customer;

import java.math.BigDecimal;
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
import com.project.demo.bank.account.Account;
import com.project.demo.bank.account.AccountService;
import com.project.demo.bank.exception.AccountAlreadyExistsWithCustomerException;
import com.project.demo.bank.exception.AccountNotFoundException;
import com.project.demo.bank.exception.CustomExceptionMessage;
import com.project.demo.bank.exception.CustomerNotFoundException;

@RestController
@RequestMapping(value = "bank-demo")
public class CustomerController {
	
	private static final Logger logger = Logger.getLogger(CustomerController.class);
	private static final String PREFIX = LocalDateTime.now() + "****\t" + logger.getName() + ":\t";
	@Autowired
	private CustomerService customerService;
	@Autowired
	private AccountService accountService;

	public CustomerService getCustomerService() {
		return customerService;
	}

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}

	@GetMapping("/customers")
	public ResponseEntity<List<Customer>> getAllCustomers() {
		List<Customer> customers = getCustomerService().getAllCustomers();
		HttpHeaders header = new HttpHeaders();
		header.add("desc", "request to return all customers");
		logger.info(PREFIX + "Inside getAllCustomers()-> return all customers from repository");
		if (customers != null && !customers.isEmpty()) {
			return ResponseEntity.status(HttpStatus.OK).headers(header).body(customers);
		} else
			throw new CustomerNotFoundException(CustomExceptionMessage.NO_CUSTOMERS_EXISTS_WITH_BANK.name());
	}

	@GetMapping("/customers/{id}")
	public ResponseEntity<Optional<Customer>> getCustomerByID(@PathVariable String id) {
		Optional<Customer> customerByID = getCustomerService().getCustomerByID(id);
		HttpHeaders header = new HttpHeaders();
		header.add("desc", "request to return customer associated with provided id");
		logger.info(PREFIX + "Inside getCustomerByID()-> return all customers associated with provide id from repository");
		if (customerByID.isPresent()) {
			return new ResponseEntity<>(customerByID, header, HttpStatus.OK);
		} else
			throw new CustomerNotFoundException(CustomExceptionMessage.NO_CUSTOMER_EXISTS_WITH_PROVIDED_ID.name());

	}

	@PostMapping("/customers")
	public ResponseEntity<Void> addCustomer(@RequestBody Customer customer) {
		customerService.addCustomer(customer);
		HttpHeaders header = new HttpHeaders();
		header.add("desc", "request to add a customer");
		logger.info(PREFIX + "Inside addCustomer()-> adding a customer to repository");
		return ResponseEntity.status(HttpStatus.CREATED).headers(header).build();
	}

	@PutMapping(value = "/customers/{id}")
	public ResponseEntity<Void> updateCustomer(@RequestBody Customer customer, @PathVariable String id) {
		customerService.updateCustomer(customer, id);
		HttpHeaders header = new HttpHeaders();
		header.add("desc", "this request will either update existing customer or create a new customer");
		logger.info(PREFIX + "Inside updateCustomer()-> adding/updating a customer to repository");
		return ResponseEntity.status(HttpStatus.CREATED).headers(header).build();
	}

	@DeleteMapping("/customers")
	public ResponseEntity<Void> deleteAll() {
		customerService.deleteAll();
		HttpHeaders header = new HttpHeaders();
		header.add("desc", "request to delete all customers");
		logger.info(PREFIX + "Inside deleteAll()-> deleting all customers from repository");
		return ResponseEntity.status(HttpStatus.OK).headers(header).build();
	}

	@DeleteMapping("/customers/{id}")
	public ResponseEntity<Void> deleteCustomerByID(@PathVariable String id) {
		customerService.deleteCustomerByID(id);
		HttpHeaders header = new HttpHeaders();
		header.add("desc", "request to delete customer associated with provided id");
		logger.info(PREFIX + "Inside deleteCustomerByID()-> deleting customer associated with provided id from repository");
		return ResponseEntity.status(HttpStatus.OK).headers(header).build();
	}

	@PostMapping("/customers/{id}/addAccount")
	public ResponseEntity<Customer> addAccountToCustomer(@PathVariable String id, @RequestBody Account account) {
		logger.info(PREFIX + "Inside addAccountToCustomer()-> adding an account to provided customer");
		Optional<Customer> customer = getCustomerService().getCustomerByID(id);
		if (customer.isPresent()) {
			boolean outcome = getCustomerService().addAccountToCustomer(customer.get(), account);
			if (!outcome) {
				throw new AccountAlreadyExistsWithCustomerException(
						CustomExceptionMessage.SIMILAR_ACCOUNT_ALREADY_ATTACHED_WITH_CUSTOMER.name());
			} else {
				HttpHeaders header = new HttpHeaders();
				header.add("desc", "request to add account to customer");
				return ResponseEntity.status(HttpStatus.OK).headers(header).body(customer.get());
			}
		} else {
			throw new CustomerNotFoundException(CustomExceptionMessage.NO_CUSTOMER_EXISTS_WITH_PROVIDED_ID.name());
		}
	}

	@PostMapping("/customers/{customerId}/accounts/{accountId}")
	public ResponseEntity<Customer> addAccountToCustomer(@PathVariable String customerId, @PathVariable int accountId) {
		logger.info(PREFIX + "Inside addAccountToCustomer()-> associating provided account id to provided customer id");
		boolean outcome=false;
		Optional<Customer> customer = getCustomerService().getCustomerByID(customerId);
		Optional<Account> account = accountService.getAccountByID(accountId);
		if (customer.isPresent() && account.isPresent()) {
			outcome = getCustomerService().addAccountToCustomer(customer.get(), account.get());
		} else if (!customer.isPresent()) {
			throw new CustomerNotFoundException(CustomExceptionMessage.NO_CUSTOMER_EXISTS_WITH_PROVIDED_ID.name());
		} else {
			throw new AccountNotFoundException(CustomExceptionMessage.NO_ACCOUNT_EXISTS_WITH_PROVIDED_ID.name());
		}
		if (!outcome) {
			throw new AccountAlreadyExistsWithCustomerException(
					CustomExceptionMessage.SIMILAR_ACCOUNT_ALREADY_ATTACHED_WITH_CUSTOMER.name());
		} else {
			HttpHeaders header = new HttpHeaders();
			header.add("desc", "request to add account to customer");
			return ResponseEntity.status(HttpStatus.OK).headers(header).body(customer.get());
		}
	}

	@PostMapping("/customers/transfers/{fromCustomerId}/{toCustomerId}/{amount}")
	public ResponseEntity<String> transferFunds(@PathVariable String fromCustomerId, @PathVariable String toCustomerId,
			@PathVariable BigDecimal amount) {
		logger.info(PREFIX + "Inside transferFunds()-> transferring funds from one account to another");
		String transferFunds = getCustomerService().transferFunds(fromCustomerId, toCustomerId, amount);
		HttpHeaders header = new HttpHeaders();
		header.add("desc", "request to transfer funds between customers provided");
		return ResponseEntity.status(HttpStatus.OK).headers(header).body(transferFunds);
	}
}
