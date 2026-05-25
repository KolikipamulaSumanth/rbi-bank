package rbibank.web.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "bank_user")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String uid;
    private String firstname;
    private String lastname;
    @Column(nullable = false, unique = true)
    private String username;
    private Date dob;
    private long tel;
    @Column(unique = true)
    private String customerId;
    private String email;
    private String address;
    private String panNumber;
    private String aadhaarNumber;
    @Enumerated(EnumType.STRING)
    private KycStatus kycStatus;
    private String tag;
    @JsonIgnore
    private String password;
    private String gender;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles;
    @OneToOne(mappedBy = "owner")
    @JsonIgnore
    private Card card;
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Transaction> transactions;
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Account> accounts;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @java.lang.SuppressWarnings("all")
    public static class UserBuilder {
        @java.lang.SuppressWarnings("all")
        private String uid;
        @java.lang.SuppressWarnings("all")
        private String firstname;
        @java.lang.SuppressWarnings("all")
        private String lastname;
        @java.lang.SuppressWarnings("all")
        private String username;
        @java.lang.SuppressWarnings("all")
        private Date dob;
        @java.lang.SuppressWarnings("all")
        private long tel;
        @java.lang.SuppressWarnings("all")
        private String customerId;
        @java.lang.SuppressWarnings("all")
        private String email;
        @java.lang.SuppressWarnings("all")
        private String address;
        @java.lang.SuppressWarnings("all")
        private String panNumber;
        @java.lang.SuppressWarnings("all")
        private String aadhaarNumber;
        @java.lang.SuppressWarnings("all")
        private KycStatus kycStatus;
        @java.lang.SuppressWarnings("all")
        private String tag;
        @java.lang.SuppressWarnings("all")
        private String password;
        @java.lang.SuppressWarnings("all")
        private String gender;
        @java.lang.SuppressWarnings("all")
        private LocalDateTime createdAt;
        @java.lang.SuppressWarnings("all")
        private LocalDateTime updatedAt;
        @java.lang.SuppressWarnings("all")
        private List<String> roles;
        @java.lang.SuppressWarnings("all")
        private Card card;
        @java.lang.SuppressWarnings("all")
        private List<Transaction> transactions;
        @java.lang.SuppressWarnings("all")
        private List<Account> accounts;

        @java.lang.SuppressWarnings("all")
        UserBuilder() {
        }

        @java.lang.SuppressWarnings("all")
        public User.UserBuilder uid(final String uid) {
            this.uid = uid;
            return this;
        }

        @java.lang.SuppressWarnings("all")
        public User.UserBuilder firstname(final String firstname) {
            this.firstname = firstname;
            return this;
        }

        @java.lang.SuppressWarnings("all")
        public User.UserBuilder lastname(final String lastname) {
            this.lastname = lastname;
            return this;
        }

        @java.lang.SuppressWarnings("all")
        public User.UserBuilder username(final String username) {
            this.username = username;
            return this;
        }

        @java.lang.SuppressWarnings("all")
        public User.UserBuilder dob(final Date dob) {
            this.dob = dob;
            return this;
        }

        @java.lang.SuppressWarnings("all")
        public User.UserBuilder tel(final long tel) {
            this.tel = tel;
            return this;
        }

        @java.lang.SuppressWarnings("all")
        public User.UserBuilder customerId(final String customerId) {
            this.customerId = customerId;
            return this;
        }

        @java.lang.SuppressWarnings("all")
        public User.UserBuilder email(final String email) {
            this.email = email;
            return this;
        }

        @java.lang.SuppressWarnings("all")
        public User.UserBuilder address(final String address) {
            this.address = address;
            return this;
        }

        @java.lang.SuppressWarnings("all")
        public User.UserBuilder panNumber(final String panNumber) {
            this.panNumber = panNumber;
            return this;
        }

        @java.lang.SuppressWarnings("all")
        public User.UserBuilder aadhaarNumber(final String aadhaarNumber) {
            this.aadhaarNumber = aadhaarNumber;
            return this;
        }

        @java.lang.SuppressWarnings("all")
        public User.UserBuilder kycStatus(final KycStatus kycStatus) {
            this.kycStatus = kycStatus;
            return this;
        }

        @java.lang.SuppressWarnings("all")
        public User.UserBuilder tag(final String tag) {
            this.tag = tag;
            return this;
        }

        @JsonIgnore
        @java.lang.SuppressWarnings("all")
        public User.UserBuilder password(final String password) {
            this.password = password;
            return this;
        }

        @java.lang.SuppressWarnings("all")
        public User.UserBuilder gender(final String gender) {
            this.gender = gender;
            return this;
        }

        @java.lang.SuppressWarnings("all")
        public User.UserBuilder createdAt(final LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        @java.lang.SuppressWarnings("all")
        public User.UserBuilder updatedAt(final LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        @java.lang.SuppressWarnings("all")
        public User.UserBuilder roles(final List<String> roles) {
            this.roles = roles;
            return this;
        }

        @JsonIgnore
        @java.lang.SuppressWarnings("all")
        public User.UserBuilder card(final Card card) {
            this.card = card;
            return this;
        }

        @JsonIgnore
        @java.lang.SuppressWarnings("all")
        public User.UserBuilder transactions(final List<Transaction> transactions) {
            this.transactions = transactions;
            return this;
        }

        @JsonIgnore
        @java.lang.SuppressWarnings("all")
        public User.UserBuilder accounts(final List<Account> accounts) {
            this.accounts = accounts;
            return this;
        }

        @java.lang.SuppressWarnings("all")
        public User build() {
            return new User(this.uid, this.firstname, this.lastname, this.username, this.dob, this.tel, this.customerId, this.email, this.address, this.panNumber, this.aadhaarNumber, this.kycStatus, this.tag, this.password, this.gender, this.createdAt, this.updatedAt, this.roles, this.card, this.transactions, this.accounts);
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("all")
        public java.lang.String toString() {
            return "User.UserBuilder(uid=" + this.uid + ", firstname=" + this.firstname + ", lastname=" + this.lastname + ", username=" + this.username + ", dob=" + this.dob + ", tel=" + this.tel + ", customerId=" + this.customerId + ", email=" + this.email + ", address=" + this.address + ", panNumber=" + this.panNumber + ", aadhaarNumber=" + this.aadhaarNumber + ", kycStatus=" + this.kycStatus + ", tag=" + this.tag + ", password=" + this.password + ", gender=" + this.gender + ", createdAt=" + this.createdAt + ", updatedAt=" + this.updatedAt + ", roles=" + this.roles + ", card=" + this.card + ", transactions=" + this.transactions + ", accounts=" + this.accounts + ")";
        }
    }

    @java.lang.SuppressWarnings("all")
    public static User.UserBuilder builder() {
        return new User.UserBuilder();
    }

    @java.lang.SuppressWarnings("all")
    public String getUid() {
        return this.uid;
    }

    @java.lang.SuppressWarnings("all")
    public String getFirstname() {
        return this.firstname;
    }

    @java.lang.SuppressWarnings("all")
    public String getLastname() {
        return this.lastname;
    }

    @java.lang.SuppressWarnings("all")
    public String getUsername() {
        return this.username;
    }

    @java.lang.SuppressWarnings("all")
    public Date getDob() {
        return this.dob;
    }

    @java.lang.SuppressWarnings("all")
    public long getTel() {
        return this.tel;
    }

    @java.lang.SuppressWarnings("all")
    public String getCustomerId() {
        return this.customerId;
    }

    @java.lang.SuppressWarnings("all")
    public String getEmail() {
        return this.email;
    }

    @java.lang.SuppressWarnings("all")
    public String getAddress() {
        return this.address;
    }

    @java.lang.SuppressWarnings("all")
    public String getPanNumber() {
        return this.panNumber;
    }

    @java.lang.SuppressWarnings("all")
    public String getAadhaarNumber() {
        return this.aadhaarNumber;
    }

    @java.lang.SuppressWarnings("all")
    public KycStatus getKycStatus() {
        return this.kycStatus;
    }

    @java.lang.SuppressWarnings("all")
    public String getTag() {
        return this.tag;
    }

    @java.lang.SuppressWarnings("all")
    public String getPassword() {
        return this.password;
    }

    @java.lang.SuppressWarnings("all")
    public String getGender() {
        return this.gender;
    }

    @java.lang.SuppressWarnings("all")
    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    @java.lang.SuppressWarnings("all")
    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    @java.lang.SuppressWarnings("all")
    public List<String> getRoles() {
        return this.roles;
    }

    @java.lang.SuppressWarnings("all")
    public Card getCard() {
        return this.card;
    }

    @java.lang.SuppressWarnings("all")
    public List<Transaction> getTransactions() {
        return this.transactions;
    }

    @java.lang.SuppressWarnings("all")
    public List<Account> getAccounts() {
        return this.accounts;
    }

    @java.lang.SuppressWarnings("all")
    public void setUid(final String uid) {
        this.uid = uid;
    }

    @java.lang.SuppressWarnings("all")
    public void setFirstname(final String firstname) {
        this.firstname = firstname;
    }

    @java.lang.SuppressWarnings("all")
    public void setLastname(final String lastname) {
        this.lastname = lastname;
    }

    @java.lang.SuppressWarnings("all")
    public void setUsername(final String username) {
        this.username = username;
    }

    @java.lang.SuppressWarnings("all")
    public void setDob(final Date dob) {
        this.dob = dob;
    }

    @java.lang.SuppressWarnings("all")
    public void setTel(final long tel) {
        this.tel = tel;
    }

    @java.lang.SuppressWarnings("all")
    public void setCustomerId(final String customerId) {
        this.customerId = customerId;
    }

    @java.lang.SuppressWarnings("all")
    public void setEmail(final String email) {
        this.email = email;
    }

    @java.lang.SuppressWarnings("all")
    public void setAddress(final String address) {
        this.address = address;
    }

    @java.lang.SuppressWarnings("all")
    public void setPanNumber(final String panNumber) {
        this.panNumber = panNumber;
    }

    @java.lang.SuppressWarnings("all")
    public void setAadhaarNumber(final String aadhaarNumber) {
        this.aadhaarNumber = aadhaarNumber;
    }

    @java.lang.SuppressWarnings("all")
    public void setKycStatus(final KycStatus kycStatus) {
        this.kycStatus = kycStatus;
    }

    @java.lang.SuppressWarnings("all")
    public void setTag(final String tag) {
        this.tag = tag;
    }

    @JsonIgnore
    @java.lang.SuppressWarnings("all")
    public void setPassword(final String password) {
        this.password = password;
    }

    @java.lang.SuppressWarnings("all")
    public void setGender(final String gender) {
        this.gender = gender;
    }

    @java.lang.SuppressWarnings("all")
    public void setCreatedAt(final LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @java.lang.SuppressWarnings("all")
    public void setUpdatedAt(final LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @java.lang.SuppressWarnings("all")
    public void setRoles(final List<String> roles) {
        this.roles = roles;
    }

    @JsonIgnore
    @java.lang.SuppressWarnings("all")
    public void setCard(final Card card) {
        this.card = card;
    }

    @JsonIgnore
    @java.lang.SuppressWarnings("all")
    public void setTransactions(final List<Transaction> transactions) {
        this.transactions = transactions;
    }

    @JsonIgnore
    @java.lang.SuppressWarnings("all")
    public void setAccounts(final List<Account> accounts) {
        this.accounts = accounts;
    }

    @java.lang.SuppressWarnings("all")
    public User(final String uid, final String firstname, final String lastname, final String username, final Date dob, final long tel, final String customerId, final String email, final String address, final String panNumber, final String aadhaarNumber, final KycStatus kycStatus, final String tag, final String password, final String gender, final LocalDateTime createdAt, final LocalDateTime updatedAt, final List<String> roles, final Card card, final List<Transaction> transactions, final List<Account> accounts) {
        this.uid = uid;
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.dob = dob;
        this.tel = tel;
        this.customerId = customerId;
        this.email = email;
        this.address = address;
        this.panNumber = panNumber;
        this.aadhaarNumber = aadhaarNumber;
        this.kycStatus = kycStatus;
        this.tag = tag;
        this.password = password;
        this.gender = gender;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.roles = roles;
        this.card = card;
        this.transactions = transactions;
        this.accounts = accounts;
    }

    @java.lang.SuppressWarnings("all")
    public User() {
    }
}
