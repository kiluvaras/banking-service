package ee.priit.pall.tuum.dto.mapper;

import ee.priit.pall.tuum.dto.BalanceResponse;
import ee.priit.pall.tuum.entity.Balance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface BalanceMapper {

    @Mapping(source = "currency.isoCode", target = "currencyCode")
    BalanceResponse balanceToBalanceResponse(Balance balance);
}
