package ee.priit.pall.tuum.controller;

import ee.priit.pall.tuum.dto.TransactionCreateRequest;
import ee.priit.pall.tuum.dto.TransactionCreateResponse;
import ee.priit.pall.tuum.entity.Transaction;
import ee.priit.pall.tuum.service.TransactionService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/transactions")
public class TransactionController {

    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TransactionCreateResponse> createTransaction(
      @RequestBody @Valid TransactionCreateRequest request) {
        TransactionCreateResponse response = service.createTransaction(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getTransactions(long accountId) {
        List<Transaction> transactions = service.getTransactions(accountId);
        return ResponseEntity.ok(transactions);
    }
}
