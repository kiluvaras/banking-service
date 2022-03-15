package ee.priit.pall.tuum.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ee.priit.pall.tuum.dto.AccountCreateRequest;
import ee.priit.pall.tuum.entity.Account;
import ee.priit.pall.tuum.entity.Balance;
import ee.priit.pall.tuum.repository.AccountRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class AccountServiceTest {

    private final long ID = 3L;
    private final long CURRENCY_ID = 1L;
    private final String CURRENCY_NAME = "Euro";
    private final String ISO_CODE = "EUR";
    private final String COUNTRY = "Germany";
    private final long CUSTOMER_ID = 13;

    @MockBean private AccountRepository mapper;
    @MockBean private CurrencyService currencyService;
    @MockBean private BalanceService balanceService;
    private AccountService service;

    @BeforeEach
    void setup() {
        service = new AccountService(mapper, mapper, currencyService, balanceService);
    }

    @Test
    void createAccount_unsupportedCurrency_throwsException() {
        AccountCreateRequest request = new AccountCreateRequest();
        request.setCountry(COUNTRY);
        request.setCustomerId(CUSTOMER_ID);
        request.setCurrencies(List.of(ISO_CODE));
        when(currencyService.isCurrencySupported(CURRENCY_ID)).thenReturn(false);

        assertThatThrownBy(() -> service.createAccount(request))
          .isExactlyInstanceOf(RuntimeException.class)
          .hasMessage("UNSUPPORTED_CURRENCY");
    }

    @Test
    void createAccount_failsToSaveAccount_throwsException() {
        AccountCreateRequest request = new AccountCreateRequest();
        request.setCountry(COUNTRY);
        request.setCustomerId(CUSTOMER_ID);
        request.setCurrencies(List.of(ISO_CODE));
        when(currencyService.getCurrencyCodes()).thenReturn(List.of(ISO_CODE));
        when(mapper.save(any(Account.class))).thenReturn(0);

        assertThatThrownBy(() -> service.createAccount(request))
          .isExactlyInstanceOf(RuntimeException.class)
          .hasMessage("ACCOUNT_SAVE_FAILED");
    }

    @Test
    void createAccount_validInput_createsAccountAndBalances() {
        Balance balance = new Balance();
        AccountCreateRequest request = new AccountCreateRequest();
        request.setCountry(COUNTRY);
        request.setCustomerId(CUSTOMER_ID);
        request.setCurrencies(List.of(ISO_CODE));
        when(currencyService.getCurrencyCodes()).thenReturn(List.of(ISO_CODE));
        when(mapper.save(any(Account.class))).thenReturn(1);
        when(balanceService.getBalances(any())).thenReturn(List.of(balance));

        Account account = service.createAccount(request);

        assertThat(account)
          .isNotNull()
          .extracting("customerId", "country", "balances")
          .contains(CUSTOMER_ID, COUNTRY, List.of(balance));
    }

    @Test
    void findById_accountNotFound_throwsException() {
        when(mapper.findById(ID)).thenReturn(null);

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
          .balances(new ArrayList<>())
          .build();
        when(mapper.findById(ID)).thenReturn(account);

        Account result = service.findById(ID);

        verify(mapper).findById(ID);
        assertThat(result)
          .isNotNull()
          .extracting("id", "country", "customerId")
          .contains(ID, COUNTRY, CUSTOMER_ID);
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
