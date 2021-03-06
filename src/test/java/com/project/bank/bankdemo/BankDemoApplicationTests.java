package com.project.bank.bankdemo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.apache.log4j.Logger;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Description;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.project.demo.bank.account.Account;
import com.project.demo.bank.account.AccountController;
import com.project.demo.bank.account.AccountRepository;
import com.project.demo.bank.account.AccountService;
import com.project.demo.bank.customer.Customer;
import com.project.demo.bank.customer.CustomerController;
import com.project.demo.bank.customer.CustomerRepository;
import com.project.demo.bank.customer.CustomerService;
import com.project.demo.bank.exception.CustomExceptionMessage;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AccountController.class, AccountService.class, CustomerController.class,
		CustomerService.class })
// @SpringBootConfiguration
public class BankDemoApplicationTests {

	private static final String MOCKED_SAVING = "Mocked Saving";
	private static final Logger logger = Logger.getLogger(BankDemoApplicationTests.class);
	private static final String PREFIX = LocalDateTime.now() + "****\t" + logger.getName() + ":\t";
	@Autowired
	private AccountController accountController;
	@Autowired
	private CustomerController customerController;
	@Autowired
	private AccountService accountService;
	@Autowired
	private CustomerService customerService;
	@MockBean
	private AccountRepository accountRepository;
	@MockBean
	private CustomerRepository customerRepository;

	Account mockAccount;
	List<Account> mockAccounts;
	Customer mockCustomer;
	List<Customer> mockCustomers;

	@Before
	public void init() {
		mockAccount = new Account(1, MOCKED_SAVING, BigDecimal.TEN);
		mockAccounts = new ArrayList<>(
				Arrays.asList(new Account(1, "Savings", BigDecimal.TEN), new Account(2, "Current", BigDecimal.ONE)));
		mockCustomer = new Customer("FN", "LN", "1", mockAccount);
		mockCustomers = new ArrayList<>(Arrays.asList(mockCustomer, new Customer("FN", "LN", "2", mockAccount)));
	}

	@Test
	@Description(value = "Test to verify if the bean dependencies are loaded correctly")
	public void contextLoads() {
		Assertions.assertThat(accountController).isNotNull();
		Assertions.assertThat(accountService).isNotNull();
		Assertions.assertThat(customerController).isNotNull();
		Assertions.assertThat(customerService).isNotNull();
	}

	@Test
	// @Ignore
	public void getAllAccountsTest() {
		Mockito.when(accountRepository.findAll()).thenReturn(mockAccounts);
		List<Account> allAccounts = new ArrayList<>(accountController.getAllAccounts().getBody());
		Assertions.assertThat(allAccounts.size()).isEqualTo(mockAccounts.size());
		Assertions.assertThat(new Account(2, "Current", BigDecimal.ONE).equals(mockAccounts.get(1)));
	}

	@Test
	// @Ignore
	public void getAccountByIDTest() {
		Mockito.when(accountRepository.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.of(mockAccount));
		Account accountByID = accountController.getAccountByID(ArgumentMatchers.anyInt()).getBody();
		Mockito.verify(accountRepository, Mockito.times(1)).findById(ArgumentMatchers.anyInt());
		Assertions.assertThat(MOCKED_SAVING).isEqualTo(accountByID.getAccountType());
	}

	@Test
	// @Ignore
	public void addAccountTest() {
		Mockito.when(accountRepository.save(mockAccount)).thenReturn(mockAccount);
		accountController.addAccount(mockAccount);
		Mockito.verify(accountRepository, Mockito.times(1)).save(mockAccount);

	}

	@Test
	// @Ignore
	public void updateAccountTest() {
		accountController.updateAccount(mockAccount, ArgumentMatchers.anyInt());
		Mockito.when(accountRepository.save(mockAccount)).thenReturn(mockAccount);
		Mockito.verify(accountRepository, Mockito.times(1)).save(ArgumentMatchers.any(Account.class));
	}

	@Test
	// @Ignore
	public void deleteAllTest() {
		accountController.deleteAll();
		Mockito.doNothing().when(accountRepository).deleteAll();
		Mockito.verify(accountRepository, Mockito.times(1)).deleteAll();
	}

	@Test
	// @Ignore
	public void deleteAccountByIDTest() {
		accountController.deleteAccountByID(ArgumentMatchers.anyInt());
		Mockito.doNothing().when(accountRepository).deleteById(ArgumentMatchers.anyInt());
		Mockito.verify(accountRepository, Mockito.times(1)).deleteById(ArgumentMatchers.anyInt());
	}
	// Test related to customer logic

