package com.isenkiv.interview.assignmentcontroller;

import com.senkiv.interview.assignment.Application;
import com.senkiv.interview.assignment.domain.dto.AuthorizeTransactionRequestDTO;
import com.senkiv.interview.assignment.domain.dto.CancelTransactionRequestDTO;
import com.senkiv.interview.assignment.domain.dto.TransferTransactionRequestDTO;
import com.senkiv.interview.assignment.domain.dto.VerifyUserRequestDTO;
import com.senkiv.interview.assignment.domain.entity.Transaction;
import com.senkiv.interview.assignment.domain.entity.User;
import com.senkiv.interview.assignment.repository.TransactionRepository;
import com.senkiv.interview.assignment.repository.UserRepository;
import com.senkiv.interview.assignment.service.impl.CurrencyConverterHelper;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * @author isenkiv
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@FixMethodOrder(MethodSorters.JVM)
public class ControllerTest {
	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(),
			Charset.forName("utf8"));

	private MockMvc mockMvc;

	private HttpMessageConverter mappingJackson2HttpMessageConverter;

	@Autowired
	private WebApplicationContext webApplicationContext;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	void setConverters(HttpMessageConverter<?>[] converters) {

		this.mappingJackson2HttpMessageConverter = Arrays.stream(converters).filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
				.findAny().orElse(null);

		assertNotNull("the JSON message converter must not be null", this.mappingJackson2HttpMessageConverter);
	}

	@Test
	public void verifyUserNotFound() throws Exception {
		VerifyUserRequestDTO verifyUserRequestDTO = new VerifyUserRequestDTO();
		verifyUserRequestDTO.setSessionId("123");
		verifyUserRequestDTO.setUserId("12");
		mockMvc.perform(post("/api/verifyuser/").content(this.json(verifyUserRequestDTO)).contentType(contentType)).andExpect(status().isOk())
				.andExpect(content().json("{\n" + "    \"userId\": \"12\",\n" + "    \"success\": false,\n" + "    \"userCat\": null,\n"
						+ "    \"kycStatus\": null,\n" + "    \"sex\": null,\n" + "    \"firstName\": null,\n" + "    \"lastName\": null,\n"
						+ "    \"street\": null,\n" + "    \"city\": null,\n" + "    \"zip\": null,\n" + "    \"country\": null,\n"
						+ "    \"email\": null,\n" + "    \"dob\": null,\n" + "    \"mobile\": null,\n" + "    \"balance\": null,\n"
						+ "    \"balanceCy\": null,\n" + "    \"locale\": null,\n" + "    \"errCode\": 1,\n"
						+ "    \"errMsg\": \"Unknown userId\"\n" + "}"));
	}

	@Test
	public void verifyUserFound() throws Exception {
		VerifyUserRequestDTO verifyUserRequestDTO = new VerifyUserRequestDTO();
		verifyUserRequestDTO.setSessionId("123");
		verifyUserRequestDTO.setUserId("1");
		mockMvc.perform(post("/api/verifyuser/").content(this.json(verifyUserRequestDTO)).contentType(contentType)).andExpect(status().isOk())
				.andExpect(content().json("{\n" + "    \"userId\": \"1\",\n" + "    \"success\": true,\n" + "    \"userCat\": \"Regular\",\n"
						+ "    \"kycStatus\": \"Approved\",\n" + "    \"sex\": \"MALE\",\n" + "    \"firstName\": \"John\",\n"
						+ "    \"lastName\": \"Johnson\",\n" + "    \"street\": \"12 str.\",\n" + "    \"city\": \"Lviv\",\n"
						+ "    \"zip\": \"77777\",\n" + "    \"country\": \"Ukraine\",\n" + "    \"email\": \"johnson@gmail.com\",\n"
						+ "    \"dob\": \"1990-01-01\",\n" + "    \"mobile\": \"12344321\",\n" + "    \"balance\": 10000,\n"
						+ "    \"balanceCy\": \"UAH\",\n" + "    \"locale\": \"en_US\",\n" + "    \"errCode\": null,\n"
						+ "    \"errMsg\": null\n" + "}"));
	}

	@Test
	public void authorizeTransactionNotValidUser() throws Exception {
		AuthorizeTransactionRequestDTO authorizeTransactionRequestDTO = createAuthorizeRequestDTO("1", "12", "50.50", "EUR");
		mockMvc.perform(post("/api/authorize/").content(this.json(authorizeTransactionRequestDTO)).contentType(contentType))
				.andExpect(status().isOk()).andExpect(content()
				.json("{\n" + "    \"userId\": \"12\",\n" + "    \"success\": false,\n" + "    \"txId\": \"1\",\n" + "    \"merchantTxId\": \""
						+ transactionRepository.findByTxId("1").getId() + "\",\n" + "    \"authCode\": \"" + transactionRepository
						.findByTxId("1").getAuthCode() + "\",\n" + "    \"errCode\": 1,\n" + "    \"errMsg\": \"Unknown userId\"\n" + "}"));
	}

	@Test
	public void authorizeTransactionSuccess() throws Exception {
		transactionRepository.deleteAllInBatch();

		User user = userRepository.findOne("3");
		BigDecimal userBalanceBeforeTx = user.getBalance();

		AuthorizeTransactionRequestDTO authorizeTransactionRequestDTO = createAuthorizeRequestDTO("2", "3", "1", "EUR");
		mockMvc.perform(post("/api/authorize/").content(this.json(authorizeTransactionRequestDTO)).contentType(contentType))
				.andExpect(status().isOk()).andExpect(content()
				.json("{\n" + "    \"userId\": \"3\",\n" + "    \"success\": true,\n" + "    \"txId\": \"2\",\n" + "    \"merchantTxId\": \""
						+ transactionRepository.findByTxId("2").getId() + "\",\n" + "    \"authCode\": \"" + transactionRepository
						.findByTxId("2").getAuthCode() + "\",\n" + "    \"errCode\": null,\n" + "    \"errMsg\": null\n" + "}"));
		//check user balance hasn't changed
		assertEquals(userBalanceBeforeTx, userRepository.findOne("3").getBalance());
	}

	@Test
	public void authorizeTransactionOverCreditUser() throws Exception {
		transactionRepository.deleteAllInBatch();

		User user = userRepository.findOne("2");
		BigDecimal userBalanceBeforeTx = user.getBalance();

		// create authorized transactions
		Transaction transaction1 = new Transaction();
		transaction1.setTransactionStatus(Transaction.TransactionStatus.PENDING);
		transaction1.setTxId("added1");
		transaction1.setTxAmount(new BigDecimal(-1200));
		transaction1.setTxAmountCy("EUR");
		transaction1.setAuthCode(UUID.randomUUID().toString());
		transaction1.setUser(userRepository.findOne("2"));

		Transaction transaction2 = new Transaction();
		transaction2.setTransactionStatus(Transaction.TransactionStatus.PENDING);
		transaction2.setTxId("added2");
		transaction2.setTxAmount(new BigDecimal(-500));
		transaction2.setTxAmountCy("EUR");
		transaction2.setAuthCode(UUID.randomUUID().toString());
		transaction2.setUser(userRepository.findOne("2"));

		Transaction transaction3 = new Transaction();
		transaction3.setTransactionStatus(Transaction.TransactionStatus.PENDING);
		transaction3.setTxId("added3");
		transaction3.setTxAmount(new BigDecimal(1200));
		transaction3.setTxAmountCy("EUR");
		transaction3.setAuthCode(UUID.randomUUID().toString());
		transaction3.setUser(userRepository.findOne("2"));

		transactionRepository.save(Arrays.asList(transaction1, transaction2, transaction3));
		// should be locked 1700 USD for user with userId=2, available amount left 300 EUR

		//try to authorize transaction to lock 400 EUR
		AuthorizeTransactionRequestDTO authorizeTransactionRequestDTO = createAuthorizeRequestDTO("3", "2", "-400", "EUR");
		mockMvc.perform(post("/api/authorize/").content(this.json(authorizeTransactionRequestDTO)).contentType(contentType))
				.andExpect(status().isOk()).andExpect(content()
				.json("{\n" + "    \"userId\": \"2\",\n" + "    \"success\": false,\n" + "    \"txId\": \"3\",\n" + "    \"merchantTxId\": \""
						+ transactionRepository.findByTxId("3").getId() + "\",\n" + "    \"authCode\":" + transactionRepository.findByTxId("3")
						.getAuthCode() + ",\n" + "    \"errCode\": 2,\n" + "   \"errMsg\": \"Insufficient funds on user's account\"\n" + "}"));

		//try to authorize transaction with positive amount should be successful
		AuthorizeTransactionRequestDTO authorizePositiveTransactionRequestDTO = createAuthorizeRequestDTO("4", "2", "500", "EUR");
		mockMvc.perform(post("/api/authorize/").content(this.json(authorizePositiveTransactionRequestDTO)).contentType(contentType))
				.andExpect(status().isOk()).andExpect(content()
				.json("{\n" + "    \"userId\": \"2\",\n" + "    \"success\": true,\n" + "    \"txId\": \"4\",\n" + "    \"merchantTxId\": \""
						+ transactionRepository.findByTxId("4").getId() + "\",\n" + "    \"authCode\":" + transactionRepository.findByTxId("4")
						.getAuthCode() + ",\n" + "    \"errCode\": null,\n" + "    \"errMsg\": null\n" + "}"));
		//check user balance hasn't changed
		assertEquals(userBalanceBeforeTx, userRepository.findOne("2").getBalance());
	}

	@Test
	public void transferNotAuthorizedTransactionSuccess() throws Exception {
		transactionRepository.deleteAllInBatch();

		User user = userRepository.findOne("1");
		BigDecimal userBalanceBeforeTx = user.getBalance();
		TransferTransactionRequestDTO transferTransactionRequestDTO = createTransferTransactionRequestDTO("1", null, "100", "USD", "0.5", "EUR",
				"ADD", "successNotAuthorized1");
		mockMvc.perform(post("/api/transfer/").content(this.json(transferTransactionRequestDTO)).contentType(contentType))
				.andExpect(status().isOk()).andExpect(content()
				.json("{\n" + "    \"userId\": \"1\",\n" + "    \"success\": true,\n" + "    \"txId\": \"successNotAuthorized1\",\n"
						+ "    \"merchantTxId\": \"" + transactionRepository.findByTxId("successNotAuthorized1").getId() + "\",\n"
						+ "    \"errCode\": null,\n" + "    \"errMsg\": null" + "}"));
		//check user balance changed (feeMode ="ADD" so fee is added to txAmount)
		// Note: user balanceCy is "UAH", txAmountCy is USD
		BigDecimal expected = CurrencyConverterHelper.convert(transferTransactionRequestDTO.getTxAmountCy(), user.getBalanceCy(),
				new BigDecimal(transferTransactionRequestDTO.getTxAmount())).add(userBalanceBeforeTx).setScale(2, BigDecimal.ROUND_UP);
		assertEquals(expected, userRepository.findOne("1").getBalance());
	}

	@Test
	public void transferCreditTransactionOverCreditUserSuccess() throws Exception {
		transactionRepository.deleteAllInBatch();

		User user = userRepository.findOne("3");
		BigDecimal userBalanceBeforeTx = user.getBalance();
		TransferTransactionRequestDTO transferTransactionRequestDTO = createTransferTransactionRequestDTO("3", null, "-100000", "USD", "0.5",
				"EUR", "DEDUCT", "successTransferOverCredit");
		mockMvc.perform(post("/api/transfer/").content(this.json(transferTransactionRequestDTO)).contentType(contentType))
				.andExpect(status().isOk()).andExpect(content()
				.json("{\n" + "    \"userId\": \"3\",\n" + "    \"success\": true,\n" + "    \"txId\": \"successTransferOverCredit\",\n"
						+ "    \"merchantTxId\": \"" + transactionRepository.findByTxId("successTransferOverCredit").getId() + "\",\n"
						+ "    \"errCode\": null,\n" + "    \"errMsg\": null" + "}"));
		//check user balance changed (feeMode ="DEDUCT" so fee should be charged separately),
		// Note: user balanceCy is "UAH", txAmountCy is USD, feeCy is EUR
		BigDecimal expected = CurrencyConverterHelper.convert(transferTransactionRequestDTO.getTxAmountCy(), user.getBalanceCy(),
				new BigDecimal(transferTransactionRequestDTO.getTxAmount())).add(userBalanceBeforeTx).subtract(CurrencyConverterHelper
				.convert(transferTransactionRequestDTO.getFeeCy(), user.getBalanceCy(), new BigDecimal(transferTransactionRequestDTO.getFee())))
				.setScale(2, BigDecimal.ROUND_UP);
		assertEquals(expected, userRepository.findOne("3").getBalance());
	}

	@Test
	public void transferAuthorizedTransactionUserSuccess() throws Exception {
		transactionRepository.deleteAllInBatch();

		User user = userRepository.findOne("1");
		BigDecimal userBalanceBeforeTx = user.getBalance();
		// create authorized transactions
		String authCode = UUID.randomUUID().toString();
		Transaction transaction = new Transaction();
		transaction.setTransactionStatus(Transaction.TransactionStatus.PENDING);
		transaction.setTxId("successWithAuthorized1");
		transaction.setTxAmount(new BigDecimal(-9000));
		transaction.setTxAmountCy("UAH");
		transaction.setAuthCode(authCode);
		transaction.setUser(userRepository.findOne("1"));
		transactionRepository.save(transaction);

		TransferTransactionRequestDTO transferTransactionRequestDTO = createTransferTransactionRequestDTO("1", authCode, "100", "USD", "0.5",
				"EUR", "ADD", "successWithAuthorized1");

		mockMvc.perform(post("/api/transfer/").content(this.json(transferTransactionRequestDTO)).contentType(contentType))
				.andExpect(status().isOk()).andExpect(content()
				.json("{\n" + "    \"userId\": \"1\",\n" + "    \"success\": true,\n" + "    \"txId\": \"successWithAuthorized1\",\n"
						+ "    \"merchantTxId\": \"" + transactionRepository.findByTxId("successWithAuthorized1").getId() + "\",\n"
						+ "    \"errCode\": null,\n" + "    \"errMsg\": null" + "}"));

		//check user balance changed (feeMode ="ADD" so fee is added to txAmount)
		// Note: user balanceCy is "UAH", txAmountCy is USD
		BigDecimal expected = CurrencyConverterHelper.convert(transferTransactionRequestDTO.getTxAmountCy(), user.getBalanceCy(),
				new BigDecimal(transferTransactionRequestDTO.getTxAmount())).add(userBalanceBeforeTx).setScale(2, BigDecimal.ROUND_UP);
		assertEquals(expected, userRepository.findOne("1").getBalance());
	}

	@Test
	public void cancelNotExistedTransaction() throws Exception {
		transactionRepository.deleteAllInBatch();

		CancelTransactionRequestDTO cancelTransactionRequestDTO = createCancelTransactionRequestDTO("2", "1234", "123");
		mockMvc.perform(post("/api/cancel/").content(this.json(cancelTransactionRequestDTO)).contentType(contentType)).andExpect(status().isOk())
				.andExpect(content().json("{\n" + "    \"userId\": \"2\",\n" + "    \"success\": false,\n" + "    \"errCode\": 3,\n"
						+ "    \"errMsg\": \"Transaction is not valid or has been already processed\"\n" + "}"));
	}

	@Test
	public void cancelAuthorizedTransactionSuccess() throws Exception {
		transactionRepository.deleteAllInBatch();

		// create authorized transactions
		String authCode = UUID.randomUUID().toString();
		Transaction transaction = new Transaction();
		transaction.setTransactionStatus(Transaction.TransactionStatus.PENDING);
		transaction.setTxId("cancelAuthorizedTx");
		transaction.setTxAmount(new BigDecimal(100));
		transaction.setTxAmountCy("EUR");
		transaction.setAuthCode(authCode);
		transaction.setUser(userRepository.findOne("3"));
		transactionRepository.save(transaction);

		CancelTransactionRequestDTO cancelTransactionRequestDTO = createCancelTransactionRequestDTO("3", authCode, "cancelAuthorizedTx");
		mockMvc.perform(post("/api/cancel/").content(this.json(cancelTransactionRequestDTO)).contentType(contentType)).andExpect(status().isOk())
				.andExpect(content().json("{\n" + "    \"userId\": \"3\",\n" + "    \"success\": true,\n" + "    \"errCode\": null,\n"
						+ "    \"errMsg\": null\n" + "}"));
	}

	private AuthorizeTransactionRequestDTO createAuthorizeRequestDTO(String txId, String userId, String txAmount, String txAmountCy) {
		AuthorizeTransactionRequestDTO authorizeTransactionRequestDTO = new AuthorizeTransactionRequestDTO();
		authorizeTransactionRequestDTO.setUserId(userId);
		authorizeTransactionRequestDTO.setTxAmount(txAmount);
		authorizeTransactionRequestDTO.setTxAmountCy(txAmountCy);
		authorizeTransactionRequestDTO.setTxId(txId);
		authorizeTransactionRequestDTO.setTxName("credit");
		authorizeTransactionRequestDTO.setProvider("MasterCard");
		authorizeTransactionRequestDTO.setPspService("pspService");
		authorizeTransactionRequestDTO.setOriginTxId("213123");
		authorizeTransactionRequestDTO.setAccountId(UUID.fromString("9903ED01-A73C-4874-8ABF-D2678E3AE23D"));
		authorizeTransactionRequestDTO.setAccountHolder("Holder");
		authorizeTransactionRequestDTO.setMaskedAccount("411812******2410");
		authorizeTransactionRequestDTO.setPspFee("12.50");
		authorizeTransactionRequestDTO.setPspFeeCy("EUR");
		authorizeTransactionRequestDTO.setPspFeeBase("14");
		authorizeTransactionRequestDTO.setPspFeeBaseCy("USD");
		authorizeTransactionRequestDTO.setAttributes(new User());
		return authorizeTransactionRequestDTO;
	}

	private TransferTransactionRequestDTO createTransferTransactionRequestDTO(String userId, String authCode, String txAmount, String txAmountCy,
			String fee, String feeCy, String feeMode, String txId) {
		TransferTransactionRequestDTO transferTransactionRequestDTO = new TransferTransactionRequestDTO();
		transferTransactionRequestDTO.setUserId(userId);
		transferTransactionRequestDTO.setAuthCode(authCode);
		transferTransactionRequestDTO.setTxAmount(txAmount);
		transferTransactionRequestDTO.setTxAmountCy(txAmountCy);
		transferTransactionRequestDTO.setTxPspAmount("100");
		transferTransactionRequestDTO.setTxPspAmountCy("USD");
		transferTransactionRequestDTO.setFee(fee);
		transferTransactionRequestDTO.setFeeCy(feeCy);
		transferTransactionRequestDTO.setFeeMode(feeMode);
		transferTransactionRequestDTO.setTxID(txId);
		transferTransactionRequestDTO.setTxTypeId(1);
		transferTransactionRequestDTO.setTxName("Tx name");
		transferTransactionRequestDTO.setProvider("provider");
		transferTransactionRequestDTO.setPspService("pspservice");
		transferTransactionRequestDTO.setTxRefId("1");
		transferTransactionRequestDTO.setOriginTxId("1");
		transferTransactionRequestDTO.setAccountId(UUID.randomUUID());
		transferTransactionRequestDTO.setAccountHolder("Holder");
		transferTransactionRequestDTO.setMaskedAccount("**********");
		transferTransactionRequestDTO.setPspFee("1");
		transferTransactionRequestDTO.setPspFeeCy("USD");
		transferTransactionRequestDTO.setPspFeeBase("1");
		transferTransactionRequestDTO.setPspFeeBaseCy("EUR");
		transferTransactionRequestDTO.setPspRefId("!");
		transferTransactionRequestDTO.setTxRefId("1");
		transferTransactionRequestDTO.setAttributes(new User());
		return transferTransactionRequestDTO;
	}

	private CancelTransactionRequestDTO createCancelTransactionRequestDTO(String userId, String authCode, String txId) {
		CancelTransactionRequestDTO cancelTransactionRequestDTO = new CancelTransactionRequestDTO();
		cancelTransactionRequestDTO.setUserId(userId);
		cancelTransactionRequestDTO.setAuthCode(authCode);
		cancelTransactionRequestDTO.setTxAmount("50");
		cancelTransactionRequestDTO.setTxAmountCy("EUR");
		cancelTransactionRequestDTO.setTxId(txId);
		cancelTransactionRequestDTO.setTxTypeId(1);
		cancelTransactionRequestDTO.setTxName("Tx name");
		cancelTransactionRequestDTO.setProvider("provider");
		cancelTransactionRequestDTO.setOriginTxId("1");
		cancelTransactionRequestDTO.setAccountId(UUID.randomUUID());
		cancelTransactionRequestDTO.setMaskedAccount("**********");
		cancelTransactionRequestDTO.setStatusCode("statuscode");
		cancelTransactionRequestDTO.setPspStatusCode("spsstatuscode");
		cancelTransactionRequestDTO.setPspFee("10");
		cancelTransactionRequestDTO.setPspFeeCy("EUR");
		cancelTransactionRequestDTO.setPspFeeBase("feebase");
		cancelTransactionRequestDTO.setPspFeeBaseCy("EUR");
		cancelTransactionRequestDTO.setPspRefId("1");
		cancelTransactionRequestDTO.setPspStatusMessage("message");
		cancelTransactionRequestDTO.setAttributes(new User());
		return cancelTransactionRequestDTO;

	}

	@Before
	public void setup() {
		this.mockMvc = webAppContextSetup(webApplicationContext).build();

		User user1 = new User();
		user1.setUserId("1");
		user1.setUserCat("Regular");
		user1.setKycStatus("Approved");
		user1.setSex(User.Sex.MALE);
		user1.setFirstName("John");
		user1.setLastName("Johnson");
		user1.setStreet("12 str.");
		user1.setCity("Lviv");
		user1.setZip("77777");
		user1.setCountry("Ukraine");
		user1.setEmail("johnson@gmail.com");
		user1.setDob("1990-01-01");
		user1.setMobile("12344321");
		user1.setBalance(new BigDecimal(10000));
		user1.setBalanceCy("UAH");
		user1.setLocale("en_US");

		User user2 = new User();
		user2.setUserId("2");
		user2.setUserCat("Regular");
		user2.setKycStatus("Approved");
		user2.setSex(User.Sex.FEMALE);
		user2.setFirstName("Cate");
		user2.setLastName("Brown");
		user2.setStreet("Amazing str.");
		user2.setCity("Berlin");
		user2.setZip("12312");
		user2.setCountry("Germany");
		user2.setEmail("brown@gmail.com");
		user2.setDob("1978-03-05");
		user2.setMobile("67437826437");
		user2.setBalance(new BigDecimal(2000));
		user2.setBalanceCy("EUR");
		user2.setLocale("de_DE");

		User user3 = new User();
		user3.setUserId("3");
		user3.setUserCat("VIP");
		user3.setKycStatus("Approved");
		user3.setSex(User.Sex.UNKNOWN);
		user3.setFirstName("Bob");
		user3.setLastName("Muller");
		user3.setStreet("Yellow str.");
		user3.setCity("London");
		user3.setZip("34567");
		user3.setCountry("UK");
		user3.setEmail("muller@gmail.com");
		user3.setDob("1989-07-06");
		user3.setMobile("677654322");
		user3.setBalance(new BigDecimal(100));
		user3.setBalanceCy("USD");
		user3.setLocale("en_GB");

		userRepository.save(Arrays.asList(user1, user2, user3));
	}

	private String json(Object o) throws IOException {
		MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
		this.mappingJackson2HttpMessageConverter.write(o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
		return mockHttpOutputMessage.getBodyAsString();
	}
}
