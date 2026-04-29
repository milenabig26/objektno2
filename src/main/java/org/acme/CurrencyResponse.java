package org.acme;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class CurrencyResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fromCurrency;
    private String toCurrency;
    private double rate;

    private double value;          
    private double convertedValue; 

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private Users user;

    public CurrencyResponse() {}

    
    public void calculateAndSetConversion(double inputValue) {
        this.value = inputValue;
        this.convertedValue = inputValue * this.rate;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFromCurrency() { return fromCurrency; }
    public void setFromCurrency(String fromCurrency) { this.fromCurrency = fromCurrency; }

    public String getToCurrency() { return toCurrency; }
    public void setToCurrency(String toCurrency) { this.toCurrency = toCurrency; }

    public double getRate() { return rate; }
    public void setRate(double rate) { this.rate = rate; }

    public double getValue() { return value; }
    public void setValue(double value) { this.value = value; }

    public double getConvertedValue() { return convertedValue; }
    public void setConvertedValue(double convertedValue) { this.convertedValue = convertedValue; }

    public Users getUser() { return user; }
    public void setUser(Users user) { this.user = user; }
}

