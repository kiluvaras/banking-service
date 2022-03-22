package ee.priit.pall.tuum.dto.mapper;

import ee.priit.pall.tuum.dto.TransactionCreateRequest;
import ee.priit.pall.tuum.dto.TransactionCreateResponse;
import ee.priit.pall.tuum.dto.TransactionResponse;
import ee.priit.pall.tuum.entity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface TransactionMapper {

    @Mapping(source = "currencyCode", target = "currency.isoCode")
    Transaction transactionCreateRequestToTransaction(TransactionCreateRequest request);

    @Mapping(source = "transaction.currency.isoCode", target = "currencyCode")
    TransactionCreateResponse toTransactionCreateResponse(Transaction transaction);

    @Mapping(source = "transaction.currency.isoCode", target = "currencyCode")
    TransactionResponse toTransactionResponse(Transaction transaction);
}
