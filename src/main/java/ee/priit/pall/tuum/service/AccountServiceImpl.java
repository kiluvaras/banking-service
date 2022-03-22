package ee.priit.pall.tuum.service;

import ee.priit.pall.tuum.controller.exception.ApplicationException;
import ee.priit.pall.tuum.controller.exception.ErrorCode;
import ee.priit.pall.tuum.dto.AccountCreateRequest;
import ee.priit.pall.tuum.dto.AccountCreateResponse;
import ee.priit.pall.tuum.dto.AccountResponse;
import ee.priit.pall.tuum.dto.mapper.AccountMapper;
import ee.priit.pall.tuum.entity.Account;
import ee.priit.pall.tuum.entity.Balance;
import ee.priit.pall.tuum.rabbit.RabbitMqProducer;
import ee.priit.pall.tuum.repository.AccountRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository repository;
    private final AccountMapper mapper;
    private final CurrencyServiceImpl currencyService;
    private final BalanceServiceImpl balanceService;
    private final RabbitMqProducer producer;

    public AccountServiceImpl(AccountRepository repository,
      AccountMapper mapper, CurrencyServiceImpl currencyService,
      BalanceServiceImpl balanceService, RabbitMqProducer producer) {
        this.repository = repository;
        this.mapper = mapper;
        this.currencyService = currencyService;
        this.balanceService = balanceService;
        this.producer = producer;
    }

    public AccountCreateResponse createAccount(AccountCreateRequest request) {
        if (!areCurrenciesSupported(request.getCurrencyCodes())) {
            throw new ApplicationException(ErrorCode.VALIDATION_ERROR, "UNSUPPORTED_CURRENCY");
        }

        Account account = mapper.createRequestToAccount(request);
        int saved = repository.save(account);

        if (saved != 1) {
            throw new ApplicationException(ErrorCode.SERVER_RUNTIME_ERROR, "ACCOUNT_SAVE_FAILED");
        }

        Long accountId = account.getId();

        for (String currency : request.getCurrencyCodes()) {
            balanceService.createBalance(accountId, currency);
        }

        List<Balance> balances = balanceService.getBalances(accountId);
        account.setBalances(balances);
        AccountCreateResponse response = mapper.accountToCreateResponse(account);
        producer.publish(response);

        return response;
    }

    public AccountResponse findById(long id) {
        Account account = repository.findById(id);

        if (account == null) {
            throw new ApplicationException(ErrorCode.ENTITY_NOT_FOUND,
              "Account not found with id: " + id);
        }

        return mapper.accountToResponse(account);
    }

    public boolean areCurrenciesSupported(List<String> currencyCodes) {
        List<String> supportedCurrencyCodes = currencyService.getCurrencyCodes();
        for (String currencyCode : currencyCodes) {
            if (!supportedCurrencyCodes.contains(currencyCode)) {
                return false;
            }
        }
        return true;
    }
}
