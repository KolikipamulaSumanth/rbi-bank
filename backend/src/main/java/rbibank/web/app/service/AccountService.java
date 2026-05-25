package rbibank.web.app.service;

import rbibank.web.app.dto.AccountDto;
import rbibank.web.app.dto.ConvertDto;
import rbibank.web.app.dto.TransferDto;
import rbibank.web.app.entity.AccountStatus;
import rbibank.web.app.entity.Account;
import rbibank.web.app.entity.Transaction;
import rbibank.web.app.entity.User;
import rbibank.web.app.repository.AccountRepository;
import rbibank.web.app.service.helper.AccountHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class AccountService {
    private final AccountRepository accountRepository;
    private final AccountHelper accountHelper;
    private final ExchangeRateService exchangeRateService;

    public Account createAccount(AccountDto accountDto, User user) throws Exception {
        return accountHelper.createAccount(accountDto, user);
    }

    public List<Account> getUserAccounts(String uid) {
        return accountRepository.findAllByOwnerUid(uid);
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public List<Account> searchAccounts(String query) {
        long tel = 0;
        try {
            tel = Long.parseLong(query);
        } catch (NumberFormatException ignored) {
        }
        List<Account> textMatches = accountRepository.findByAccountNameContainingIgnoreCaseOrOwnerPanNumberContainingIgnoreCaseOrOwnerTel(query, query, tel);
        accountRepository.findByAccountNumber(tel).ifPresent(account -> {
            if (!textMatches.contains(account)) {
                textMatches.add(account);
            }
        });
        return textMatches;
    }

    public Transaction transferFunds(TransferDto transferDto, User user) throws Exception {
        Account senderAccount = resolveOwnedAccount(transferDto, user);
        Account receiverAccount = accountRepository.findByAccountNumber(transferDto.getRecipientAccountNumber()).orElseThrow(() -> new IllegalArgumentException("Receiver account does not exist in RBI Bank"));
        return accountHelper.performTransfer(senderAccount, receiverAccount, transferDto.getAmount(), user, transferDto.getRemarks());
    }

    public Transaction deposit(TransferDto transferDto, User user) throws Exception {
        Account account = resolveOperationalAccount(transferDto, user);
        return accountHelper.deposit(account, transferDto.getAmount());
    }

    public Transaction withdraw(TransferDto transferDto, User user) throws Exception {
        Account account = resolveOperationalAccount(transferDto, user);
        return accountHelper.withdraw(account, transferDto.getAmount());
    }

    public Account updateStatus(String accountId, AccountStatus status) {
        Account account = accountRepository.findById(accountId).orElseThrow();
        account.setAccountStatus(status);
        return accountRepository.save(account);
    }

    public Account closeAccount(String accountId) {
        return updateStatus(accountId, AccountStatus.CLOSED);
    }

    public Map<String, Double> getExchangeRate() {
        return exchangeRateService.getRates();
    }

    public Transaction convertCurrency(ConvertDto convertDto, User user) throws Exception {
        return accountHelper.convertCurrency(convertDto, user);
    }

    public Account findAccount(String code, long recipientAccountNumber) {
        System.out.println("Account Number : " + recipientAccountNumber);
        System.out.println("Code: " + code);
        return accountRepository.findByCodeAndAccountNumber(code, recipientAccountNumber).orElseThrow();
    }

    private Account resolveOwnedAccount(TransferDto transferDto, User user) {
        if (transferDto.getAccountNumber() > 0) {
            Account account = accountRepository.findByAccountNumber(transferDto.getAccountNumber()).orElseThrow();
            if (!account.getOwner().getUid().equals(user.getUid())) {
                throw new IllegalStateException("Invalid account owner");
            }
            return account;
        }
        return accountRepository.findFirstByCodeAndOwnerUid(transferDto.getCode(), user.getUid()).orElseThrow(() -> new UnsupportedOperationException("Account does not exist for user"));
    }

    private Account resolveOperationalAccount(TransferDto transferDto, User user) {
        if (!user.getRoles().contains("EMPLOYEE") && !user.getRoles().contains("ADMIN")) {
            throw new IllegalStateException("Only bank employees can perform deposit or withdrawal operations");
        }
        long accountNumber = transferDto.getAccountNumber() > 0 ? transferDto.getAccountNumber() : transferDto.getRecipientAccountNumber();
        if (accountNumber <= 0) {
            throw new IllegalArgumentException("Customer account number is required");
        }
        return accountRepository.findByAccountNumber(accountNumber).orElseThrow(() -> new IllegalArgumentException("Customer account does not exist"));
    }

    @java.lang.SuppressWarnings("all")
    public AccountService(final AccountRepository accountRepository, final AccountHelper accountHelper, final ExchangeRateService exchangeRateService) {
        this.accountRepository = accountRepository;
        this.accountHelper = accountHelper;
        this.exchangeRateService = exchangeRateService;
    }
}
