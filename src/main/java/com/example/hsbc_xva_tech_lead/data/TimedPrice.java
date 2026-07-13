package com.example.hsbc_xva_tech_lead.data;

import java.math.BigDecimal;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

public class TimedPrice {
    public BigDecimal currentPrice;
    public int currentPriceTimeStamp;

    public Map<Integer, BigDecimal> updateList = new TreeMap<>();
    public Map<BigDecimal, TreeSet<Integer>> priceIndex = new TreeMap<>();
}
