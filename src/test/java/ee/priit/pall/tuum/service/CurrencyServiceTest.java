package ee.priit.pall.tuum.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ee.priit.pall.tuum.entity.Currency;
import ee.priit.pall.tuum.repository.CurrencyRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class CurrencyServiceTest {

    private final Long ID = 1L;
    private final String SEK_ISO_CODE = "SEK";
    private final String SEK_NAME = "Swedish krona";

    @MockBean
    private CurrencyRepository mapper;
    private CurrencyService service;

    @BeforeEach
    void setup() {
        service = new CurrencyService(mapper);
    }

    @Test
    void getCurrencies_currenciesExist_returnsAllCurrencies() {
        Currency usd = Currency.builder().id(1L).name("US dollar").isoCode("USD").build();
        Currency eur = Currency.builder().id(2L).name("Euro").isoCode("EUR").build();
        List<Currency> currencies = List.of(usd, eur);
        when(mapper.findAll()).thenReturn(currencies);

        List<Currency> result = service.getCurrencies();

        verify(mapper).findAll();
        assertThat(result)
          .isNotEmpty()
          .hasSize(currencies.size())
          .containsAll(currencies);
    }

    @Test
    void isCurrencySupported_currencyDoesNotExist_returnsFalse() {
        when(mapper.findById(ID)).thenReturn(null);

        boolean result = service.isCurrencySupported(ID);

        assertThat(result).isFalse();
    }

    @Test
    void isCurrencySupported_currencyExists_returnsTrue() {
        when(mapper.findById(ID)).thenReturn(new Currency());

        boolean result = service.isCurrencySupported(ID);

        assertThat(result).isTrue();
    }

    @Test
    void getCurrencyCodes_currenciesExist_returnsAllCurrencyCodes() {
        List<String> currencies = List.of("EUR", "USD", "SEK", "GBP");
        when(mapper.findAllCurrencyCodes()).thenReturn(currencies);

        List<String> result = service.getCurrencyCodes();

        verify(mapper).findAllCurrencyCodes();
        assertThat(result)
          .isNotEmpty()
          .hasSize(currencies.size())
          .containsAll(currencies);
    }

    @Test
    void getCurrency_currencyDoesNotExist_returnsNull() {
        when(mapper.findByIsoCode(SEK_ISO_CODE)).thenReturn(null);

        Currency result = service.getCurrency(SEK_ISO_CODE);

        assertThat(result)
          .isNull();
    }

    @Test
    void getCurrency_currencyExists_returnsCurrency() {
        Currency sek = Currency.builder().id(ID).name(SEK_NAME).isoCode(SEK_ISO_CODE).build();
        when(mapper.findByIsoCode(SEK_ISO_CODE)).thenReturn(sek);

        Currency result = service.getCurrency(SEK_ISO_CODE);

        assertThat(result)
          .isNotNull()
          .extracting("id", "name", "isoCode")
          .contains(ID, SEK_NAME, SEK_ISO_CODE);
    }
}
