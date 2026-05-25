package rbibank.web.app.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rbibank.web.app.entity.Account;
import rbibank.web.app.entity.Status;
import rbibank.web.app.entity.Transaction;
import rbibank.web.app.entity.Type;
import rbibank.web.app.entity.User;
import rbibank.web.app.repository.TransactionRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void createAccountTransactionBuildsAndSavesCompletedTransaction() {
        User owner = User.builder().uid("customer-1").username("customer@example.com").build();
        Account account = Account.builder().accountId("account-1").accountNumber(1001L).build();
        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);

        when(transactionRepository.save(transactionCaptor.capture()))
            .thenAnswer(invocation -> invocation.getArgument(0));

        Transaction transaction = transactionService.createAccountTransaction(
            1500.0,
            Type.CREDIT,
            0.0,
            owner,
            account,
            null,
            1001L,
            "RBI Bank Branch",
            "Branch cash deposit",
            2500.0
        );

        assertThat(transaction).isSameAs(transactionCaptor.getValue());
        assertThat(transaction.getAmount()).isEqualTo(1500.0);
        assertThat(transaction.getType()).isEqualTo(Type.CREDIT);
        assertThat(transaction.getStatus()).isEqualTo(Status.COMPLETED);
        assertThat(transaction.getOwner()).isSameAs(owner);
        assertThat(transaction.getAccount()).isSameAs(account);
        assertThat(transaction.getReceiverAccountNumber()).isEqualTo(1001L);
        assertThat(transaction.getCounterpartyName()).isEqualTo("RBI Bank Branch");
        assertThat(transaction.getDescription()).isEqualTo("Branch cash deposit");
        assertThat(transaction.getBalanceAfterTransaction()).isEqualTo(2500.0);
        verify(transactionRepository).save(transactionCaptor.getValue());
    }

    @Test
    void createSimpleAccountTransactionAddsDefaultDescription() {
        User owner = User.builder().uid("customer-1").username("customer@example.com").build();
        Account account = Account.builder().accountId("account-1").accountNumber(1001L).build();
        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);

        when(transactionRepository.save(transactionCaptor.capture()))
            .thenAnswer(invocation -> invocation.getArgument(0));

        Transaction transaction = transactionService.createAccountTransaction(
            100.0,
            Type.DEBIT,
            2.0,
            owner,
            account
        );

        assertThat(transaction).isSameAs(transactionCaptor.getValue());
        assertThat(transaction.getDescription()).isEqualTo("DEBIT via RBI Bank");
        assertThat(transaction.getTxFee()).isEqualTo(2.0);
        assertThat(transaction.getStatus()).isEqualTo(Status.COMPLETED);
        verify(transactionRepository).save(transactionCaptor.getValue());
    }
}
