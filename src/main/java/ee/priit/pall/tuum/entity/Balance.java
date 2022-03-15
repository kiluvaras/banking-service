package ee.priit.pall.tuum.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Balance {

    private Long id;
    private Long amount;
    private Long accountId;
    private Currency currency;
}
