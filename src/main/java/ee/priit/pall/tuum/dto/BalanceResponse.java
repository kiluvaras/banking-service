package ee.priit.pall.tuum.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BalanceResponse {

    private Long amount;
    private String currencyCode;
}
