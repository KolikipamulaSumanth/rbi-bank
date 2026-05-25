package rbibank.web.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String accountId;
    private double balance;
    private String bankName;
    private String ifscCode;
    private String branchName;
    @Enumerated(EnumType.STRING)
    private AccountType accountType;
    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;
    private String accountName;
    @Column(unique = true, nullable = false)
    private long accountNumber;
    private String currency;
    private String code;
    private String label;
    private char symbol;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    private LocalDateTime lastTransactionAt;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    @JsonIgnore
    private User owner;
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaction> transactions;

    @java.lang.SuppressWarnings("all")
    public static class AccountBuilder {
        @java.lang.SuppressWarnings("all")
        private String accountId;
        @java.lang.SuppressWarnings("all")
        private double balance;
        @java.lang.SuppressWarnings("all")
        private String bankName;
        @java.lang.SuppressWarnings("all")
        private String ifscCode;
        @java.lang.SuppressWarnings("all")
        private String branchName;
        @java.lang.SuppressWarnings("all")
        private AccountType accountType;
        @java.lang.SuppressWarnings("all")
        private AccountStatus accountStatus;
        @java.lang.SuppressWarnings("all")
        private String accountName;
        @java.lang.SuppressWarnings("all")
        private long accountNumber;
        @java.lang.SuppressWarnings("all")
        private String currency;
        @java.lang.SuppressWarnings("all")
        private String code;
        @java.lang.SuppressWarnings("all")
        private String label;
        @java.lang.SuppressWarnings("all")
        private char symbol;
        @java.lang.SuppressWarnings("all")
        private LocalDateTime updatedAt;
        @java.lang.SuppressWarnings("all")
        private LocalDateTime lastTransactionAt;
        @java.lang.SuppressWarnings("all")
        private LocalDateTime createdAt;
        @java.lang.SuppressWarnings("all")
        private User owner;
        @java.lang.SuppressWarnings("all")
        private List<Transaction> transactions;

        @java.lang.SuppressWarnings("all")
        AccountBuilder() {
        }

        @java.lang.SuppressWarnings("all")
        public Account.AccountBuilder accountId(final String accountId) {
            this.accountId = accountId;
            return this;
        }

        @java.lang.SuppressWarnings("all")
        public Account.AccountBuilder balance(final double balance) {
            this.balance = balance;
            return this;
        }

        @java.lang.SuppressWarnings("all")
        public Account.AccountBuilder bankName(final String bankName) {
            this.bankName = bankName;
            return this;
        }

        @java.lang.SuppressWarnings("all")
        public Account.AccountBuilder ifscCode(final String ifscCode) {
            this.ifscCode = ifscCode;
            return this;
        }

        @java.lang.SuppressWarnings("all")
        public Account.AccountBuilder branchName(final String branchName) {
            this.branchName = branchName;
            return this;
        }

        @java.lang.SuppressWarnings("all")
        public Account.AccountBuilder accountType(final AccountType accountType) {
            this.accountType = accountType;
            return this;
        }

        @java.lang.SuppressWarnings("all")
        public Account.AccountBuilder accountStatus(final AccountStatus accountStatus) {
            this.accountStatus = accountStatus;
            return this;
        }

        @java.lang.SuppressWarnings("all")
        public Account.AccountBuilder accountName(final String accountName) {
            this.accountName = accountName;
            return this;
        }

        @java.lang.SuppressWarnings("all")
        public Account.AccountBuilder accountNumber(final long accountNumber) {
            this.accountNumber = accountNumber;
            return this;
        }

        @java.lang.SuppressWarnings("all")
        public Account.AccountBuilder currency(final String currency) {
            this.currency = currency;
            return this;
        }

        @java.lang.SuppressWarnings("all")
        public Account.AccountBuilder code(final String code) {
            this.code = code;
            return this;
        }

        @java.lang.SuppressWarnings("all")
        public Account.AccountBuilder label(final String label) {
            this.label = label;
            return this;
        }

        @java.lang.SuppressWarnings("all")
        public Account.AccountBuilder symbol(final char symbol) {
            this.symbol = symbol;
            return this;
        }

        @java.lang.SuppressWarnings("all")
        public Account.AccountBuilder updatedAt(final LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        @java.lang.SuppressWarnings("all")
        public Account.AccountBuilder lastTransactionAt(final LocalDateTime lastTransactionAt) {
            this.lastTransactionAt = lastTransactionAt;
            return this;
        }

        @java.lang.SuppressWarnings("all")
        public Account.AccountBuilder createdAt(final LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        @JsonIgnore
        @java.lang.SuppressWarnings("all")
        public Account.AccountBuilder owner(final User owner) {
            this.owner = owner;
            return this;
        }

        @java.lang.SuppressWarnings("all")
        public Account.AccountBuilder transactions(final List<Transaction> transactions) {
            this.transactions = transactions;
            return this;
        }

        @java.lang.SuppressWarnings("all")
        public Account build() {
            return new Account(this.accountId, this.balance, this.bankName, this.ifscCode, this.branchName, this.accountType, this.accountStatus, this.accountName, this.accountNumber, this.currency, this.code, this.label, this.symbol, this.updatedAt, this.lastTransactionAt, this.createdAt, this.owner, this.transactions);
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("all")
        public java.lang.String toString() {
            return "Account.AccountBuilder(accountId=" + this.accountId + ", balance=" + this.balance + ", bankName=" + this.bankName + ", ifscCode=" + this.ifscCode + ", branchName=" + this.branchName + ", accountType=" + this.accountType + ", accountStatus=" + this.accountStatus + ", accountName=" + this.accountName + ", accountNumber=" + this.accountNumber + ", currency=" + this.currency + ", code=" + this.code + ", label=" + this.label + ", symbol=" + this.symbol + ", updatedAt=" + this.updatedAt + ", lastTransactionAt=" + this.lastTransactionAt + ", createdAt=" + this.createdAt + ", owner=" + this.owner + ", transactions=" + this.transactions + ")";
        }
    }

    @java.lang.SuppressWarnings("all")
    public static Account.AccountBuilder builder() {
        return new Account.AccountBuilder();
    }

    @java.lang.SuppressWarnings("all")
    public String getAccountId() {
        return this.accountId;
    }

    @java.lang.SuppressWarnings("all")
    public double getBalance() {
        return this.balance;
    }

    @java.lang.SuppressWarnings("all")
    public String getBankName() {
        return this.bankName;
    }

    @java.lang.SuppressWarnings("all")
    public String getIfscCode() {
        return this.ifscCode;
    }

    @java.lang.SuppressWarnings("all")
    public String getBranchName() {
        return this.branchName;
    }

    @java.lang.SuppressWarnings("all")
    public AccountType getAccountType() {
        return this.accountType;
    }

    @java.lang.SuppressWarnings("all")
    public AccountStatus getAccountStatus() {
        return this.accountStatus;
    }

    @java.lang.SuppressWarnings("all")
    public String getAccountName() {
        return this.accountName;
    }

    @java.lang.SuppressWarnings("all")
    public long getAccountNumber() {
        return this.accountNumber;
    }

    @java.lang.SuppressWarnings("all")
    public String getCurrency() {
        return this.currency;
    }

    @java.lang.SuppressWarnings("all")
    public String getCode() {
        return this.code;
    }

    @java.lang.SuppressWarnings("all")
    public String getLabel() {
        return this.label;
    }

    @java.lang.SuppressWarnings("all")
    public char getSymbol() {
        return this.symbol;
    }

    @java.lang.SuppressWarnings("all")
    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    @java.lang.SuppressWarnings("all")
    public LocalDateTime getLastTransactionAt() {
        return this.lastTransactionAt;
    }

    @java.lang.SuppressWarnings("all")
    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    @java.lang.SuppressWarnings("all")
    public User getOwner() {
        return this.owner;
    }

    @java.lang.SuppressWarnings("all")
    public List<Transaction> getTransactions() {
        return this.transactions;
    }

    @java.lang.SuppressWarnings("all")
    public void setAccountId(final String accountId) {
        this.accountId = accountId;
    }

    @java.lang.SuppressWarnings("all")
    public void setBalance(final double balance) {
        this.balance = balance;
    }

    @java.lang.SuppressWarnings("all")
    public void setBankName(final String bankName) {
        this.bankName = bankName;
    }

    @java.lang.SuppressWarnings("all")
    public void setIfscCode(final String ifscCode) {
        this.ifscCode = ifscCode;
    }

    @java.lang.SuppressWarnings("all")
    public void setBranchName(final String branchName) {
        this.branchName = branchName;
    }

    @java.lang.SuppressWarnings("all")
    public void setAccountType(final AccountType accountType) {
        this.accountType = accountType;
    }

    @java.lang.SuppressWarnings("all")
    public void setAccountStatus(final AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
    }

    @java.lang.SuppressWarnings("all")
    public void setAccountName(final String accountName) {
        this.accountName = accountName;
    }

    @java.lang.SuppressWarnings("all")
    public void setAccountNumber(final long accountNumber) {
        this.accountNumber = accountNumber;
    }

    @java.lang.SuppressWarnings("all")
    public void setCurrency(final String currency) {
        this.currency = currency;
    }

    @java.lang.SuppressWarnings("all")
    public void setCode(final String code) {
        this.code = code;
    }

    @java.lang.SuppressWarnings("all")
    public void setLabel(final String label) {
        this.label = label;
    }

    @java.lang.SuppressWarnings("all")
    public void setSymbol(final char symbol) {
        this.symbol = symbol;
    }

    @java.lang.SuppressWarnings("all")
    public void setUpdatedAt(final LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @java.lang.SuppressWarnings("all")
    public void setLastTransactionAt(final LocalDateTime lastTransactionAt) {
        this.lastTransactionAt = lastTransactionAt;
    }

    @java.lang.SuppressWarnings("all")
    public void setCreatedAt(final LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @JsonIgnore
    @java.lang.SuppressWarnings("all")
    public void setOwner(final User owner) {
        this.owner = owner;
    }

    @java.lang.SuppressWarnings("all")
    public void setTransactions(final List<Transaction> transactions) {
        this.transactions = transactions;
    }

    @java.lang.SuppressWarnings("all")
    public Account(final String accountId, final double balance, final String bankName, final String ifscCode, final String branchName, final AccountType accountType, final AccountStatus accountStatus, final String accountName, final long accountNumber, final String currency, final String code, final String label, final char symbol, final LocalDateTime updatedAt, final LocalDateTime lastTransactionAt, final LocalDateTime createdAt, final User owner, final List<Transaction> transactions) {
        this.accountId = accountId;
        this.balance = balance;
        this.bankName = bankName;
        this.ifscCode = ifscCode;
        this.branchName = branchName;
        this.accountType = accountType;
        this.accountStatus = accountStatus;
        this.accountName = accountName;
        this.accountNumber = accountNumber;
        this.currency = currency;
        this.code = code;
        this.label = label;
        this.symbol = symbol;
        this.updatedAt = updatedAt;
        this.lastTransactionAt = lastTransactionAt;
        this.createdAt = createdAt;
        this.owner = owner;
        this.transactions = transactions;
    }

    @java.lang.SuppressWarnings("all")
    public Account() {
    }
}
