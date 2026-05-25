package rbibank.web.app.dto;

public class ConvertDto {
    private String fromCurrency;
    private String toCurrency;
    private double amount;

    @java.lang.SuppressWarnings("all")
    public String getFromCurrency() {
        return this.fromCurrency;
    }

    @java.lang.SuppressWarnings("all")
    public String getToCurrency() {
        return this.toCurrency;
    }

    @java.lang.SuppressWarnings("all")
    public double getAmount() {
        return this.amount;
    }

    @java.lang.SuppressWarnings("all")
    public void setFromCurrency(final String fromCurrency) {
        this.fromCurrency = fromCurrency;
    }

    @java.lang.SuppressWarnings("all")
    public void setToCurrency(final String toCurrency) {
        this.toCurrency = toCurrency;
    }

    @java.lang.SuppressWarnings("all")
    public void setAmount(final double amount) {
        this.amount = amount;
    }

    @java.lang.SuppressWarnings("all")
    public ConvertDto(final String fromCurrency, final String toCurrency, final double amount) {
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.amount = amount;
    }

    @java.lang.SuppressWarnings("all")
    public ConvertDto() {
    }
}
