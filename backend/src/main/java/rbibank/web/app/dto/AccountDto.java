package rbibank.web.app.dto;

public class AccountDto {
    private String code;
    private String label;
    private char symbol;
    private String accountType;
    private Double openingBalance;

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
    public String getAccountType() {
        return this.accountType;
    }

    @java.lang.SuppressWarnings("all")
    public Double getOpeningBalance() {
        return this.openingBalance;
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
    public void setAccountType(final String accountType) {
        this.accountType = accountType;
    }

    @java.lang.SuppressWarnings("all")
    public void setOpeningBalance(final Double openingBalance) {
        this.openingBalance = openingBalance;
    }

    @java.lang.SuppressWarnings("all")
    public AccountDto(final String code, final String label, final char symbol, final String accountType, final Double openingBalance) {
        this.code = code;
        this.label = label;
        this.symbol = symbol;
        this.accountType = accountType;
        this.openingBalance = openingBalance;
    }

    @java.lang.SuppressWarnings("all")
    public AccountDto() {
    }
}
