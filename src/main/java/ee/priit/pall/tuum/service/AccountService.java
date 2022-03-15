package ee.priit.pall.tuum.service;

import ee.priit.pall.tuum.dto.AccountCreateRequest;
import ee.priit.pall.tuum.dto.AccountCreateResponse;
import ee.priit.pall.tuum.dto.mapper.AccountMapper;
import ee.priit.pall.tuum.entity.Account;
import ee.priit.pall.tuum.entity.Balance;
import ee.priit.pall.tuum.repository.AccountRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private final AccountRepository repository;
    private final AccountMapper mapper;
    private final CurrencyService currencyService;
    private final BalanceService balanceService;

    public AccountService(AccountRepository repository,
      AccountMapper mapper, CurrencyService currencyService,
      BalanceService balanceService) {
        this.repository = repository;
        this.mapper = mapper;
        this.currencyService = currencyService;
        this.balanceService = balanceService;
    }

    public Account createAccount(AccountCreateRequest request) {
        if (!areCurrenciesSupported(request.getCurrencies())) {
            throw new RuntimeException("UNSUPPORTED_CURRENCY");
        }

        Account account = mapper.createRequestToAccount(request);
        int saved = repository.save(account);

        if (saved != 1) {
            throw new RuntimeException("ACCOUNT_SAVE_FAILED");
        }

        Long accountId = account.getId();

        for (String currency : request.getCurrencies()) {
            balanceService.createBalance(accountId, currency);
        }

        List<Balance> balances = balanceService.getBalances(accountId);
        account.setBalances(balances);

        AccountCreateResponse response = new AccountCreateResponse();
        response.setId(account.getId());
        response.setCustomerId(account.getCustomerId());

        return account;
    }

    public Account findById(long id) {
        Account account = repository.findById(id);

        if (account == null) {
            throw new RuntimeException("Account not found with id: " + id);
        }

        return account;
    }

    public boolean areCurrenciesSupported(List<String> currencies) {
        List<String> supportedCurrencyCodes = currencyService.getCurrencyCodes();
        for (String currencyCode : currencies) {
            if (!supportedCurrencyCodes.contains(currencyCode)) {
                return false;
            }
        }
        return true;
    }
}
