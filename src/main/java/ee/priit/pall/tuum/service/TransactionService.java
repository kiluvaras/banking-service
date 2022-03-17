package ee.priit.pall.tuum.service;

import ee.priit.pall.tuum.dto.AccountResponse;
import ee.priit.pall.tuum.dto.BalanceResponse;
import ee.priit.pall.tuum.dto.TransactionCreateRequest;
import ee.priit.pall.tuum.dto.TransactionCreateResponse;
import ee.priit.pall.tuum.dto.TransactionResponse;
import ee.priit.pall.tuum.dto.mapper.TransactionMapper;
import ee.priit.pall.tuum.entity.Currency;
import ee.priit.pall.tuum.entity.Transaction;
import ee.priit.pall.tuum.rabbit.RabbitMqProducer;
import ee.priit.pall.tuum.repository.TransactionRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    private final TransactionRepository repository;
    private final TransactionMapper mapper;
    private final BalanceService balanceService;
    private final CurrencyService currencyService;
    private final AccountService accountService;
    private final RabbitMqProducer producer;

    public TransactionService(TransactionRepository repository,
      TransactionMapper mapper,
      BalanceService balanceService,
      CurrencyService currencyService,
      AccountService accountService, RabbitMqProducer producer) {
        this.repository = repository;
        this.mapper = mapper;
        this.balanceService = balanceService;
        this.currencyService = currencyService;
        this.accountService = accountService;
        this.producer = producer;
    }

    public TransactionCreateResponse createTransaction(TransactionCreateRequest request) {
        validateCurrency(request.getCurrencyCode());
        validateAccountExists(request.getAccountId());

        BalanceResponse balance = balanceService.updateBalance(
          request.getDirection(),
          request.getAccountId(),
          request.getCurrencyCode(),
          request.getAmount()
        );
        Currency currency = currencyService.getCurrency(request.getCurrencyCode());
        Transaction transaction = mapper.transactionCreateRequestToTransaction(request);
        transaction.setCurrency(currency);
        repository.save(transaction);

        TransactionCreateResponse transactionResponse = mapper.transactionToTransactionCreateResponse(
          transaction);
        transactionResponse.setBalance(balance.getAmount());

        return transactionResponse;
    }

    public List<TransactionResponse> getTransactions(long accountId) {
        List<Transaction> transactions = repository.findByAccountId(accountId);
        List<TransactionResponse> response = transactions
          .stream()
          .map(mapper::toTransactionResponse)
          .toList();

        producer.produce(response);

        return response;
    }

    private void validateCurrency(String currencyCode) {
        if (!currencyService.isCurrencySupported(currencyCode)) {
            throw new RuntimeException("CURRENCY_NOT_SUPPORTED");
        }
    }

    private void validateAccountExists(long accountId) {
        AccountResponse account = accountService.findById(accountId);

        if (account == null) {
            throw new RuntimeException("Account not found with id: " + accountId);
        }
    }
}
