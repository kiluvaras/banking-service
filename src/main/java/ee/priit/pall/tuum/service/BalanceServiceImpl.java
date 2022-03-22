package ee.priit.pall.tuum.service;

import ee.priit.pall.tuum.controller.exception.ApplicationException;
import ee.priit.pall.tuum.controller.exception.ErrorCode;
import ee.priit.pall.tuum.dto.BalanceResponse;
import ee.priit.pall.tuum.dto.mapper.BalanceMapper;
import ee.priit.pall.tuum.entity.Balance;
import ee.priit.pall.tuum.entity.Currency;
import ee.priit.pall.tuum.entity.Direction;
import ee.priit.pall.tuum.rabbit.RabbitMqProducer;
import ee.priit.pall.tuum.repository.BalanceRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BalanceServiceImpl implements BalanceService {

    private final BalanceRepository repository;
    private final BalanceMapper mapper;
    private final CurrencyServiceImpl currencyService;
    private final RabbitMqProducer producer;

    public BalanceServiceImpl(BalanceRepository repository,
      BalanceMapper mapper, CurrencyServiceImpl currencyService,
      RabbitMqProducer producer) {
        this.repository = repository;
        this.mapper = mapper;
        this.currencyService = currencyService;
        this.producer = producer;
    }

    public void createBalance(Long accountId, String currencyCode) {
        Currency currency = currencyService.getCurrency(currencyCode);
        if (currency == null) {
            throw new ApplicationException(ErrorCode.ENTITY_NOT_FOUND,
              "Currency not found with iso_code: " + currencyCode);
        }
        repository.save(accountId, currency.getId());
        producer.publish(
          String.format("Created new balance with currency: %s for account_id: %d", currencyCode,
            accountId));
    }

    public List<Balance> getBalances(Long accountId) {
        return repository.findByAccountId(accountId);
    }

    public BalanceResponse updateBalance(Direction direction, Long accountId, String currencyCode,
      long amount) {
        Currency currency = currencyService.getCurrency(currencyCode);
        Balance balance = repository.findByAccountIdAndCurrencyId(accountId, currency.getId());
        if (balance == null) {
            throw new ApplicationException(ErrorCode.ENTITY_NOT_FOUND,
              String.format("Balance not found with account_id: %d, currency_id: %d", accountId,
                currency.getId()));
        }

        if (direction.equals(Direction.OUT)) {
            debit(balance, amount);
        } else {
            credit(balance, amount);
        }

        balance = repository.findById(balance.getId());

        BalanceResponse response = mapper.balanceToBalanceResponse(balance);
        producer.publish(response);

        return response;
    }

    private void debit(Balance balance, long amount) {
        if (balance.getAmount() < amount) {
            throw new ApplicationException(ErrorCode.VALIDATION_ERROR, "NOT_ENOUGH_FUNDS");
        }
        repository.update(balance.getId(), balance.getAmount() - amount);
    }

    private void credit(Balance balance, long amount) {
        repository.update(balance.getId(), balance.getAmount() + amount);
    }
}
