package ee.priit.pall.tuum.dto;

import ee.priit.pall.tuum.entity.Direction;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TransactionResponse {

    private Long accountId;
    private Long id;
    private Long amount;
    private String currencyCode;
    private Direction direction;
    private String description;
}
