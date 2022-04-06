package ee.priit.pall.tuum.dto.mapper;

import ee.priit.pall.tuum.dto.CurrencyResponse;
import ee.priit.pall.tuum.entity.Currency;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface CurrencyMapper {

    CurrencyResponse toResponse(Currency currency);

    Currency toEntity(CurrencyResponse currency);
}
