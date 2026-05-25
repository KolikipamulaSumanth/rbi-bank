package rbibank.web.app.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rbibank.web.app.dto.TransferDto;
import rbibank.web.app.entity.Account;
import rbibank.web.app.entity.AccountStatus;
import rbibank.web.app.entity.Transaction;
import rbibank.web.app.entity.User;
import rbibank.web.app.repository.AccountRepository;
import rbibank.web.app.service.helper.AccountHelper;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountHelper accountHelper;

    @Mock
    private ExchangeRateService exchangeRateService;

    @InjectMocks
    private AccountService accountService;

    @Test
    void depositAllowsEmployeeAndDelegatesToAccountHelper() throws Exception {
        User employee = user("employee-1", "EMPLOYEE");
        Account account = account("account-1", employee, 1001L);
        TransferDto transferDto = transferDto(1001L, 500.0);
        Transaction expectedTransaction = Transaction.builder().amount(500.0).account(account).build();

        when(accountRepository.findByAccountNumber(1001L)).thenReturn(Optional.of(account));
        when(accountHelper.deposit(account, 500.0)).thenReturn(expectedTransaction);

        Transaction actualTransaction = accountService.deposit(transferDto, employee);

        assertThat(actualTransaction).isSameAs(expectedTransaction);
        verify(accountRepository).findByAccountNumber(1001L);
        verify(accountHelper).deposit(account, 500.0);
    }

    @Test
    void depositRejectsNonStaffUsers() {
        User customer = user("customer-1", "CUSTOMER");
        TransferDto transferDto = transferDto(1001L, 500.0);

        assertThatThrownBy(() -> accountService.deposit(transferDto, customer))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("Only bank employees can perform deposit or withdrawal operations");

        verify(accountRepository, never()).findByAccountNumber(1001L);
    }

    @Test
    void transferFundsUsesOwnedSenderAccountAndReceiverAccount() throws Exception {
        User owner = user("customer-1", "CUSTOMER");
        Account senderAccount = account("sender-account", owner, 1001L);
        Account receiverAccount = account("receiver-account", user("customer-2", "CUSTOMER"), 2002L);
        TransferDto transferDto = transferDto(1001L, 2002L, 250.0, "Rent");
        Transaction expectedTransaction = Transaction.builder().amount(250.0).account(senderAccount).build();

        when(accountRepository.findByAccountNumber(1001L)).thenReturn(Optional.of(senderAccount));
        when(accountRepository.findByAccountNumber(2002L)).thenReturn(Optional.of(receiverAccount));
        when(accountHelper.performTransfer(senderAccount, receiverAccount, 250.0, owner, "Rent"))
            .thenReturn(expectedTransaction);

        Transaction actualTransaction = accountService.transferFunds(transferDto, owner);

        assertThat(actualTransaction).isSameAs(expectedTransaction);
        verify(accountHelper).performTransfer(senderAccount, receiverAccount, 250.0, owner, "Rent");
    }

    @Test
    void transferFundsRejectsAccountOwnedByDifferentUser() throws Exception {
        User signedInUser = user("customer-1", "CUSTOMER");
        Account anotherCustomersAccount = account("sender-account", user("customer-2", "CUSTOMER"), 1001L);
        TransferDto transferDto = transferDto(1001L, 2002L, 250.0, "Rent");

        when(accountRepository.findByAccountNumber(1001L)).thenReturn(Optional.of(anotherCustomersAccount));

        assertThatThrownBy(() -> accountService.transferFunds(transferDto, signedInUser))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("Invalid account owner");

        verify(accountHelper, never()).performTransfer(
            anotherCustomersAccount,
            null,
            250.0,
            signedInUser,
            "Rent"
        );
    }

    @Test
    void updateStatusSavesAccountWithNewStatus() {
        Account account = account("account-1", user("customer-1", "CUSTOMER"), 1001L);
        when(accountRepository.findById("account-1")).thenReturn(Optional.of(account));
        when(accountRepository.save(account)).thenReturn(account);

        Account updatedAccount = accountService.updateStatus("account-1", AccountStatus.CLOSED);

        assertThat(updatedAccount.getAccountStatus()).isEqualTo(AccountStatus.CLOSED);
        verify(accountRepository).save(account);
    }

    private static User user(String uid, String role) {
        return User.builder()
            .uid(uid)
            .username(uid + "@example.com")
            .roles(List.of(role))
            .build();
    }

    private static Account account(String accountId, User owner, long accountNumber) {
        return Account.builder()
            .accountId(accountId)
            .accountNumber(accountNumber)
            .accountStatus(AccountStatus.ACTIVE)
            .owner(owner)
            .build();
    }

    private static TransferDto transferDto(long accountNumber, double amount) {
        TransferDto transferDto = new TransferDto();
        transferDto.setAccountNumber(accountNumber);
        transferDto.setAmount(amount);
        return transferDto;
    }

    private static TransferDto transferDto(
        long accountNumber,
        long recipientAccountNumber,
        double amount,
        String remarks
    ) {
        TransferDto transferDto = transferDto(accountNumber, amount);
        transferDto.setRecipientAccountNumber(recipientAccountNumber);
        transferDto.setRemarks(remarks);
        return transferDto;
    }
}
