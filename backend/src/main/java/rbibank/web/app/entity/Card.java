package rbibank.web.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String cardId;
    @Column(nullable = false, unique = true)
    private long cardNumber;
    private String cardHolder;
    private Double balance;
    @CreationTimestamp
    private LocalDate iss;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    private LocalDateTime exp;
    private String cvv;
    private String pin;
    private String billingAddress;
    @OneToOne
    @JoinColumn(name = "owner_id")
    @JsonIgnore
    private User owner;
    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Transaction> transactions;

    @java.lang.SuppressWarnings("all")
    public static class CardBuilder {
        @java.lang.SuppressWarnings("all")
        private String cardId;
        @java.lang.SuppressWarnings("all")
        private long cardNumber;
        @java.lang.SuppressWarnings("all")
        private String cardHolder;
        @java.lang.SuppressWarnings("all")
        private Double balance;
        @java.lang.SuppressWarnings("all")
        private LocalDate iss;
        @java.lang.SuppressWarnings("all")
        private LocalDateTime updatedAt;
        @java.lang.SuppressWarnings("all")
        private LocalDateTime exp;
        @java.lang.SuppressWarnings("all")
        private String cvv;
        @java.lang.SuppressWarnings("all")
        private String pin;
        @java.lang.SuppressWarnings("all")
        private String billingAddress;
        @java.lang.SuppressWarnings("all")
        private User owner;
        @java.lang.SuppressWarnings("all")
        private List<Transaction> transactions;

        @java.lang.SuppressWarnings("all")
        CardBuilder() {
        }

        @java.lang.SuppressWarnings("all")
        public Card.CardBuilder cardId(final String cardId) {
            this.cardId = cardId;
            return this;
        }

        @java.lang.SuppressWarnings("all")
        public Card.CardBuilder cardNumber(final long cardNumber) {
            this.cardNumber = cardNumber;
            return this;
        }

        @java.lang.SuppressWarnings("all")
        public Card.CardBuilder cardHolder(final String cardHolder) {
            this.cardHolder = cardHolder;
            return this;
        }

        @java.lang.SuppressWarnings("all")
        public Card.CardBuilder balance(final Double balance) {
            this.balance = balance;
            return this;
        }

        @java.lang.SuppressWarnings("all")
        public Card.CardBuilder iss(final LocalDate iss) {
            this.iss = iss;
            return this;
        }

        @java.lang.SuppressWarnings("all")
        public Card.CardBuilder updatedAt(final LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        @java.lang.SuppressWarnings("all")
        public Card.CardBuilder exp(final LocalDateTime exp) {
            this.exp = exp;
            return this;
        }

        @java.lang.SuppressWarnings("all")
        public Card.CardBuilder cvv(final String cvv) {
            this.cvv = cvv;
            return this;
        }

        @java.lang.SuppressWarnings("all")
        public Card.CardBuilder pin(final String pin) {
            this.pin = pin;
            return this;
        }

        @java.lang.SuppressWarnings("all")
        public Card.CardBuilder billingAddress(final String billingAddress) {
            this.billingAddress = billingAddress;
            return this;
        }

        @JsonIgnore
        @java.lang.SuppressWarnings("all")
        public Card.CardBuilder owner(final User owner) {
            this.owner = owner;
            return this;
        }

        @JsonIgnore
        @java.lang.SuppressWarnings("all")
        public Card.CardBuilder transactions(final List<Transaction> transactions) {
            this.transactions = transactions;
            return this;
        }

        @java.lang.SuppressWarnings("all")
        public Card build() {
            return new Card(this.cardId, this.cardNumber, this.cardHolder, this.balance, this.iss, this.updatedAt, this.exp, this.cvv, this.pin, this.billingAddress, this.owner, this.transactions);
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("all")
        public java.lang.String toString() {
            return "Card.CardBuilder(cardId=" + this.cardId + ", cardNumber=" + this.cardNumber + ", cardHolder=" + this.cardHolder + ", balance=" + this.balance + ", iss=" + this.iss + ", updatedAt=" + this.updatedAt + ", exp=" + this.exp + ", cvv=" + this.cvv + ", pin=" + this.pin + ", billingAddress=" + this.billingAddress + ", owner=" + this.owner + ", transactions=" + this.transactions + ")";
        }
    }

    @java.lang.SuppressWarnings("all")
    public static Card.CardBuilder builder() {
        return new Card.CardBuilder();
    }

    @java.lang.SuppressWarnings("all")
    public String getCardId() {
        return this.cardId;
    }

    @java.lang.SuppressWarnings("all")
    public long getCardNumber() {
        return this.cardNumber;
    }

    @java.lang.SuppressWarnings("all")
    public String getCardHolder() {
        return this.cardHolder;
    }

    @java.lang.SuppressWarnings("all")
    public Double getBalance() {
        return this.balance;
    }

    @java.lang.SuppressWarnings("all")
    public LocalDate getIss() {
        return this.iss;
    }

    @java.lang.SuppressWarnings("all")
    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    @java.lang.SuppressWarnings("all")
    public LocalDateTime getExp() {
        return this.exp;
    }

    @java.lang.SuppressWarnings("all")
    public String getCvv() {
        return this.cvv;
    }

    @java.lang.SuppressWarnings("all")
    public String getPin() {
        return this.pin;
    }

    @java.lang.SuppressWarnings("all")
    public String getBillingAddress() {
        return this.billingAddress;
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
    public void setCardId(final String cardId) {
        this.cardId = cardId;
    }

    @java.lang.SuppressWarnings("all")
    public void setCardNumber(final long cardNumber) {
        this.cardNumber = cardNumber;
    }

    @java.lang.SuppressWarnings("all")
    public void setCardHolder(final String cardHolder) {
        this.cardHolder = cardHolder;
    }

    @java.lang.SuppressWarnings("all")
    public void setBalance(final Double balance) {
        this.balance = balance;
    }

    @java.lang.SuppressWarnings("all")
    public void setIss(final LocalDate iss) {
        this.iss = iss;
    }

    @java.lang.SuppressWarnings("all")
    public void setUpdatedAt(final LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @java.lang.SuppressWarnings("all")
    public void setExp(final LocalDateTime exp) {
        this.exp = exp;
    }

    @java.lang.SuppressWarnings("all")
    public void setCvv(final String cvv) {
        this.cvv = cvv;
    }

    @java.lang.SuppressWarnings("all")
    public void setPin(final String pin) {
        this.pin = pin;
    }

    @java.lang.SuppressWarnings("all")
    public void setBillingAddress(final String billingAddress) {
        this.billingAddress = billingAddress;
    }

    @JsonIgnore
    @java.lang.SuppressWarnings("all")
    public void setOwner(final User owner) {
        this.owner = owner;
    }

    @JsonIgnore
    @java.lang.SuppressWarnings("all")
    public void setTransactions(final List<Transaction> transactions) {
        this.transactions = transactions;
    }

    @java.lang.SuppressWarnings("all")
    public Card(final String cardId, final long cardNumber, final String cardHolder, final Double balance, final LocalDate iss, final LocalDateTime updatedAt, final LocalDateTime exp, final String cvv, final String pin, final String billingAddress, final User owner, final List<Transaction> transactions) {
        this.cardId = cardId;
        this.cardNumber = cardNumber;
        this.cardHolder = cardHolder;
        this.balance = balance;
        this.iss = iss;
        this.updatedAt = updatedAt;
        this.exp = exp;
        this.cvv = cvv;
        this.pin = pin;
        this.billingAddress = billingAddress;
        this.owner = owner;
        this.transactions = transactions;
    }

    @java.lang.SuppressWarnings("all")
    public Card() {
    }
}
