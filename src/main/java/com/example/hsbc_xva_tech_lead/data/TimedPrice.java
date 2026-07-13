package com.example.hsbc_xva_tech_lead.data;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TimedPrice {
    public BigDecimal currentPrice;
    public int currentPriceTimeStamp;

    public Map<Integer, BigDecimal> updateList = new ConcurrentHashMap<>();
}
