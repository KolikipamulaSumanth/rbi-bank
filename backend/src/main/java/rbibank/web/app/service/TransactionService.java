package rbibank.web.app.service;

import rbibank.web.app.entity.*;
import rbibank.web.app.repository.TransactionRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;

    public List<Transaction> getAllTransactions(String page, User user) {
        Pageable pageable = PageRequest.of(Integer.parseInt(page), 10, Sort.by("createdAt").descending());
        return transactionRepository.findAllByOwnerUid(user.getUid(), pageable).getContent();
    }

    public List<Transaction> getAllTransactionsForStaff(String page) {
        Pageable pageable = PageRequest.of(Integer.parseInt(page), 50, Sort.by("createdAt").descending());
        return transactionRepository.findAll(pageable).getContent();
    }

    public List<Transaction> getTransactionsByCardId(String cardId, String page, User user) {
        Pageable pageable = PageRequest.of(Integer.parseInt(page), 10, Sort.by("createdAt").descending());
        return transactionRepository.findAllByCardCardIdAndOwnerUid(cardId, user.getUid(), pageable).getContent();
    }

    public List<Transaction> getTransactionsByAccountId(String accountId, String page, User user) {
        Pageable pageable = PageRequest.of(Integer.parseInt(page), 10, Sort.by("createdAt").descending());
        return transactionRepository.findAllByAccountAccountIdAndOwnerUid(accountId, user.getUid(), pageable).getContent();
    }

    public Transaction createAccountTransaction(double amount, Type type, double txFee, User user, Account account) {
        var tx = Transaction.builder().txFee(txFee).amount(amount).type(type).status(Status.COMPLETED).owner(user).account(account).description(type + " via RBI Bank").build();
        return transactionRepository.save(tx);
    }

    public Transaction createAccountTransaction(double amount, Type type, double txFee, User user, Account account, Long senderAccountNumber, Long receiverAccountNumber, String counterpartyName, String description, double balanceAfterTransaction) {
        var tx = Transaction.builder().txFee(txFee).amount(amount).type(type).status(Status.COMPLETED).owner(user).account(account).senderAccountNumber(senderAccountNumber).receiverAccountNumber(receiverAccountNumber).counterpartyName(counterpartyName).description(description).balanceAfterTransaction(balanceAfterTransaction).build();
        return transactionRepository.save(tx);
    }

    public Transaction createCardTransaction(double amount, Type type, double txFee, User user, Card card) {
        Transaction tx = Transaction.builder().txFee(txFee).amount(amount).type(type).card(card).status(Status.COMPLETED).owner(user).build();
        return transactionRepository.save(tx);
    }

    @java.lang.SuppressWarnings("all")
    public TransactionService(final TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }
}
