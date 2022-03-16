package ee.priit.pall.tuum.dto;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AccountResponse {

    private Long id;
    private Long customerId;
    private List<BalanceResponse> balances;
}
