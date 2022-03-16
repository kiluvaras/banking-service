package ee.priit.pall.tuum.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ee.priit.pall.tuum.dto.AccountCreateRequest;
import ee.priit.pall.tuum.dto.AccountCreateResponse;
import ee.priit.pall.tuum.dto.AccountResponse;
import ee.priit.pall.tuum.dto.BalanceResponse;
import ee.priit.pall.tuum.dto.mapper.AccountMapper;
import ee.priit.pall.tuum.dto.mapper.AccountMapperImpl;
import ee.priit.pall.tuum.dto.mapper.BalanceMapper;
import ee.priit.pall.tuum.dto.mapper.BalanceMapperImpl;
import ee.priit.pall.tuum.entity.Account;
import ee.priit.pall.tuum.entity.Balance;
import ee.priit.pall.tuum.entity.Currency;
import ee.priit.pall.tuum.repository.AccountRepository;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class AccountServiceTest {

    private final long ID = 3L;
    private final String ISO_CODE = "EUR";
    private final String COUNTRY = "Germany";
    private final long CUSTOMER_ID = 13;
    private final long NEW_BALANCE_AMOUNT = 0;

    @MockBean private AccountRepository repository;
    @MockBean private CurrencyService currencyService;
    @MockBean private BalanceService balanceService;
    private BalanceMapper balanceMapper;
    private AccountMapper accountMapper;
    private AccountService service;

    @BeforeEach
    void setup() {
        balanceMapper = new BalanceMapperImpl();
        accountMapper = new AccountMapperImpl(balanceMapper);
        service = new AccountService(repository, accountMapper, currencyService, balanceService);
    }

    @Test
    void createAccount_unsupportedCurrency_throwsException() {
        AccountCreateRequest request = new AccountCreateRequest();
        request.setCountry(COUNTRY);
        request.setCustomerId(CUSTOMER_ID);
        request.setCurrencyCodes(List.of(ISO_CODE));
        when(currencyService.isCurrencySupported(ISO_CODE)).thenReturn(false);

        assertThatThrownBy(() -> service.createAccount(request))
          .isExactlyInstanceOf(RuntimeException.class)
          .hasMessage("UNSUPPORTED_CURRENCY");
    }

    @Test
    void createAccount_failsToSaveAccount_throwsException() {
        AccountCreateRequest request = new AccountCreateRequest();
        request.setCountry(COUNTRY);
        request.setCustomerId(CUSTOMER_ID);
        request.setCurrencyCodes(List.of(ISO_CODE));
        when(currencyService.getCurrencyCodes()).thenReturn(List.of(ISO_CODE));
        when(repository.save(any(Account.class))).thenReturn(0);

        assertThatThrownBy(() -> service.createAccount(request))
          .isExactlyInstanceOf(RuntimeException.class)
          .hasMessage("ACCOUNT_SAVE_FAILED");
    }

    @Test
    void createAccount_validInput_createsAccountAndBalances() {
        Balance balance = new Balance();
        balance.setAmount(NEW_BALANCE_AMOUNT);
        balance.setCurrency(Currency.builder().isoCode(ISO_CODE).build());
        AccountCreateRequest request = new AccountCreateRequest();
        request.setCountry(COUNTRY);
        request.setCustomerId(CUSTOMER_ID);
        request.setCurrencyCodes(List.of(ISO_CODE));
        when(currencyService.getCurrencyCodes()).thenReturn(List.of(ISO_CODE));
        when(repository.save(any(Account.class))).thenReturn(1);
        when(balanceService.getBalances(any())).thenReturn(List.of(balance));

        AccountCreateResponse result = service.createAccount(request);

        assertThat(result)
          .isNotNull()
          .extracting("customerId")
          .contains(CUSTOMER_ID);
        assertThat(result.getBalances())
          .isNotEmpty()
          .hasSize(1)
          .extracting(BalanceResponse::getCurrencyCode, BalanceResponse::getAmount)
          .containsExactlyInAnyOrder(tuple(ISO_CODE, NEW_BALANCE_AMOUNT));
    }

    @Test
    void findById_accountNotFound_throwsException() {
        when(repository.findById(ID)).thenReturn(null);

        assertThatThrownBy(() -> service.findById(ID))
          .isExactlyInstanceOf(RuntimeException.class)
          .hasMessage("Account not found with id: " + ID);
    }

    @Test
    void findById_accountFound_returnsAccount() {
        Account account = Account.builder()
          .id(ID)
          .country(COUNTRY)
          .customerId(CUSTOMER_ID)
          .balances(Collections.emptyList())
          .build();
        when(repository.findById(ID)).thenReturn(account);

        AccountResponse result = service.findById(ID);

        verify(repository).findById(ID);
        assertThat(result)
          .isNotNull()
          .extracting("id", "customerId")
          .contains(ID,  CUSTOMER_ID);
    }

    @Test
    void areCurrenciesSupported_inputContainsUnsupportedCurrency_returnsFalse() {
        String currencyToBeChecked = "EUR";
        when(currencyService.getCurrencyCodes()).thenReturn(List.of("USD", "SEK"));

        boolean result = service.areCurrenciesSupported(List.of(currencyToBeChecked));

        assertThat(result).isFalse();
    }

    @Test
    void areCurrenciesSupported_inputContainsSupportedCurrency_returnsTrue() {
        String currencyToBeChecked = "EUR";
        when(currencyService.getCurrencyCodes()).thenReturn(List.of("EUR"));

        boolean result = service.areCurrenciesSupported(List.of(currencyToBeChecked));

        assertThat(result).isTrue();
    }
}
