package ee.priit.pall.tuum.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import ee.priit.pall.tuum.dto.CurrencyRequest;
import ee.priit.pall.tuum.dto.TransactionCreateRequest;
import ee.priit.pall.tuum.dto.mapper.TransactionMapper;
import ee.priit.pall.tuum.dto.mapper.TransactionMapperImpl;
import ee.priit.pall.tuum.entity.Direction;
import ee.priit.pall.tuum.entity.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith({SpringExtension.class})
class TransactionMapperTest {

    private TransactionMapper mapper;

    @BeforeEach
    void setup() {
        mapper = new TransactionMapperImpl();
    }

    @Test
    void transactionCreateRequestToTransaction_validInput_mapsAllFields() {
        CurrencyRequest currencyRequest = new CurrencyRequest();
        currencyRequest.setId(1L);
        currencyRequest.setName("Euro");
        currencyRequest.setIsoCode("EUR");

        TransactionCreateRequest request = new TransactionCreateRequest();
        request.setAccountId(1L);
        request.setAmount(500L);
//        request.setCurrency(Currency.builder().id(1L).name("Euro").isoCode("EUR").build());
        request.setCurrency(currencyRequest);
        request.setDirection(Direction.IN);
        request.setDescription("test description");

        Transaction transaction = mapper.transactionCreateRequestToTransaction(request);

        assertThat(transaction)
          .isNotNull();
    }
}
