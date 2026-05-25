package rbibank.web.app.service;

import rbibank.web.app.entity.*;
import rbibank.web.app.repository.CardRepository;
import rbibank.web.app.service.helper.AccountHelper;
import rbibank.web.app.util.RandomUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
@Transactional
public class CardService {
    private final CardRepository cardRepository;
    private final AccountHelper accountHelper;
    private final TransactionService transactionService;

    public Card getCard(User user) {
        return cardRepository.findByOwnerUid(user.getUid()).orElseThrow();
    }

    public Card createCard(double amount, User user) throws Exception {
        if (amount < 2) {
            throw new IllegalArgumentException("Amount should be at least $2");
        }
        if (!accountHelper.existsByCodeAndOwnerUid("USD", user.getUid())) {
            throw new IllegalArgumentException("USD Account not found for this user so card cannot be created");
        }
        var usdAccount = accountHelper.findByCodeAndOwnerUid("USD", user.getUid()).orElseThrow();
        accountHelper.validateSufficientFunds(usdAccount, amount);
        usdAccount.setBalance(usdAccount.getBalance() - amount);
        long cardNumber;
        do {
            cardNumber = generateCardNumber();
        } while (cardRepository.existsByCardNumber(cardNumber));
        Card card = Card.builder().cardHolder(user.getFirstname() + " " + user.getLastname()).cardNumber(cardNumber).exp(LocalDateTime.now().plusYears(3)).owner(user).cvv(new RandomUtil().generateRandom(3).toString()).balance(amount - 1).build();
        card = cardRepository.save(card);
        transactionService.createAccountTransaction(1, Type.WITHDRAW, 0.0, user, usdAccount);
        transactionService.createAccountTransaction(amount - 1, Type.WITHDRAW, 0.0, user, usdAccount);
        transactionService.createCardTransaction(amount - 1, Type.WITHDRAW, 0.0, user, card);
        accountHelper.save(usdAccount);
        return card;
    }

    private long generateCardNumber() {
        return new RandomUtil().generateRandom(16);
    }

    public Transaction creditCard(double amount, User user) throws Exception {
        accountHelper.validateAmount(amount);
        var usdAccount = accountHelper.findByCodeAndOwnerUid("USD", user.getUid()).orElseThrow();
        accountHelper.validateSufficientFunds(usdAccount, amount);
        usdAccount.setBalance(usdAccount.getBalance() - amount);
        transactionService.createAccountTransaction(amount, Type.WITHDRAW, 0.0, user, usdAccount);
        var card = user.getCard();
        card.setBalance(card.getBalance() + amount);
        cardRepository.save(card);
        return transactionService.createCardTransaction(amount, Type.CREDIT, 0.0, user, card);
    }

    public Transaction debitCard(double amount, User user) throws Exception {
        accountHelper.validateAmount(amount);
        var usdAccount = accountHelper.findByCodeAndOwnerUid("USD", user.getUid()).orElseThrow();
        var card = user.getCard();
        if (card.getBalance() < amount) {
            throw new IllegalArgumentException("Insufficient funds on the card");
        }
        usdAccount.setBalance(usdAccount.getBalance() + amount);
        transactionService.createAccountTransaction(amount, Type.DEPOSIT, 0.0, user, usdAccount);
        card.setBalance(card.getBalance() - amount);
        cardRepository.save(card);
        return transactionService.createCardTransaction(amount, Type.DEBIT, 0.0, user, card);
    }

    @java.lang.SuppressWarnings("all")
    public CardService(final CardRepository cardRepository, final AccountHelper accountHelper, final TransactionService transactionService) {
        this.cardRepository = cardRepository;
        this.accountHelper = accountHelper;
        this.transactionService = transactionService;
    }
}
