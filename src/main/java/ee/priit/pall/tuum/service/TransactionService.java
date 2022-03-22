package ee.priit.pall.tuum.service;

import ee.priit.pall.tuum.dto.TransactionCreateRequest;
import ee.priit.pall.tuum.dto.TransactionCreateResponse;
import ee.priit.pall.tuum.dto.TransactionResponse;
import java.util.List;

public interface TransactionService {
    TransactionCreateResponse createTransaction(TransactionCreateRequest request);
    List<TransactionResponse> getTransactions(long accountId);

}
