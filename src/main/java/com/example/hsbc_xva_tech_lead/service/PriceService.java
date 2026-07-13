package com.example.hsbc_xva_tech_lead.service;

import com.example.hsbc_xva_tech_lead.data.TimedPrice;
import com.example.hsbc_xva_tech_lead.dto.Price;
import com.example.hsbc_xva_tech_lead.dto.UpdatePriceRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PriceService {

    private final Map<String, TimedPrice> priceList = new ConcurrentHashMap<>();

    public Price updatePrice(@Valid UpdatePriceRequest request) {
        if(priceList.containsKey(request.instrumentId))
        {
            TimedPrice currentTimedPrice = priceList.get(request.instrumentId);

            indexPrice(currentTimedPrice, request.timestamp, request.price);

            if(currentTimedPrice.currentPriceTimeStamp <= request.timestamp)
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
            indexPrice(timePrice, request.timestamp, request.price);

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
            TreeMap<BigDecimal, TreeSet<Integer>> priceIndex = (TreeMap<BigDecimal, TreeSet<Integer>>) priceList.get(instrumentId).priceIndex;

            Map.Entry<BigDecimal, TreeSet<Integer>> highest = priceIndex.lastEntry();

            return new Price(instrumentId, highest.getKey(), highest.getValue().first());
        }
        return new Price(instrumentId, BigDecimal.ZERO , 0);
    }

    public Price minimum(String instrumentId)
    {
        if(priceList.containsKey(instrumentId))
        {
            TreeMap<BigDecimal, TreeSet<Integer>> priceIndex = (TreeMap<BigDecimal, TreeSet<Integer>>) priceList.get(instrumentId).priceIndex;

            Map.Entry<BigDecimal, TreeSet<Integer>> lowest = priceIndex.firstEntry();

            return new Price(instrumentId, lowest.getKey(), lowest.getValue().first());
        }
        return new Price(instrumentId, BigDecimal.ZERO , 0);
    }

    private void indexPrice(TimedPrice timedPrice, int timestamp, BigDecimal price)
    {
        BigDecimal previousPrice = timedPrice.updateList.get(timestamp);

        if(previousPrice != null)
        {
            TreeSet<Integer> timestamps = (TreeSet<Integer>) timedPrice.priceIndex.get(previousPrice);
            timestamps.remove(timestamp);
            if(timestamps.isEmpty())
            {
                timedPrice.priceIndex.remove(previousPrice);
            }
        }

        timedPrice.updateList.put(timestamp, price);
        timedPrice.priceIndex.computeIfAbsent(price, key -> new TreeSet<>()).add(timestamp);
    }
}
