package ee.priit.pall.tuum.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import ee.priit.pall.tuum.dto.BalanceResponse;
import ee.priit.pall.tuum.dto.mapper.BalanceMapper;
import ee.priit.pall.tuum.dto.mapper.BalanceMapperImpl;
import ee.priit.pall.tuum.entity.Balance;
import ee.priit.pall.tuum.entity.Currency;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class BalanceMapperTest {
    private static final long ID =  13;
    private static final long AMOUNT = 6700;
    private static final long ACCOUNT_ID = 3;
    private static final long CURRENCY_ID = 1;
    private static final String NAME = "Euro";
    private static final String ISO_CODE = "EUR";
    private BalanceMapper mapper;

    @BeforeEach
    void setup() {
        mapper = new BalanceMapperImpl();
    }

    @Test
    void balanceToBalanceResponse_validInput_mapsAllFields() {
        Currency currency = Currency.builder()
          .id(CURRENCY_ID)
          .name(NAME)
          .isoCode(ISO_CODE)
          .build();
        Balance balance = new Balance();
        balance.setId(ID);
        balance.setAmount(AMOUNT);
        balance.setAccountId(ACCOUNT_ID);
        balance.setCurrency(currency);

        BalanceResponse result = mapper.balanceToBalanceResponse(balance);

        assertThat(result)
          .isNotNull()
          .extracting("amount", "currencyCode")
          .contains(AMOUNT, ISO_CODE);
    }
}
