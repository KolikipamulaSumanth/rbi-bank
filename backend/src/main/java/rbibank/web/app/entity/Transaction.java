package rbibank.web.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String txId;
    private Double amount;
    private Double txFee;
    private String sender;
    private String receiver;
    private String description;
    private Long senderAccountNumber;
    private Long receiverAccountNumber;
    private String counterpartyName;
    private Double balanceAfterTransaction;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @Enumerated(EnumType.STRING)
    private Status status;
    @Enumerated(EnumType.STRING)
    private Type type;
    @ManyToOne
    @JoinColumn(name = "card_id")
    @JsonIgnore
    private Card card;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    @JsonIgnore
    private User owner;
    @ManyToOne
    @JoinColumn(name = "account_id")
    @JsonIgnore
    private Account account;

    @java.lang.SuppressWarnings("all")
    public static class TransactionBuilder {
        @java.lang.SuppressWarnings("all")
        private String txId;
        @java.lang.SuppressWarnings("all")
        private Double amount;
        @java.lang.SuppressWarnings("all")
        private Double txFee;
        @java.lang.SuppressWarnings("all")
        private String sender;
        @java.lang.SuppressWarnings("all")
        private String receiver;
        @java.lang.SuppressWarnings("all")
        private String description;
        @java.lang.SuppressWarnings("all")
        private Long senderAccountNumber;
        @java.lang.SuppressWarnings("all")
        private Long receiverAccountNumber;
        @java.lang.SuppressWarnings("all")
        private String counterpartyName;
        @java.lang.SuppressWarnings("all")
        private Double balanceAfterTransaction;
        @java.lang.SuppressWarnings("all")
        private LocalDateTime updatedAt;
        @java.lang.SuppressWarnings("all")
        private LocalDateTime createdAt;
        @java.lang.SuppressWarnings("all")
        private Status status;
        @java.lang.SuppressWarnings("all")
        private Type type;
        @java.lang.SuppressWarnings("all")
        private Card card;
        @java.lang.SuppressWarnings("all")
        private User owner;
        @java.lang.SuppressWarnings("all")
        private Account account;

        @java.lang.SuppressWarnings("all")
        TransactionBuilder() {
        }

        @java.lang.SuppressWarnings("all")
        public Transaction.TransactionBuilder txId(final String txId) {
            this.txId = txId;
            return this;
        }

        @java.lang.SuppressWarnings("all")
        public Transaction.TransactionBuilder amount(final Double amount) {
            this.amount = amount;
            return this;
        }

        @java.lang.SuppressWarnings("all")
        public Transaction.TransactionBuilder txFee(final Double txFee) {
            this.txFee = txFee;
            return this;
        }

        @java.lang.SuppressWarnings("all")
        public Transaction.TransactionBuilder sender(final String sender) {
            this.sender = sender;
            return this;
        }

        @java.lang.SuppressWarnings("all")
        public Transaction.TransactionBuilder receiver(final String receiver) {
            this.receiver = receiver;
            return this;
        }

        @java.lang.SuppressWarnings("all")
        public Transaction.TransactionBuilder description(final String description) {
            this.description = description;
            return this;
        }

        @java.lang.SuppressWarnings("all")
        public Transaction.TransactionBuilder senderAccountNumber(final Long senderAccountNumber) {
            this.senderAccountNumber = senderAccountNumber;
            return this;
        }

        @java.lang.SuppressWarnings("all")
        public Transaction.TransactionBuilder receiverAccountNumber(final Long receiverAccountNumber) {
            this.receiverAccountNumber = receiverAccountNumber;
            return this;
        }

        @java.lang.SuppressWarnings("all")
        public Transaction.TransactionBuilder counterpartyName(final String counterpartyName) {
            this.counterpartyName = counterpartyName;
            return this;
        }

        @java.lang.SuppressWarnings("all")
        public Transaction.TransactionBuilder balanceAfterTransaction(final Double balanceAfterTransaction) {
            this.balanceAfterTransaction = balanceAfterTransaction;
            return this;
        }

        @java.lang.SuppressWarnings("all")
        public Transaction.TransactionBuilder updatedAt(final LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        @java.lang.SuppressWarnings("all")
        public Transaction.TransactionBuilder createdAt(final LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        @java.lang.SuppressWarnings("all")
        public Transaction.TransactionBuilder status(final Status status) {
            this.status = status;
            return this;
        }

        @java.lang.SuppressWarnings("all")
        public Transaction.TransactionBuilder type(final Type type) {
            this.type = type;
            return this;
        }

        @JsonIgnore
        @java.lang.SuppressWarnings("all")
        public Transaction.TransactionBuilder card(final Card card) {
            this.card = card;
            return this;
        }

        @JsonIgnore
        @java.lang.SuppressWarnings("all")
        public Transaction.TransactionBuilder owner(final User owner) {
            this.owner = owner;
            return this;
        }

        @JsonIgnore
        @java.lang.SuppressWarnings("all")
        public Transaction.TransactionBuilder account(final Account account) {
            this.account = account;
            return this;
        }

        @java.lang.SuppressWarnings("all")
        public Transaction build() {
            return new Transaction(this.txId, this.amount, this.txFee, this.sender, this.receiver, this.description, this.senderAccountNumber, this.receiverAccountNumber, this.counterpartyName, this.balanceAfterTransaction, this.updatedAt, this.createdAt, this.status, this.type, this.card, this.owner, this.account);
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("all")
        public java.lang.String toString() {
            return "Transaction.TransactionBuilder(txId=" + this.txId + ", amount=" + this.amount + ", txFee=" + this.txFee + ", sender=" + this.sender + ", receiver=" + this.receiver + ", description=" + this.description + ", senderAccountNumber=" + this.senderAccountNumber + ", receiverAccountNumber=" + this.receiverAccountNumber + ", counterpartyName=" + this.counterpartyName + ", balanceAfterTransaction=" + this.balanceAfterTransaction + ", updatedAt=" + this.updatedAt + ", createdAt=" + this.createdAt + ", status=" + this.status + ", type=" + this.type + ", card=" + this.card + ", owner=" + this.owner + ", account=" + this.account + ")";
        }
    }

    @java.lang.SuppressWarnings("all")
    public static Transaction.TransactionBuilder builder() {
        return new Transaction.TransactionBuilder();
    }

    @java.lang.SuppressWarnings("all")
    public String getTxId() {
        return this.txId;
    }

    @java.lang.SuppressWarnings("all")
    public Double getAmount() {
        return this.amount;
    }

    @java.lang.SuppressWarnings("all")
    public Double getTxFee() {
        return this.txFee;
    }

    @java.lang.SuppressWarnings("all")
    public String getSender() {
        return this.sender;
    }

    @java.lang.SuppressWarnings("all")
    public String getReceiver() {
        return this.receiver;
    }

    @java.lang.SuppressWarnings("all")
    public String getDescription() {
        return this.description;
    }

    @java.lang.SuppressWarnings("all")
    public Long getSenderAccountNumber() {
        return this.senderAccountNumber;
    }

    @java.lang.SuppressWarnings("all")
    public Long getReceiverAccountNumber() {
        return this.receiverAccountNumber;
    }

    @java.lang.SuppressWarnings("all")
    public String getCounterpartyName() {
        return this.counterpartyName;
    }

    @java.lang.SuppressWarnings("all")
    public Double getBalanceAfterTransaction() {
        return this.balanceAfterTransaction;
    }

    @java.lang.SuppressWarnings("all")
    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    @java.lang.SuppressWarnings("all")
    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    @java.lang.SuppressWarnings("all")
    public Status getStatus() {
        return this.status;
    }

    @java.lang.SuppressWarnings("all")
    public Type getType() {
        return this.type;
    }

    @java.lang.SuppressWarnings("all")
    public Card getCard() {
        return this.card;
    }

    @java.lang.SuppressWarnings("all")
    public User getOwner() {
        return this.owner;
    }

    @java.lang.SuppressWarnings("all")
    public Account getAccount() {
        return this.account;
    }

    @java.lang.SuppressWarnings("all")
    public void setTxId(final String txId) {
        this.txId = txId;
    }

    @java.lang.SuppressWarnings("all")
    public void setAmount(final Double amount) {
        this.amount = amount;
    }

    @java.lang.SuppressWarnings("all")
    public void setTxFee(final Double txFee) {
        this.txFee = txFee;
    }

    @java.lang.SuppressWarnings("all")
    public void setSender(final String sender) {
        this.sender = sender;
    }

    @java.lang.SuppressWarnings("all")
    public void setReceiver(final String receiver) {
        this.receiver = receiver;
    }

    @java.lang.SuppressWarnings("all")
    public void setDescription(final String description) {
        this.description = description;
    }

    @java.lang.SuppressWarnings("all")
    public void setSenderAccountNumber(final Long senderAccountNumber) {
        this.senderAccountNumber = senderAccountNumber;
    }

    @java.lang.SuppressWarnings("all")
    public void setReceiverAccountNumber(final Long receiverAccountNumber) {
        this.receiverAccountNumber = receiverAccountNumber;
    }

    @java.lang.SuppressWarnings("all")
    public void setCounterpartyName(final String counterpartyName) {
        this.counterpartyName = counterpartyName;
    }

    @java.lang.SuppressWarnings("all")
    public void setBalanceAfterTransaction(final Double balanceAfterTransaction) {
        this.balanceAfterTransaction = balanceAfterTransaction;
    }

    @java.lang.SuppressWarnings("all")
    public void setUpdatedAt(final LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @java.lang.SuppressWarnings("all")
    public void setCreatedAt(final LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @java.lang.SuppressWarnings("all")
    public void setStatus(final Status status) {
        this.status = status;
    }

    @java.lang.SuppressWarnings("all")
    public void setType(final Type type) {
        this.type = type;
    }

    @JsonIgnore
    @java.lang.SuppressWarnings("all")
    public void setCard(final Card card) {
        this.card = card;
    }

    @JsonIgnore
    @java.lang.SuppressWarnings("all")
    public void setOwner(final User owner) {
        this.owner = owner;
    }

    @JsonIgnore
    @java.lang.SuppressWarnings("all")
    public void setAccount(final Account account) {
        this.account = account;
    }

    @java.lang.SuppressWarnings("all")
    public Transaction(final String txId, final Double amount, final Double txFee, final String sender, final String receiver, final String description, final Long senderAccountNumber, final Long receiverAccountNumber, final String counterpartyName, final Double balanceAfterTransaction, final LocalDateTime updatedAt, final LocalDateTime createdAt, final Status status, final Type type, final Card card, final User owner, final Account account) {
        this.txId = txId;
        this.amount = amount;
        this.txFee = txFee;
        this.sender = sender;
        this.receiver = receiver;
        this.description = description;
        this.senderAccountNumber = senderAccountNumber;
        this.receiverAccountNumber = receiverAccountNumber;
        this.counterpartyName = counterpartyName;
        this.balanceAfterTransaction = balanceAfterTransaction;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
        this.status = status;
        this.type = type;
        this.card = card;
        this.owner = owner;
        this.account = account;
    }

    @java.lang.SuppressWarnings("all")
    public Transaction() {
    }
}
