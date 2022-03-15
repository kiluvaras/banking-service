package ee.priit.pall.tuum.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {
    private Long id;
    private Long accountId;
    private Direction direction;
    private Long amount;
    private Currency currency;
    private String description;
}
