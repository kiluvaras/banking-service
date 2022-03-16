package ee.priit.pall.tuum.service;

import ee.priit.pall.tuum.dto.BalanceResponse;
import ee.priit.pall.tuum.dto.mapper.BalanceMapper;
import ee.priit.pall.tuum.entity.Balance;
import ee.priit.pall.tuum.entity.Currency;
import ee.priit.pall.tuum.entity.Direction;
import ee.priit.pall.tuum.repository.BalanceRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BalanceService {

    private final BalanceRepository repository;
    private final BalanceMapper mapper;
    private final CurrencyService currencyService;

    public BalanceService(BalanceRepository repository,
      BalanceMapper mapper, CurrencyService currencyService) {
        this.repository = repository;
        this.mapper = mapper;
        this.currencyService = currencyService;
    }

    public void createBalance(Long accountId, String currencyCode) {
        Currency currency = currencyService.getCurrency(currencyCode);
        if (currency == null) {
            throw new RuntimeException("Currency not found with iso_code: " + currencyCode);
        }
        repository.save(accountId, currency.getId());
    }

    public List<Balance> getBalances(Long accountId) {
        return repository.findByAccountId(accountId);
    }

    public BalanceResponse updateBalance(Direction direction, Long accountId, String currencyCode,
      long amount) {
        Currency currency = currencyService.getCurrency(currencyCode);
        Balance balance = repository.findByAccountIdAndCurrencyId(accountId, currency.getId());
        if (balance == null) {
            throw new RuntimeException("BALANCE_NOT_FOUND");
        }

        if (direction.equals(Direction.OUT)) {
            debit(balance, amount);
        } else {
            credit(balance, amount);
        }

        balance = repository.findById(balance.getId());

        return mapper.balanceToBalanceResponse(balance);
    }

    public void debit(Balance balance, long amount) {
        if (balance.getAmount() < amount) {
            throw new RuntimeException("NOT_ENOUGH_FUNDS");
        }
        repository.update(balance.getId(), balance.getAmount() - amount);
    }

    public void credit(Balance balance, long amount) {
        repository.update(balance.getId(), balance.getAmount() + amount);
    }
}
