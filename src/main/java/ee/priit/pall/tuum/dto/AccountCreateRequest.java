package ee.priit.pall.tuum.dto;

import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AccountCreateRequest {

    @NotNull(message = "CUSTOMER_ID_NOT_FOUND")
    private long customerId;
    private String country;

    @NotEmpty(message = "CURRENCIES_NOT_FOUND")
    private List<String> currencies;
}
