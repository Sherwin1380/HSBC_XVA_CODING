package com.example.hsbc_xva_tech_lead.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class UpdatePriceRequest {
    @NotBlank
    public String instrumentId;

    @NotNull
    @DecimalMin(value = "1")
    @DecimalMax(value = "1000000000")
    public BigDecimal price;

    @Min(1)
    @Max(1000000000)
    public int timestamp;

    public UpdatePriceRequest(String instrumentId, BigDecimal price, int timestamp) {
        this.instrumentId = instrumentId;
        this.price = price;
        this.timestamp = timestamp;
    }
}
