package ee.priit.pall.tuum.dto.mapper;

import ee.priit.pall.tuum.dto.TransactionCreateRequest;
import ee.priit.pall.tuum.dto.TransactionCreateResponse;
import ee.priit.pall.tuum.entity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface TransactionMapper {
    Transaction transactionCreateRequestToTransaction(TransactionCreateRequest request);
    TransactionCreateResponse transactionToTransactionCreateResponse(Transaction transaction);
}
