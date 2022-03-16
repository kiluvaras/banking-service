package ee.priit.pall.tuum.dto;

import ee.priit.pall.tuum.entity.Direction;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TransactionCreateResponse {

    private Long id;
    private Direction direction;
    private Long accountId;
    private Long amount;
    private String currencyCode;
    private String description;
    private Long balance;
}
