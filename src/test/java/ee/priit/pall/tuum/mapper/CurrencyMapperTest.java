package ee.priit.pall.tuum.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import ee.priit.pall.tuum.dto.CurrencyResponse;
import ee.priit.pall.tuum.dto.mapper.CurrencyMapper;
import ee.priit.pall.tuum.dto.mapper.CurrencyMapperImpl;
import ee.priit.pall.tuum.entity.Currency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class CurrencyMapperTest {

    private static final long ID = 219;
    private static final String NAME = "Euro";
    private static final String ISO_CODE = "EUR";
    private CurrencyMapper mapper;

    @BeforeEach
    void setup() {
        mapper = new CurrencyMapperImpl();
    }

    @Test
    void toResponse_validInput_mapsAllFields() {
        Currency currency = Currency.builder()
          .id(ID)
          .name(NAME)
          .isoCode(ISO_CODE)
          .build();

        CurrencyResponse result = mapper.toResponse(currency);

        assertThat(result)
          .extracting("id", "name", "isoCode")
          .contains(ID, NAME, ISO_CODE);
    }

    @Test
    void toEntity_validInput_mapsAllFields() {
        CurrencyResponse dto = new CurrencyResponse();
        dto.setId(ID);
        dto.setName(NAME);
        dto.setIsoCode(ISO_CODE);

        Currency result = mapper.toEntity(dto);

        assertThat(result)
          .extracting("id", "name", "isoCode")
          .contains(ID, NAME, ISO_CODE);
    }
}
