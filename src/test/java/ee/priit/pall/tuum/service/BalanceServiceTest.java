package ee.priit.pall.tuum.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ee.priit.pall.tuum.dto.BalanceResponse;
import ee.priit.pall.tuum.dto.mapper.BalanceMapper;
import ee.priit.pall.tuum.dto.mapper.BalanceMapperImpl;
import ee.priit.pall.tuum.entity.Balance;
import ee.priit.pall.tuum.entity.Currency;
import ee.priit.pall.tuum.entity.Direction;
import ee.priit.pall.tuum.repository.BalanceRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class BalanceServiceTest {

    private final long ID = 1337;
    private final Direction DIRECTION_IN = Direction.IN;
    private final Direction DIRECTION_OUT = Direction.OUT;
    private final long ACCOUNT_ID = 43;
    private final long CURRENCY_ID = 17;
    private final String CURRENCY_CODE = "GBP";
    private final long TRANSACTION_AMOUNT = 500;
    private final long BALANCE_AMOUNT_ZERO = 0;
    private final long BALANCE_AMOUNT = 1000;

    @MockBean
    private BalanceRepository repository;
    @MockBean
    private CurrencyService currencyService;
    private BalanceMapper mapper;
    private BalanceService service;

    @BeforeEach
    void setup() {
        mapper = new BalanceMapperImpl();
        service = new BalanceService(repository, mapper, currencyService);
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
        when(currencyService.getCurrency(CURRENCY_CODE)).thenReturn(
          Currency.builder().id(CURRENCY_ID).build());

        service.createBalance(ACCOUNT_ID, CURRENCY_CODE);

        verify(repository).save(ACCOUNT_ID, CURRENCY_ID);
    }

    @Test
    void getBalances_balancesExist_returnsBalances() {
        Balance balance = new Balance();
        List<Balance> balances = List.of(balance);
        when(repository.findByAccountId(ACCOUNT_ID)).thenReturn(balances);

        List<Balance> result = service.getBalances(ACCOUNT_ID);

        verify(repository).findByAccountId(ACCOUNT_ID);
        assertThat(result)
          .isNotEmpty()
          .hasSize(balances.size())
          .containsExactlyInAnyOrder(balance);
    }

    // TODO: debit & credit tests

    @Test
    void updateBalance_balanceNotFound_throwsException() {
        when(currencyService.getCurrency(CURRENCY_CODE)).thenReturn(
          Currency.builder().id(CURRENCY_ID).isoCode(CURRENCY_CODE).build());
        when(repository.findByAccountIdAndCurrencyId(ACCOUNT_ID, CURRENCY_ID)).thenReturn(null);

        assertThatThrownBy(() -> service.updateBalance(DIRECTION_IN, ACCOUNT_ID, CURRENCY_CODE,
          TRANSACTION_AMOUNT))
          .isExactlyInstanceOf(RuntimeException.class)
          .hasMessage("BALANCE_NOT_FOUND");
    }

    @Test
    void updateBalance_directionOutNotEnoughFunds_throwsException() {
        Balance balance = new Balance();
        balance.setAmount(BALANCE_AMOUNT_ZERO);
        when(currencyService.getCurrency(CURRENCY_CODE)).thenReturn(
          Currency.builder().id(CURRENCY_ID).isoCode(CURRENCY_CODE).build());
        when(repository.findByAccountIdAndCurrencyId(ACCOUNT_ID, CURRENCY_ID)).thenReturn(balance);

        assertThatThrownBy(() -> service.updateBalance(DIRECTION_OUT, ACCOUNT_ID, CURRENCY_CODE,
          TRANSACTION_AMOUNT))
          .isExactlyInstanceOf(RuntimeException.class)
          .hasMessage("NOT_ENOUGH_FUNDS");
    }

    @Test
    void updateBalance_directionInValidRequest_addsAmountToBalance() {
        Balance balance = new Balance();
        balance.setId(ID);
        balance.setAmount(BALANCE_AMOUNT);
        balance.setCurrency(Currency.builder().isoCode(CURRENCY_CODE).build());
        when(currencyService.getCurrency(CURRENCY_CODE)).thenReturn(
          Currency.builder().id(CURRENCY_ID).isoCode(CURRENCY_CODE).build());
        when(repository.findByAccountIdAndCurrencyId(ACCOUNT_ID, CURRENCY_ID)).thenReturn(balance);
        when(repository.findById(anyLong())).thenReturn(balance);

        BalanceResponse result = service.updateBalance(DIRECTION_IN, ACCOUNT_ID, CURRENCY_CODE,
          TRANSACTION_AMOUNT);

        assertThat(result)
          .isNotNull()
          .extracting("currencyCode")
          .contains(CURRENCY_CODE);
        verify(repository).update(ID, BALANCE_AMOUNT + TRANSACTION_AMOUNT);

    }

    @Test
    void updateBalance_directionOutValidRequest_withdrawsAmountFromBalance() {
        Balance balance = new Balance();
        balance.setId(ID);
        balance.setAmount(BALANCE_AMOUNT);
        balance.setCurrency(Currency.builder().isoCode(CURRENCY_CODE).build());
        when(currencyService.getCurrency(CURRENCY_CODE)).thenReturn(
          Currency.builder().id(CURRENCY_ID).isoCode(CURRENCY_CODE).build());
        when(repository.findByAccountIdAndCurrencyId(ACCOUNT_ID, CURRENCY_ID)).thenReturn(balance);
        when(repository.findById(anyLong())).thenReturn(balance);

        BalanceResponse result = service.updateBalance(DIRECTION_OUT, ACCOUNT_ID, CURRENCY_CODE,
          TRANSACTION_AMOUNT);

        assertThat(result)
          .isNotNull()
          .extracting("currencyCode")
          .contains(CURRENCY_CODE);
        verify(repository).update(ID, BALANCE_AMOUNT - TRANSACTION_AMOUNT);

    }
}
