package ee.priit.pall.tuum.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CurrencyResponse {
    private Long id;
    private String name;
    private String isoCode;
}
