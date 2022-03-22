package ee.priit.pall.tuum.integration.controller;

import static org.assertj.core.api.Assertions.assertThat;

import ee.priit.pall.tuum.controller.exception.ErrorCode;
import ee.priit.pall.tuum.controller.exception.RestErrorMessage;
import ee.priit.pall.tuum.dto.TransactionCreateRequest;
import ee.priit.pall.tuum.entity.Direction;
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
class TransactionControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;
    private String url;

    private final long ACCOUNT_ID = 3L;
    private final Direction DIRECTION_IN = Direction.IN;
    private final long AMOUNT = 1200L;
    private final String CURRENCY_CODE_INVALID = "gkdfn)";
    private final String DESCRIPTION = "if money is the root, I want the whole damn tree";

    @BeforeEach
    void setup() {
        this.url = "http://localhost:" + port + "/rest/transactions";
    }

    @Test
    void createTransaction_unsupportedCurrency_returnsErrorResponse() {
        TransactionCreateRequest request = new TransactionCreateRequest();
        request.setAccountId(ACCOUNT_ID);
        request.setDirection(DIRECTION_IN);
        request.setAmount(AMOUNT);
        request.setCurrencyCode(CURRENCY_CODE_INVALID);
        request.setDescription(DESCRIPTION);

        ResponseEntity<RestErrorMessage> response = restTemplate.postForEntity(
          url,
          request,
          RestErrorMessage.class
        );

        assertThat(response)
          .isNotNull()
          .extracting("statusCode")
          .contains(HttpStatus.INTERNAL_SERVER_ERROR);

        assertThat(response.getBody())
          .extracting("errorCode", "message")
          .contains(ErrorCode.VALIDATION_ERROR, "CURRENCY_NOT_SUPPORTED");
    }
}
