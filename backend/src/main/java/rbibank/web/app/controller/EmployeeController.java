package rbibank.web.app.controller;

import rbibank.web.app.dto.AccountDto;
import rbibank.web.app.dto.UserDto;
import rbibank.web.app.entity.Account;
import rbibank.web.app.entity.AccountStatus;
import rbibank.web.app.entity.KycStatus;
import rbibank.web.app.entity.Transaction;
import rbibank.web.app.entity.User;
import rbibank.web.app.service.AccountService;
import rbibank.web.app.service.TransactionService;
import rbibank.web.app.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/employee")
@PreAuthorize("hasAnyAuthority(\'EMPLOYEE\',\'ADMIN\')")
public class EmployeeController {
    private final UserService userService;
    private final AccountService accountService;
    private final TransactionService transactionService;

    @PostMapping("/customers")
    public ResponseEntity<User> createCustomer(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.createCustomer(userDto));
    }

    @GetMapping("/customers/search")
    public ResponseEntity<List<User>> searchCustomers(@RequestParam String q) {
        return ResponseEntity.ok(userService.searchUsers(q));
    }

    @PatchMapping("/customers/{uid}/kyc")
    public ResponseEntity<User> updateKyc(@PathVariable String uid, @RequestParam KycStatus status) {
        return ResponseEntity.ok(userService.updateKyc(uid, status));
    }

    @PatchMapping("/customers/{uid}/password")
    public ResponseEntity<User> resetPassword(@PathVariable String uid, @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(userService.resetPassword(uid, body.getOrDefault("password", "Password123")));
    }

    @PostMapping("/customers/{uid}/accounts")
    public ResponseEntity<Account> createAccountForCustomer(@PathVariable String uid, @RequestBody AccountDto accountDto) throws Exception {
        User customer = userService.getUser(uid);
        return ResponseEntity.ok(accountService.createAccount(accountDto, customer));
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<Account>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    @GetMapping("/accounts/search")
    public ResponseEntity<List<Account>> searchAccounts(@RequestParam String q) {
        return ResponseEntity.ok(accountService.searchAccounts(q));
    }

    @PatchMapping("/accounts/{accountId}/status")
    public ResponseEntity<Account> updateAccountStatus(@PathVariable String accountId, @RequestParam AccountStatus status) {
        return ResponseEntity.ok(accountService.updateStatus(accountId, status));
    }

    @DeleteMapping("/accounts/{accountId}")
    public ResponseEntity<Account> closeAccount(@PathVariable String accountId) {
        return ResponseEntity.ok(accountService.closeAccount(accountId));
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<Transaction>> transactionLogs(@RequestParam(defaultValue = "0") String page) {
        return ResponseEntity.ok(transactionService.getAllTransactionsForStaff(page));
    }

    @GetMapping("/reports/summary")
    public ResponseEntity<Map<String, Object>> summaryReport() {
        List<Account> accounts = accountService.getAllAccounts();
        double deposits = accounts.stream().mapToDouble(Account::getBalance).sum();
        long frozen = accounts.stream().filter(account -> account.getAccountStatus() == AccountStatus.FROZEN).count();
        return ResponseEntity.ok(Map.of("totalAccounts", accounts.size(), "totalCustomerBalance", deposits, "frozenAccounts", frozen, "bankName", "RBI Bank"));
    }

    @java.lang.SuppressWarnings("all")
    public EmployeeController(final UserService userService, final AccountService accountService, final TransactionService transactionService) {
        this.userService = userService;
        this.accountService = accountService;
        this.transactionService = transactionService;
    }
}
