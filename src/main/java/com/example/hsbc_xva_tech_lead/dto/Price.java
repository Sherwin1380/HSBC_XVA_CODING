package com.example.hsbc_xva_tech_lead.dto;

import java.math.BigDecimal;

public record Price (String instrumentId, BigDecimal price, int dateTime) {
}
