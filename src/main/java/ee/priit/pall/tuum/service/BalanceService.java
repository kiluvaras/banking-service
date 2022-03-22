package ee.priit.pall.tuum.service;

import ee.priit.pall.tuum.dto.BalanceResponse;
import ee.priit.pall.tuum.entity.Balance;
import ee.priit.pall.tuum.entity.Direction;
import java.util.List;

public interface BalanceService {
    void createBalance(Long accountId, String currencyCode);
    List<Balance> getBalances(Long accountId);
    BalanceResponse updateBalance(Direction direction, Long accountId, String currencyCode,
      long amount);
}
