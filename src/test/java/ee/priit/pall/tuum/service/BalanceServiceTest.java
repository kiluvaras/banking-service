package ee.priit.pall.tuum.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ee.priit.pall.tuum.entity.Currency;
import ee.priit.pall.tuum.repository.BalanceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class BalanceServiceTest {

    private final Long ACCOUNT_ID = 43L;
    private final Long CURRENCY_ID = 17L;
    private final String CURRENCY_CODE = "GBP";

    @MockBean
    private BalanceRepository mapper;
    @MockBean
    private CurrencyService currencyService;
    private BalanceService service;

    @BeforeEach
    void setup() {
        service = new BalanceService(mapper, currencyService);
    }

    @Test
    void createBalance_currencyNotFound_throwsException() {
        when(currencyService.getCurrency(CURRENCY_CODE)).thenReturn(null);

        assertThatThrownBy(() -> service.createBalance(ACCOUNT_ID, CURRENCY_CODE))
          .isExactlyInstanceOf(RuntimeException.class)
          .hasMessage("Currency not found with iso_code: " + CURRENCY_CODE);
    }

    @Test
    void createBalance_validInput_createsBalance() {
        when(currencyService.getCurrency(CURRENCY_CODE)).thenReturn(Currency.builder().id(CURRENCY_ID).build());

        service.createBalance(ACCOUNT_ID, CURRENCY_CODE);

        verify(mapper).save(ACCOUNT_ID, CURRENCY_ID);
    }

    // TODO: debit & credit tests
}
