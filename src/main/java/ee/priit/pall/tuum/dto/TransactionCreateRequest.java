package ee.priit.pall.tuum.dto;

import ee.priit.pall.tuum.entity.Currency;
import ee.priit.pall.tuum.entity.Direction;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionCreateRequest {

    @NotNull(message = "ACCOUNT_ID_NOT_FOUND")
    private Long accountId;

    @NotNull(message = "DIRECTION_NOT_FOUND")
    private Direction direction;

    @NotNull(message = "AMOUNT_NOT_FOUND")
    @Positive(message = "AMOUNT_MUST_BE_POSITIVE")
    private Long amount;

    @NotNull(message = "CURRENCY_NOT_FOUND")
    private CurrencyRequest currency;

    @NotNull(message = "DESCRIPTION_NOT_FOUND")
    @NotEmpty(message = "DESCRIPTION_MUST_NOT_BE_EMPTY")
    private String description;
}
