package rbibank.web.app.service.helper;

import rbibank.web.app.dto.AccountDto;
import rbibank.web.app.dto.ConvertDto;
import rbibank.web.app.entity.*;
import rbibank.web.app.repository.AccountRepository;
import rbibank.web.app.repository.TransactionRepository;
import rbibank.web.app.service.ExchangeRateService;
import rbibank.web.app.service.TransactionService;
import rbibank.web.app.util.RandomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import javax.naming.OperationNotSupportedException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.time.LocalDateTime;

@Component
public class AccountHelper {
    private final AccountRepository accountRepository;
    private final TransactionService transactionService;
    private final ExchangeRateService exchangeRateService;
    private final Logger logger = LoggerFactory.getLogger(AccountHelper.class);
    private final Map<String, String> CURRENCIES = Map.of("USD", "United States Dollar", "EUR", "Euro", "GBP", "British Pound", "JPY", "Japanese Yen", "NGN", "Nigerian Naira", "INR", "Indian Rupee");

    public Account createAccount(AccountDto accountDto, User user) throws Exception {
        long accountNumber;
        validateAccountNonExistsForUser(accountDto.getCode(), user.getUid());
        do {
            accountNumber = new RandomUtil().generateRandom(10);
        } while (accountRepository.existsByAccountNumber(accountNumber));
        var account = Account.builder().accountNumber(accountNumber).accountName(user.getFirstname() + " " + user.getLastname()).balance(accountDto.getOpeningBalance() == null ? 1000 : accountDto.getOpeningBalance()).owner(user).code(accountDto.getCode()).symbol(accountDto.getSymbol()).label(CURRENCIES.get(accountDto.getCode())).bankName("RBI Bank").ifscCode(generateIfscCode(accountDto.getCode())).branchName("Main Branch").accountType(resolveAccountType(accountDto.getAccountType())).accountStatus(AccountStatus.ACTIVE).build();
        return accountRepository.save(account);
    }

    public Transaction performTransfer(Account senderAccount, Account receiverAccount, double amount, User user, String remarks) throws Exception {
        validateAccountCanOperate(senderAccount);
        validateAccountCanOperate(receiverAccount);
        if (senderAccount.getAccountNumber() == receiverAccount.getAccountNumber()) {
            throw new IllegalArgumentException("Self-transfer is not allowed");
        }
        validateAmount(amount);
        validateSufficientFunds(senderAccount, amount);
        senderAccount.setBalance(senderAccount.getBalance() - amount);
        receiverAccount.setBalance(receiverAccount.getBalance() + amount);
        senderAccount.setLastTransactionAt(LocalDateTime.now());
        receiverAccount.setLastTransactionAt(LocalDateTime.now());
        accountRepository.saveAll(List.of(senderAccount, receiverAccount));
        String description = (remarks == null || remarks.isBlank()) ? "Internal fund transfer" : remarks;
        var senderTransaction = transactionService.createAccountTransaction(amount, Type.DEBIT, 0.0, user, senderAccount, senderAccount.getAccountNumber(), receiverAccount.getAccountNumber(), receiverAccount.getAccountName(), description, senderAccount.getBalance());
        transactionService.createAccountTransaction(amount, Type.CREDIT, 0.0, receiverAccount.getOwner(), receiverAccount, senderAccount.getAccountNumber(), receiverAccount.getAccountNumber(), senderAccount.getAccountName(), description, receiverAccount.getBalance());
        return senderTransaction;
    }

    public Transaction deposit(Account account, double amount) throws Exception {
        validateAccountCanOperate(account);
        validateAmount(amount);
        account.setBalance(account.getBalance() + amount);
        account.setLastTransactionAt(LocalDateTime.now());
        accountRepository.save(account);
        return transactionService.createAccountTransaction(amount, Type.CREDIT, 0.0, account.getOwner(), account, null, account.getAccountNumber(), "RBI Bank Branch", "Branch cash deposit", account.getBalance());
    }

    public Transaction withdraw(Account account, double amount) throws Exception {
        validateAccountCanOperate(account);
        validateAmount(amount);
        validateSufficientFunds(account, amount);
        account.setBalance(account.getBalance() - amount);
        account.setLastTransactionAt(LocalDateTime.now());
        accountRepository.save(account);
        return transactionService.createAccountTransaction(amount, Type.DEBIT, 0.0, account.getOwner(), account, account.getAccountNumber(), null, "RBI Bank Branch", "Branch cash withdrawal", account.getBalance());
    }

    public void validateSufficientFunds(Account account, double amount) throws Exception {
        if (account.getBalance() < amount) {
            throw new OperationNotSupportedException("Insufficient funds in the account");
        }
    }

    public void validateAmount(double amount) throws Exception {
        if (amount <= 0) {
            throw new IllegalArgumentException("Invalid amount");
        }
    }

