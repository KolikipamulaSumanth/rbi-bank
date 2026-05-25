package rbibank.web.app.controller;

import rbibank.web.app.dto.AccountDto;
import rbibank.web.app.dto.ConvertDto;
import rbibank.web.app.dto.TransferDto;
import rbibank.web.app.entity.Account;
import rbibank.web.app.entity.Transaction;
import rbibank.web.app.entity.User;
import rbibank.web.app.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority(\'EMPLOYEE\',\'ADMIN\')")
    public ResponseEntity<Account> createAccount(@RequestBody AccountDto accountDto, Authentication authentication) throws Exception {
        var user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(accountService.createAccount(accountDto, user));
    }

    @GetMapping
    public ResponseEntity<List<Account>> getUserAccounts(Authentication authentication) {
        var user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(accountService.getUserAccounts(user.getUid()));
    }

    @PostMapping("/transfer")
    public ResponseEntity<Transaction> transferFunds(@RequestBody TransferDto transferDto, Authentication authentication) throws Exception {
        var user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(accountService.transferFunds(transferDto, user));
    }

    @PostMapping("/deposit")
    @PreAuthorize("hasAnyAuthority(\'EMPLOYEE\',\'ADMIN\')")
    public ResponseEntity<Transaction> deposit(@RequestBody TransferDto transferDto, Authentication authentication) throws Exception {
        var user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(accountService.deposit(transferDto, user));
    }

    @PostMapping("/withdraw")
    @PreAuthorize("hasAnyAuthority(\'EMPLOYEE\',\'ADMIN\')")
    public ResponseEntity<Transaction> withdraw(@RequestBody TransferDto transferDto, Authentication authentication) throws Exception {
        var user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(accountService.withdraw(transferDto, user));
    }

    @GetMapping("/rates")
    public ResponseEntity<Map<String, Double>> getExchangeRate() {
        return ResponseEntity.ok(accountService.getExchangeRate());
    }

    @PostMapping("/find")
    @PreAuthorize("hasAnyAuthority(\'EMPLOYEE\',\'ADMIN\')")
    public ResponseEntity<Account> findAccount(@RequestBody TransferDto dto) {
        return ResponseEntity.ok(accountService.findAccount(dto.getCode(), dto.getRecipientAccountNumber()));
    }

    @PostMapping("/convert")
    public ResponseEntity<Transaction> convertCurrency(@RequestBody ConvertDto convertDto, Authentication authentication) throws Exception {
        var user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(accountService.convertCurrency(convertDto, user));
    }

    @java.lang.SuppressWarnings("all")
    public AccountController(final AccountService accountService) {
        this.accountService = accountService;
    }
}
