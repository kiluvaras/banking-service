package ee.priit.pall.tuum.controller;

import ee.priit.pall.tuum.dto.AccountCreateRequest;
import ee.priit.pall.tuum.entity.Account;
import ee.priit.pall.tuum.service.AccountService;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/accounts")
public class AccountController {

    private final AccountService service;

    public AccountController(AccountService service) {
        this.service = service;
    }


    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestBody @Valid AccountCreateRequest request) {
        Account response = service.createAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccount(@PathVariable long id) {
        return ResponseEntity.ok(service.findById(id));
    }
}
