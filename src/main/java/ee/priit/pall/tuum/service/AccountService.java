package ee.priit.pall.tuum.service;

import ee.priit.pall.tuum.dto.AccountCreateRequest;
import ee.priit.pall.tuum.dto.AccountCreateResponse;
import ee.priit.pall.tuum.dto.AccountResponse;
import java.util.List;

public interface AccountService {
    AccountCreateResponse createAccount(AccountCreateRequest request);
    AccountResponse findById(long id);
    boolean areCurrenciesSupported(List<String> currencyCodes);
}