    public void validateAccountCanOperate(Account account) {
        if (account.getAccountStatus() != AccountStatus.ACTIVE) {
            throw new IllegalStateException("Account is not active");
        }
    }

    public void validateDifferentCurrencyType(ConvertDto convertDto) throws Exception {
        if (convertDto.getToCurrency().equals(convertDto.getFromCurrency())) {
            throw new IllegalArgumentException("Conversion between the same currency types is not allowed");
        }
    }

    public void validateAccountOwnership(ConvertDto convertDto, String uid) throws Exception {
        accountRepository.findFirstByCodeAndOwnerUid(convertDto.getFromCurrency(), uid).orElseThrow();
        accountRepository.findFirstByCodeAndOwnerUid(convertDto.getToCurrency(), uid).orElseThrow();
    }

    public void validateAccountOwnership(String code, String uid) throws Exception {
        accountRepository.findFirstByCodeAndOwnerUid(code, uid).orElseThrow();
    }

    public void validateConversion(ConvertDto convertDto, String uid) throws Exception {
        validateDifferentCurrencyType(convertDto);
        validateAccountOwnership(convertDto, uid);
        validateAmount(convertDto.getAmount());
        validateSufficientFunds(accountRepository.findFirstByCodeAndOwnerUid(convertDto.getFromCurrency(), uid).get(), convertDto.getAmount() * 1.01);
    }

    public Transaction convertCurrency(ConvertDto convertDto, User user) throws Exception {
        validateConversion(convertDto, user.getUid());
        var rates = exchangeRateService.getRates();
        var sendingRates = rates.get(convertDto.getFromCurrency());
        var receivingRates = rates.get(convertDto.getToCurrency());
        var computedAmount = (receivingRates / sendingRates) * convertDto.getAmount();
        Account fromAccount = accountRepository.findFirstByCodeAndOwnerUid(convertDto.getFromCurrency(), user.getUid()).orElseThrow();
        Account toAccount = accountRepository.findFirstByCodeAndOwnerUid(convertDto.getToCurrency(), user.getUid()).orElseThrow();
        fromAccount.setBalance(fromAccount.getBalance() - (convertDto.getAmount() * 1.01));
        toAccount.setBalance(toAccount.getBalance() + computedAmount);
        fromAccount.setLastTransactionAt(LocalDateTime.now());
        toAccount.setLastTransactionAt(LocalDateTime.now());
        accountRepository.saveAll(List.of(fromAccount, toAccount));
        var fromAccountTransaction = transactionService.createAccountTransaction(convertDto.getAmount(), Type.CONVERSION, convertDto.getAmount() * 0.01, user, fromAccount);
        var toAccountTransaction = transactionService.createAccountTransaction(computedAmount, Type.DEPOSIT, convertDto.getAmount() * 0.0, user, toAccount);
        return fromAccountTransaction;
    }

    public boolean existsByCodeAndOwnerUid(String code, String uid) {
        return accountRepository.existsByCodeAndOwnerUid(code, uid);
    }

    public Optional<Account> findByCodeAndOwnerUid(String code, String uid) {
        return accountRepository.findFirstByCodeAndOwnerUid(code, uid);
    }

    public Account save(Account usdAccount) {
        return accountRepository.save(usdAccount);
    }

    private AccountType resolveAccountType(String accountType) {
        if ("CURRENT".equalsIgnoreCase(accountType)) {
            return AccountType.CURRENT;
        }
        return AccountType.SAVINGS;
    }

    private String generateIfscCode(String code) {
        int suffix = new RandomUtil().generateRandom(4).intValue();
        return "RBI0" + code + suffix;
    }

    public void validateAccountNonExistsForUser(String code, String uid) {
        if (accountRepository.existsByCodeAndOwnerUid(code, uid)) {
            throw new IllegalArgumentException("Account of this type already exist for this user");
        }
    }

    @java.lang.SuppressWarnings("all")
    public AccountHelper(final AccountRepository accountRepository, final TransactionService transactionService, final ExchangeRateService exchangeRateService) {
        this.accountRepository = accountRepository;
        this.transactionService = transactionService;
        this.exchangeRateService = exchangeRateService;
    }

    @java.lang.SuppressWarnings("all")
    public AccountRepository getAccountRepository() {
        return this.accountRepository;
    }

    @java.lang.SuppressWarnings("all")
    public TransactionService getTransactionService() {
        return this.transactionService;
    }

    @java.lang.SuppressWarnings("all")
    public ExchangeRateService getExchangeRateService() {
        return this.exchangeRateService;
    }

    @java.lang.SuppressWarnings("all")
    public Logger getLogger() {
        return this.logger;
    }

    @java.lang.SuppressWarnings("all")
    public Map<String, String> getCURRENCIES() {
        return this.CURRENCIES;
    }
}
