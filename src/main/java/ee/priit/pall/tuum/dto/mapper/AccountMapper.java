package ee.priit.pall.tuum.dto.mapper;

import ee.priit.pall.tuum.dto.AccountCreateRequest;
import ee.priit.pall.tuum.dto.AccountCreateResponse;
import ee.priit.pall.tuum.dto.AccountResponse;
import ee.priit.pall.tuum.entity.Account;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE, uses = {
  BalanceMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface AccountMapper {

    Account createRequestToAccount(AccountCreateRequest request);

    AccountCreateResponse accountToCreateResponse(Account account);

    AccountResponse accountToResponse(Account account);
}
