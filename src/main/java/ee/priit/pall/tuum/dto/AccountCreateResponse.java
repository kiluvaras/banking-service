package ee.priit.pall.tuum.dto;

import ee.priit.pall.tuum.entity.Balance;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AccountCreateResponse {
    private Long id;
    private Long customerId;
    private List<Balance> balances;
}
