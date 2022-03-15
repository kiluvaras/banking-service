package ee.priit.pall.tuum.service;

import ee.priit.pall.tuum.dto.BalanceResponse;
import ee.priit.pall.tuum.dto.TransactionCreateRequest;
import ee.priit.pall.tuum.dto.TransactionCreateResponse;
import ee.priit.pall.tuum.dto.mapper.BalanceMapper;
import ee.priit.pall.tuum.dto.mapper.TransactionMapper;
import ee.priit.pall.tuum.entity.Account;
import ee.priit.pall.tuum.entity.Balance;
import ee.priit.pall.tuum.entity.Direction;
import ee.priit.pall.tuum.entity.Transaction;
import ee.priit.pall.tuum.repository.TransactionRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    private final TransactionRepository repository;
    private final TransactionMapper mapper;
    private final BalanceMapper balanceMapper;
    private final BalanceService balanceService;
    private final CurrencyService currencyService;
    private final AccountService accountService;

    public TransactionService(TransactionRepository repository,
      TransactionMapper mapper, BalanceMapper balanceMapper,
      BalanceService balanceService,
      CurrencyService currencyService,
      AccountService accountService) {
        this.repository = repository;
        this.mapper = mapper;
        this.balanceMapper = balanceMapper;
        this.balanceService = balanceService;
        this.currencyService = currencyService;
        this.accountService = accountService;
    }

    public TransactionCreateResponse createTransaction(TransactionCreateRequest request) {
        Transaction transaction = mapper.transactionCreateRequestToTransaction(request);


        if (!currencyService.isCurrencySupported(request.getCurrency().getId())) {
            throw new RuntimeException("CURRENCY_NOT_SUPPORTED");
        }

        Account account = accountService.findById(request.getAccountId());

        if (account == null) {
            throw new RuntimeException("ACCOUNT_DOES_NOT_EXIST");
        }

        Balance balance;

        if (request.getDirection().equals(Direction.OUT)) {
            balance = balanceService.debit(request.getAccountId(), request.getCurrency().getId(),
              request.getAmount());
        } else {
            balance = balanceService.credit(request.getAccountId(), request.getCurrency().getId(),
              request.getAmount());
        }


        repository.save(transaction);

        TransactionCreateResponse transactionResponse = mapper.transactionToTransactionCreateResponse(transaction);
        BalanceResponse balanceResponse = balanceMapper.balanceToBalanceResponse(balance);
        transactionResponse.setBalance(balanceResponse);

        return transactionResponse;
    }

    public List<Transaction> getTransactions(long accountId) {
        List<Transaction> transactions = repository.findByAccountId(accountId);
        return transactions;
    }
}
