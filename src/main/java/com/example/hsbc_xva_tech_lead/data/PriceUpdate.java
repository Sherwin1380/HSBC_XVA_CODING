package com.example.hsbc_xva_tech_lead.data;

import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PriceUpdate {
    public BigDecimal price;
    public int timestamp;

    public PriceUpdate(@Positive BigDecimal price, int timestamp) {
        this.price = price;
        this.timestamp = timestamp;
    }
}
