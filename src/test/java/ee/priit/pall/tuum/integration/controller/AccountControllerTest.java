package ee.priit.pall.tuum.integration.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import ee.priit.pall.tuum.dto.AccountCreateRequest;
import ee.priit.pall.tuum.dto.AccountCreateResponse;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@MybatisTest
class AccountControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;
    private String url;

    private static final long CUSTOMER_ID = 10;
    private static final String COUNTRY = "Barbados";
    private static final List<String> CURRENCY_CODES = List.of("EUR", "USD");
    private static final long NEW_BALANCE_AMOUNT = 0;

    @BeforeEach
    void setup() {
        this.url = "http://localhost:" + port + "/rest/accounts";
    }

    @Test
    void createAccount_customerIdIsNull_returnsErrorResponse() {
        AccountCreateRequest createRequest = new AccountCreateRequest();
        createRequest.setCustomerId(null);
        createRequest.setCountry(COUNTRY);
        createRequest.setCurrencyCodes(CURRENCY_CODES);

        ResponseEntity<AccountCreateResponse> response = restTemplate.postForEntity(
          url,
          createRequest,
          AccountCreateResponse.class
        );

        assertThat(response)
          .isNotNull()
          .extracting("statusCode")
          .contains(HttpStatus.BAD_REQUEST);
    }

    @Test
    void createAccount_currenciesIsNull_returnsErrorResponse() {
        AccountCreateRequest createRequest = new AccountCreateRequest();
        createRequest.setCustomerId(CUSTOMER_ID);
        createRequest.setCountry(COUNTRY);
        createRequest.setCurrencyCodes(null);

        ResponseEntity<AccountCreateResponse> response = restTemplate.postForEntity(
          url,
          createRequest,
          AccountCreateResponse.class
        );

        assertThat(response)
          .isNotNull()
          .extracting("statusCode")
          .contains(HttpStatus.BAD_REQUEST);
    }

    @Test
    void createAccount_currenciesIsEmpty_returnsErrorResponse() {
        AccountCreateRequest createRequest = new AccountCreateRequest();
        createRequest.setCustomerId(CUSTOMER_ID);
        createRequest.setCountry(COUNTRY);
        createRequest.setCurrencyCodes(Collections.emptyList());

        ResponseEntity<AccountCreateResponse> response = restTemplate.postForEntity(
          url,
          createRequest,
          AccountCreateResponse.class
        );

        assertThat(response)
          .isNotNull()
          .extracting("statusCode")
          .contains(HttpStatus.BAD_REQUEST);
    }

    @Test
    void createAccount_validInput_returnsCreatedAccountWithBalances() {
        AccountCreateRequest createRequest = new AccountCreateRequest();
        createRequest.setCustomerId(CUSTOMER_ID);
        createRequest.setCountry(COUNTRY);
        createRequest.setCurrencyCodes(CURRENCY_CODES);

        ResponseEntity<AccountCreateResponse> response = restTemplate.postForEntity(
          url,
          createRequest,
          AccountCreateResponse.class
        );

        assertThat(response)
          .isNotNull()
          .extracting("statusCode")
          .contains(HttpStatus.CREATED);

        assertThat(response.getBody())
          .isNotNull()
          .extracting("customerId")
          .contains(CUSTOMER_ID);

        assertThat(response.getBody().getBalances())
          .isNotEmpty()
          .hasSize(CURRENCY_CODES.size())
          .extracting("currencyCode", "amount")
          .containsExactlyInAnyOrder(tuple("EUR", NEW_BALANCE_AMOUNT),
            tuple("USD", NEW_BALANCE_AMOUNT));
    }
}
