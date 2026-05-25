package rbibank.web.app.dto;

public class TransferDto {
    private long recipientAccountNumber;
    private long accountNumber;
    private double amount;
    private String code;
    private String remarks;

    @java.lang.SuppressWarnings("all")
    public long getRecipientAccountNumber() {
        return this.recipientAccountNumber;
    }

    @java.lang.SuppressWarnings("all")
    public long getAccountNumber() {
        return this.accountNumber;
    }

    @java.lang.SuppressWarnings("all")
    public double getAmount() {
        return this.amount;
    }

    @java.lang.SuppressWarnings("all")
    public String getCode() {
        return this.code;
    }

    @java.lang.SuppressWarnings("all")
    public String getRemarks() {
        return this.remarks;
    }

    @java.lang.SuppressWarnings("all")
    public void setRecipientAccountNumber(final long recipientAccountNumber) {
        this.recipientAccountNumber = recipientAccountNumber;
    }

    @java.lang.SuppressWarnings("all")
    public void setAccountNumber(final long accountNumber) {
        this.accountNumber = accountNumber;
    }

    @java.lang.SuppressWarnings("all")
    public void setAmount(final double amount) {
        this.amount = amount;
    }

    @java.lang.SuppressWarnings("all")
    public void setCode(final String code) {
        this.code = code;
    }

    @java.lang.SuppressWarnings("all")
    public void setRemarks(final String remarks) {
        this.remarks = remarks;
    }

    @java.lang.SuppressWarnings("all")
    public TransferDto(final long recipientAccountNumber, final long accountNumber, final double amount, final String code, final String remarks) {
        this.recipientAccountNumber = recipientAccountNumber;
        this.accountNumber = accountNumber;
        this.amount = amount;
        this.code = code;
        this.remarks = remarks;
    }

    @java.lang.SuppressWarnings("all")
    public TransferDto() {
    }
}
