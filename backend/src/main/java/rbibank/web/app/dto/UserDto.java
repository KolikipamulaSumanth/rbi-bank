package rbibank.web.app.dto;

import jakarta.persistence.Column;
import java.util.Date;

public class UserDto {
    private String firstname;
    private String lastname;
    private String username;
    private Date dob;
    private long tel;
    private String email;
    private String address;
    private String panNumber;
    private String aadhaarNumber;
    private String role;
    private String kycStatus;
    private String accountType;
    private String password;
    private String gender;

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
    public String getRole() {
        return this.role;
    }

    @java.lang.SuppressWarnings("all")
    public String getKycStatus() {
        return this.kycStatus;
    }

    @java.lang.SuppressWarnings("all")
    public String getAccountType() {
        return this.accountType;
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
    public void setRole(final String role) {
        this.role = role;
    }

    @java.lang.SuppressWarnings("all")
    public void setKycStatus(final String kycStatus) {
        this.kycStatus = kycStatus;
    }

    @java.lang.SuppressWarnings("all")
    public void setAccountType(final String accountType) {
        this.accountType = accountType;
    }

    @java.lang.SuppressWarnings("all")
    public void setPassword(final String password) {
        this.password = password;
    }

    @java.lang.SuppressWarnings("all")
    public void setGender(final String gender) {
        this.gender = gender;
    }

    @java.lang.SuppressWarnings("all")
    public UserDto(final String firstname, final String lastname, final String username, final Date dob, final long tel, final String email, final String address, final String panNumber, final String aadhaarNumber, final String role, final String kycStatus, final String accountType, final String password, final String gender) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.dob = dob;
        this.tel = tel;
        this.email = email;
        this.address = address;
        this.panNumber = panNumber;
        this.aadhaarNumber = aadhaarNumber;
        this.role = role;
        this.kycStatus = kycStatus;
        this.accountType = accountType;
        this.password = password;
        this.gender = gender;
    }

    @java.lang.SuppressWarnings("all")
    public UserDto() {
    }
}
