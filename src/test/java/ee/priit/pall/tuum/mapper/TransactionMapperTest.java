package ee.priit.pall.tuum.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import ee.priit.pall.tuum.dto.TransactionCreateRequest;
import ee.priit.pall.tuum.dto.TransactionCreateResponse;
import ee.priit.pall.tuum.dto.TransactionResponse;
import ee.priit.pall.tuum.dto.mapper.TransactionMapper;
import ee.priit.pall.tuum.dto.mapper.TransactionMapperImpl;
import ee.priit.pall.tuum.entity.Currency;
import ee.priit.pall.tuum.entity.Direction;
import ee.priit.pall.tuum.entity.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class TransactionMapperTest {

    private static final long ID = 10;
    private static final long ACCOUNT_ID = 1;
    private static final Direction DIRECTION = Direction.IN;
    private static final long AMOUNT = 1000;
    private static final long CURRENCY_ID = 6;
    private static final String CURRENCY_NAME = "Swedish krone";
    private static final String CURRENCY_CODE = "SEK";
    private static final String DESCRIPTION = "birthday money";
    private TransactionMapper mapper;

    @BeforeEach
    void setup() {
        mapper = new TransactionMapperImpl();
    }

    @Test
    void transactionCreateRequestToTransaction_validInput_mapsAllFields() {
        TransactionCreateRequest request = new TransactionCreateRequest();
        request.setAccountId(ACCOUNT_ID);
        request.setDirection(DIRECTION);
        request.setAmount(AMOUNT);
        request.setCurrencyCode(CURRENCY_CODE);
        request.setDescription(DESCRIPTION);

        Transaction result = mapper.transactionCreateRequestToTransaction(request);

        assertThat(result)
          .isNotNull()
          .extracting("accountId", "direction", "amount", "description")
          .contains(ACCOUNT_ID, DIRECTION, AMOUNT, DESCRIPTION);
    }

    @Test
    void toTransactionCreateResponse_validInput_mapsAllFields() {
        Currency currency = Currency.builder()
          .id(CURRENCY_ID)
          .name(CURRENCY_NAME)
          .isoCode(CURRENCY_CODE)
          .build();
        Transaction transaction = Transaction.builder()
          .id(ID)
          .direction(DIRECTION)
          .description(DESCRIPTION)
          .amount(AMOUNT)
          .accountId(ACCOUNT_ID)
          .currency(currency)
          .build();

        TransactionCreateResponse result = mapper.toTransactionCreateResponse(
          transaction);

        assertThat(result)
          .isNotNull()
          .extracting("id", "direction", "accountId", "amount", "currencyCode", "description")
          .contains(ID, DIRECTION, ACCOUNT_ID, AMOUNT, CURRENCY_CODE, DESCRIPTION);
    }

    @Test
    void toTransactionResponse_validInput_mapsAllFields() {
        Currency currency = Currency.builder()
          .id(CURRENCY_ID)
          .name(CURRENCY_NAME)
          .isoCode(CURRENCY_CODE)
          .build();
        Transaction transaction = Transaction.builder()
          .id(ID)
          .direction(DIRECTION)
          .description(DESCRIPTION)
          .amount(AMOUNT)
          .accountId(ACCOUNT_ID)
          .currency(currency)
          .build();

        TransactionResponse result = mapper.toTransactionResponse(transaction);

        assertThat(result)
          .isNotNull()
          .extracting("id", "direction", "accountId", "amount", "currencyCode", "description")
          .contains(ID, DIRECTION, ACCOUNT_ID, AMOUNT, CURRENCY_CODE, DESCRIPTION);
    }
}
