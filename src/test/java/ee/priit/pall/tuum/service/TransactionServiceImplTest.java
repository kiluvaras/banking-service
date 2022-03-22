package ee.priit.pall.tuum.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ee.priit.pall.tuum.controller.exception.ApplicationException;
import ee.priit.pall.tuum.dto.AccountResponse;
import ee.priit.pall.tuum.dto.BalanceResponse;
import ee.priit.pall.tuum.dto.CurrencyResponse;
import ee.priit.pall.tuum.dto.TransactionCreateRequest;
import ee.priit.pall.tuum.dto.TransactionCreateResponse;
import ee.priit.pall.tuum.dto.TransactionResponse;
import ee.priit.pall.tuum.dto.mapper.CurrencyMapper;
import ee.priit.pall.tuum.dto.mapper.CurrencyMapperImpl;
import ee.priit.pall.tuum.dto.mapper.TransactionMapper;
import ee.priit.pall.tuum.dto.mapper.TransactionMapperImpl;
import ee.priit.pall.tuum.entity.Currency;
import ee.priit.pall.tuum.entity.Direction;
import ee.priit.pall.tuum.entity.Transaction;
import ee.priit.pall.tuum.rabbit.RabbitMqProducer;
import ee.priit.pall.tuum.repository.TransactionRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class TransactionServiceImplTest {

    private final long ACCOUNT_ID = 3L;
    private final Direction DIRECTION_IN = Direction.IN;
    private final Direction DIRECTION_OUT = Direction.OUT;
    private final long AMOUNT = 1200L;
    private final String CURRENCY_CODE = "USD";
    private final String DESCRIPTION = "if money is the root, I want the whole damn tree";

    @MockBean
    private TransactionRepository repository;
    @MockBean
    private BalanceServiceImpl balanceService;
    @MockBean
    private CurrencyServiceImpl currencyService;
    @MockBean
    private AccountServiceImpl accountService;
    @MockBean
    private RabbitMqProducer producer;
    private TransactionMapper mapper;
    private CurrencyMapper currencyMapper;
    private TransactionServiceImpl service;

    @BeforeEach
    void setup() {
        mapper = new TransactionMapperImpl();
        currencyMapper = new CurrencyMapperImpl();
        service = new TransactionServiceImpl(repository, mapper, balanceService,
          currencyService, accountService, producer, currencyMapper);
    }

    @Test
    void createTransaction_unsupportedCurrency_throwsException() {
        TransactionCreateRequest request = new TransactionCreateRequest();
        request.setCurrencyCode(CURRENCY_CODE);
        when(currencyService.isCurrencySupported(CURRENCY_CODE)).thenReturn(false);

        assertThatThrownBy(() -> service.createTransaction(request))
          .isExactlyInstanceOf(ApplicationException.class)
          .hasMessage("CURRENCY_NOT_SUPPORTED");
    }

    @Test
    void createTransaction_accountDoesNotExist_throwsException() {
        TransactionCreateRequest request = new TransactionCreateRequest();
        request.setCurrencyCode(CURRENCY_CODE);
        request.setAccountId(ACCOUNT_ID);

        when(currencyService.isCurrencySupported(CURRENCY_CODE)).thenReturn(true);
        when(accountService.findById(ACCOUNT_ID)).thenReturn(null);

        assertThatThrownBy(() -> service.createTransaction(request))
          .isExactlyInstanceOf(ApplicationException.class)
          .hasMessage("Account not found with id: " + ACCOUNT_ID);
    }

    @Test
    void createTransaction_directionInValidInput_returnsResponse() {
        TransactionCreateRequest request = generateCreateRequest();
        request.setDirection(DIRECTION_IN);
        request.setCurrencyCode(CURRENCY_CODE);
        BalanceResponse balanceResponse = new BalanceResponse();
        balanceResponse.setCurrencyCode(CURRENCY_CODE);
        balanceResponse.setAmount(AMOUNT);
        when(currencyService.isCurrencySupported(CURRENCY_CODE)).thenReturn(true);
        when(accountService.findById(ACCOUNT_ID)).thenReturn(new AccountResponse());
        when(balanceService.updateBalance(request.getDirection(), request.getAccountId(),
          request.getCurrencyCode(), request.getAmount()))
          .thenReturn(balanceResponse);
        CurrencyResponse currency = getMockCurrencyResponse();
        when(currencyService.getCurrency(CURRENCY_CODE)).thenReturn(currency);

        TransactionCreateResponse result = service.createTransaction(request);

        assertThat(result)
          .isNotNull()
          .extracting("direction", "accountId", "amount", "currencyCode", "description", "balance")
          .contains(DIRECTION_IN, ACCOUNT_ID, AMOUNT, CURRENCY_CODE, DESCRIPTION, AMOUNT);
        verify(repository).save(any(Transaction.class));
    }

    @Test
    void createTransaction_directionOutValidInput_returnsResponse() {
        TransactionCreateRequest request = generateCreateRequest();
        request.setDirection(DIRECTION_OUT);
        request.setCurrencyCode(CURRENCY_CODE);
        BalanceResponse balanceResponse = new BalanceResponse();
        balanceResponse.setCurrencyCode(CURRENCY_CODE);
        balanceResponse.setAmount(AMOUNT);
        when(currencyService.isCurrencySupported(CURRENCY_CODE)).thenReturn(true);
        when(accountService.findById(ACCOUNT_ID)).thenReturn(new AccountResponse());
        when(balanceService.updateBalance(request.getDirection(), request.getAccountId(),
          request.getCurrencyCode(), request.getAmount()))
          .thenReturn(balanceResponse);
        CurrencyResponse currency = getMockCurrencyResponse();
        when(currencyService.getCurrency(CURRENCY_CODE)).thenReturn(currency);

        TransactionCreateResponse result = service.createTransaction(request);

        assertThat(result)
          .isNotNull()
          .extracting("direction", "accountId", "amount", "currencyCode", "description", "balance")
          .contains(DIRECTION_OUT, ACCOUNT_ID, AMOUNT, CURRENCY_CODE, DESCRIPTION, AMOUNT);
        verify(repository).save(any(Transaction.class));
    }

    @Test
    void getTransactions_transactionsExist_returnsMappedTransactions() {
        Transaction transaction = new Transaction();
        transaction.setCurrency(Currency.builder().isoCode(CURRENCY_CODE).build());
        transaction.setDescription(DESCRIPTION);
        transaction.setAmount(AMOUNT);
        transaction.setAccountId(ACCOUNT_ID);
        transaction.setDirection(DIRECTION_OUT);
        List<Transaction> transactions = List.of(transaction);
        when(accountService.findById(ACCOUNT_ID)).thenReturn(new AccountResponse());
        when(repository.findByAccountId(ACCOUNT_ID)).thenReturn(transactions);

        List<TransactionResponse> result = service.getTransactions(ACCOUNT_ID);

        assertThat(result)
          .isNotEmpty()
          .hasSize(transactions.size())
          .extracting("currencyCode", "description", "amount", "accountId", "direction")
          .containsExactly(tuple(CURRENCY_CODE, DESCRIPTION, AMOUNT, ACCOUNT_ID, DIRECTION_OUT));
    }

    private TransactionCreateRequest generateCreateRequest() {
        TransactionCreateRequest request = new TransactionCreateRequest();
        request.setAccountId(ACCOUNT_ID);
        request.setAmount(AMOUNT);
        request.setCurrencyCode(CURRENCY_CODE);
        request.setDescription(DESCRIPTION);
        return request;
    }

    private CurrencyResponse getMockCurrencyResponse() {
        CurrencyResponse currency = new CurrencyResponse();
        currency.setIsoCode(CURRENCY_CODE);
        when(currencyService.getCurrency(CURRENCY_CODE)).thenReturn(currency);
        return currency;
    }
}