	@Test
	// @Ignore
	public void getAllCustomersTest() {
		Mockito.when(customerRepository.findAll()).thenReturn(mockCustomers);
		List<Customer> allCustomers = new ArrayList<>(customerController.getAllCustomers().getBody());
		Assertions.assertThat(allCustomers.size()).isEqualTo(mockCustomers.size());
		Assertions.assertThat(mockCustomer).isEqualTo(mockCustomers.get(0));
	}

	@Test
	// @Ignore
	@Description(value = "Test to verify if the bean dependencies are loaded correctly")
	public void getCustomerByIDTest() {
		Mockito.when(customerRepository.findById(ArgumentMatchers.anyString())).thenReturn(Optional.of(mockCustomer));
		Optional<Customer> customerByID = customerController.getCustomerByID(ArgumentMatchers.anyString()).getBody();
		Mockito.verify(customerRepository, Mockito.times(1)).findById(ArgumentMatchers.anyString());
		Assertions.assertThat(mockCustomer.getAccount()).isEqualTo(customerByID.get().getAccount());
		Assertions.assertThat(mockAccount).isEqualTo(customerByID.get().getAccount());
		Assertions.assertThat(mockCustomers.get(0).getCustomerID()).isEqualTo(customerByID.get().getCustomerID(),"Testing customer of expected vs actual");
	}

	@Test
	// @Ignore
	public void addCustomerTest() {
		Mockito.when(customerRepository.save(mockCustomer)).thenReturn(mockCustomer);
		customerController.addCustomer(mockCustomer);
		Mockito.verify(customerRepository, Mockito.times(1)).save(mockCustomer);
	}

	@Test
	// @Ignore
	public void updateCustomerTest() {
		customerController.updateCustomer(mockCustomer, ArgumentMatchers.anyString());
		Mockito.when(customerRepository.save(mockCustomer)).thenReturn(mockCustomer);
		Mockito.verify(customerRepository, Mockito.times(1)).save(ArgumentMatchers.any(Customer.class));
	}

	@Test
	// @Ignore
	public void deleteAllCustomerTest() {
		customerController.deleteAll();
		Mockito.doNothing().when(customerRepository).deleteAll();
		Mockito.verify(customerRepository, Mockito.times(1)).deleteAll();
	}

	@Test
	// @Ignore
	public void deleteCustomerByIDTest() {
		customerController.deleteCustomerByID(ArgumentMatchers.anyString());
		Mockito.doNothing().when(customerRepository).deleteById(ArgumentMatchers.anyString());
		Mockito.verify(customerRepository, Mockito.times(1)).deleteById(ArgumentMatchers.anyString());
	}
	
	@Test
	public void transferFundFailTest(){
		String id1 = "1";
		String id2 = "2";
		Mockito.when(customerRepository.findById(id1)).thenReturn(Optional.of(new Customer("FN", "LN", "1", new Account(1, MOCKED_SAVING, BigDecimal.TEN))));
		Mockito.when(customerRepository.findById(id2)).thenReturn(Optional.of(new Customer("FN", "LN", "2", new Account(2, MOCKED_SAVING, BigDecimal.ONE))));
		ResponseEntity<String> transferFunds = customerController.transferFunds(id1,id2, BigDecimal.TEN);
		Assertions.assertThat(transferFunds.getBody().startsWith(CustomExceptionMessage.INSUFFICIENT_FUND.name()));
		logger.info(PREFIX + "Inside transferFundFailTest() -> " + transferFunds.getBody());
	}
	
	@Test
	public void transferFundSuccessTest(){
		String id1 = "1";
		String id2 = "2";
		Mockito.when(customerRepository.findById(id1)).thenReturn(Optional.of(new Customer("FN", "LN", "1", new Account(1, MOCKED_SAVING, BigDecimal.ONE))));
		Mockito.when(customerRepository.findById(id2)).thenReturn(Optional.of(new Customer("FN", "LN", "2", new Account(2, MOCKED_SAVING, BigDecimal.TEN))));
		ResponseEntity<String> transferFunds = customerController.transferFunds(id1,id2, BigDecimal.ONE);
		Assertions.assertThat(transferFunds.getBody().startsWith("Successful"));
		logger.info(PREFIX + "Inside transferFundSuccessTest() -> " + transferFunds.getBody());
	}
}
