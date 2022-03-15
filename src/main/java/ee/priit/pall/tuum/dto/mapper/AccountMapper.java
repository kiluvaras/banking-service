package ee.priit.pall.tuum.dto.mapper;

import ee.priit.pall.tuum.dto.AccountCreateRequest;
import ee.priit.pall.tuum.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface AccountMapper {
    Account createRequestToAccount(AccountCreateRequest request);
}
