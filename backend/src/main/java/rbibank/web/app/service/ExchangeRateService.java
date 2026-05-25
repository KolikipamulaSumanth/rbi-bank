package rbibank.web.app.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Component
public class ExchangeRateService {
    private static final Logger logger = LoggerFactory.getLogger(ExchangeRateService.class);
    private final RestTemplate restTemplate;
    private final Map<String, Double> rates = new HashMap<>(Map.of("USD", 1.0, "EUR", 0.92, "GBP", 0.79, "JPY", 156.0, "NGN", 1500.0, "INR", 83.5));
    private final Set<String> CURRENCIES = Set.of("USD", "EUR", "GBP", "JPY", "NGN", "INR");
    @Value("${currencyApiKey}")
    private String apiKey;

    public void getExchangeRate() {
        try {
            String CURRENCY_API = "https://api.currencyapi.com/v3/latest?apikey=";
            var response = restTemplate.getForEntity(CURRENCY_API + apiKey, JsonNode.class);
            var data = Objects.requireNonNull(response.getBody()).get("data");
            for (var currency : CURRENCIES) {
                var node = data.get(currency);
                if (node != null && node.get("value") != null) {
                    rates.put(currency, node.get("value").doubleValue());
                }
            }
            logger.info("Successfully fetched latest exchange rates: {}", rates);
        } catch (Exception e) {
            logger.error("Failed to fetch exchange rates from currency API, using fallback rates: {}", e.getMessage(), e);
        }
    }

    @java.lang.SuppressWarnings("all")
    public ExchangeRateService(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @java.lang.SuppressWarnings("all")
    public RestTemplate getRestTemplate() {
        return this.restTemplate;
    }

    @java.lang.SuppressWarnings("all")
    public Map<String, Double> getRates() {
        return this.rates;
    }

    @java.lang.SuppressWarnings("all")
    public Set<String> getCURRENCIES() {
        return this.CURRENCIES;
    }

    @java.lang.SuppressWarnings("all")
    public String getApiKey() {
        return this.apiKey;
    }
}
