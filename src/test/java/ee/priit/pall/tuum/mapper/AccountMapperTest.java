package ee.priit.pall.tuum.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import ee.priit.pall.tuum.dto.AccountCreateRequest;
import ee.priit.pall.tuum.dto.AccountCreateResponse;
import ee.priit.pall.tuum.dto.AccountResponse;
import ee.priit.pall.tuum.dto.mapper.AccountMapper;
import ee.priit.pall.tuum.dto.mapper.AccountMapperImpl;
import ee.priit.pall.tuum.dto.mapper.BalanceMapper;
import ee.priit.pall.tuum.dto.mapper.BalanceMapperImpl;
import ee.priit.pall.tuum.entity.Account;
import ee.priit.pall.tuum.entity.Balance;
import ee.priit.pall.tuum.entity.Currency;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class AccountMapperTest {

    private static final long ID = 44;
    private static final long CUSTOMER_ID = 9;
    private static final String COUNTRY = "Japan";
    private BalanceMapper balanceMapper;
    private AccountMapper mapper;
    private List<String> CURRENCY_CODES = List.of("EUR", "GBP", "SEK");

    @BeforeEach
    void setup() {
        balanceMapper = new BalanceMapperImpl();
        mapper = new AccountMapperImpl(balanceMapper);
    }

    @Test
    void createRequestToAccount_validInput_mapsFields() {
        AccountCreateRequest request = new AccountCreateRequest();
        request.setCustomerId(CUSTOMER_ID);
        request.setCountry(COUNTRY);
        request.setCurrencyCodes(CURRENCY_CODES);

        Account result = mapper.createRequestToAccount(request);

        assertThat(result)
          .isNotNull()
          .extracting("customerId", "country")
          .contains(CUSTOMER_ID, COUNTRY);
    }

    @Test
    void accountToCreateResponse_validInput_mapsALlFields() {
        List<Balance> balances = getMockBalances();
        Account account = Account.builder()
          .id(ID)
          .customerId(CUSTOMER_ID)
          .country(COUNTRY)
          .balances(balances)
          .build();

        AccountCreateResponse result = mapper.accountToCreateResponse(account);

        assertThat(result)
          .isNotNull()
          .extracting("id", "customerId")
          .contains(ID, CUSTOMER_ID);
        assertThat(result.getBalances())
          .isNotEmpty()
          .hasSize(balances.size());
    }

    @Test
    void accountToResponse_validInput_mapsAllFields() {
        List<Balance> balances = getMockBalances();
        Account account = Account.builder()
          .id(ID)
          .customerId(CUSTOMER_ID)
          .country(COUNTRY)
          .balances(balances)
          .build();

        AccountResponse result = mapper.accountToResponse(account);

        assertThat(result)
          .isNotNull()
          .extracting("id", "customerId")
          .contains(ID, CUSTOMER_ID);
        assertThat(result.getBalances())
          .isNotEmpty()
          .hasSize(balances.size());
    }

    private List<Balance> getMockBalances() {
        Balance eur = new Balance();
        eur.setId(1L);
        eur.setCurrency(Currency.builder().id(1L).name("Euro").isoCode("EUR").build());
        eur.setAmount(42000L);
        eur.setAccountId(ID);

        Balance usd = new Balance();
        eur.setId(2L);
        eur.setCurrency(
          Currency.builder().id(2L).name("United States dollar").isoCode("USD").build());
        eur.setAmount(1300L);
        eur.setAccountId(ID);

        return List.of(eur, usd);
    }

}
