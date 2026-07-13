package com.example.hsbc_xva_tech_lead.service;

import com.example.hsbc_xva_tech_lead.data.TimedPrice;
import com.example.hsbc_xva_tech_lead.dto.Price;
import com.example.hsbc_xva_tech_lead.dto.UpdatePriceRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PriceService {

    private final Map<String, TimedPrice> priceList = new ConcurrentHashMap<>();

    public Price updatePrice(@Valid UpdatePriceRequest request) {
        if(priceList.containsKey(request.instrumentId))
        {
            TimedPrice currentTimedPrice = priceList.get(request.instrumentId);

            currentTimedPrice.updateList.put(request.timestamp, request.price);

            if(currentTimedPrice.currentPriceTimeStamp < request.timestamp)
            {
                currentTimedPrice.currentPrice = request.price;
                currentTimedPrice.currentPriceTimeStamp = request.timestamp;
                priceList.put(request.instrumentId, currentTimedPrice);
            }
        }
        else
        {
            TimedPrice timePrice = new TimedPrice();
            timePrice.currentPrice = request.price;
            timePrice.currentPriceTimeStamp = request.timestamp;
            timePrice.updateList.put(request.timestamp, request.price);

            priceList.put(request.instrumentId, timePrice);
        }

        TimedPrice lastestPrice = priceList.get(request.instrumentId);
        return new Price(request.instrumentId, lastestPrice.currentPrice, lastestPrice.currentPriceTimeStamp);
    }

    public Price current(String instrumentId)
    {
        if(priceList.containsKey(instrumentId))
        {
            TimedPrice lastestPrice = priceList.get(instrumentId);

            return new Price(instrumentId, lastestPrice.currentPrice, lastestPrice.currentPriceTimeStamp);
        }
        return new Price(instrumentId, BigDecimal.ZERO , 0);
    }


    public Price maximum(String instrumentId)
    {
        if(priceList.containsKey(instrumentId))
        {
            Map.Entry<Integer, BigDecimal> maxEntry = priceList.get(instrumentId).updateList.entrySet()
                    .stream()
                    .max(Map.Entry.comparingByValue())
                    .orElseThrow();

            return new Price(instrumentId, maxEntry.getValue(), maxEntry.getKey());
        }
        return new Price(instrumentId, BigDecimal.ZERO , 0);
    }

    public Price minimum(String instrumentId)
    {
        if(priceList.containsKey(instrumentId))
        {
            Map.Entry<Integer, BigDecimal> minEntry = priceList.get(instrumentId).updateList.entrySet()
                    .stream()
                    .min(Map.Entry.comparingByValue())
                    .orElseThrow();

            return new Price(instrumentId, minEntry.getValue(), minEntry.getKey());
        }
        return new Price(instrumentId, BigDecimal.ZERO , 0);
    }
}
