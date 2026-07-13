package com.example.hsbc_xva_tech_lead;

import com.example.hsbc_xva_tech_lead.dto.Price;
import com.example.hsbc_xva_tech_lead.dto.UpdatePriceRequest;
import com.example.hsbc_xva_tech_lead.service.PriceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PriceServiceTests {
    private PriceService service;

    @BeforeEach
    public void setup()
    {
        service = new PriceService();
    }

    @Test
    public void current_unknownInstrument_returnsZeroPrice()
    {
        Price price = service.current("UNKNOWN");

        assertEquals(new Price("UNKNOWN", BigDecimal.ZERO, 0), price);
    }

    @Test
    public void maximum_unknownInstrument_returnsZeroPrice()
    {
        Price price = service.maximum("UNKNOWN");

        assertEquals(new Price("UNKNOWN", BigDecimal.ZERO, 0), price);
    }

    @Test
    public void minimum_unknownInstrument_returnsZeroPrice()
    {
        Price price = service.minimum("UNKNOWN");

        assertEquals(new Price("UNKNOWN", BigDecimal.ZERO, 0), price);
    }

    @Test
    public void updatePrice_firstUpdate_becomesCurrentPrice()
    {
        Price price = service.updatePrice(new UpdatePriceRequest("1", BigDecimal.valueOf(10), 1));

        assertEquals(new Price("1", BigDecimal.valueOf(10), 1), price);
        assertEquals(new Price("1", BigDecimal.valueOf(10), 1), service.current("1"));
    }

    @Test
    public void updatePrice_newerTimestamp_updatesCurrentPrice()
    {
        service.updatePrice(new UpdatePriceRequest("1", BigDecimal.valueOf(10), 1));
        service.updatePrice(new UpdatePriceRequest("1", BigDecimal.valueOf(5), 2));

        assertEquals(new Price("1", BigDecimal.valueOf(5), 2), service.current("1"));
    }

    @Test
    public void updatePrice_olderTimestamp_doesNotUpdateCurrentPrice()
    {
        service.updatePrice(new UpdatePriceRequest("1", BigDecimal.valueOf(10), 1));
        service.updatePrice(new UpdatePriceRequest("1", BigDecimal.valueOf(5), 2));
        service.updatePrice(new UpdatePriceRequest("1", BigDecimal.valueOf(3), 1));

        assertEquals(new Price("1", BigDecimal.valueOf(5), 2), service.current("1"));
    }

    @Test
    public void updatePrice_sameTimestamp_overwritesValueAtThatTimestamp()
    {
        service.updatePrice(new UpdatePriceRequest("1", BigDecimal.valueOf(10), 1));
        service.updatePrice(new UpdatePriceRequest("1", BigDecimal.valueOf(3), 1));

        assertEquals(new Price("1", BigDecimal.valueOf(3), 1), service.maximum("1"));
    }

    @Test
    public void maximum_returnsHighestPriceAcrossAllUpdates()
    {
        service.updatePrice(new UpdatePriceRequest("1", BigDecimal.valueOf(10), 1));
        service.updatePrice(new UpdatePriceRequest("1", BigDecimal.valueOf(5), 2));

        assertEquals(new Price("1", BigDecimal.valueOf(10), 1), service.maximum("1"));
    }

    @Test
    public void minimum_returnsLowestPriceAcrossAllUpdates()
    {
        service.updatePrice(new UpdatePriceRequest("1", BigDecimal.valueOf(10), 1));
        service.updatePrice(new UpdatePriceRequest("1", BigDecimal.valueOf(5), 2));
        service.updatePrice(new UpdatePriceRequest("1", BigDecimal.valueOf(2), 4));

        assertEquals(new Price("1", BigDecimal.valueOf(2), 4), service.minimum("1"));
    }

    @Test
    public void maximumAndMinimum_areIndependentPerInstrument()
    {
        service.updatePrice(new UpdatePriceRequest("1", BigDecimal.valueOf(10), 1));
        service.updatePrice(new UpdatePriceRequest("2", BigDecimal.valueOf(100), 1));

        assertEquals(new Price("1", BigDecimal.valueOf(10), 1), service.maximum("1"));
        assertEquals(new Price("2", BigDecimal.valueOf(100), 1), service.maximum("2"));
    }

    /**
     * Documented scenario:
     * ["StockPrice", "update", "update", "current", "maximum", "update", "maximum", "update", "minimum"]
     * [[], [1, 10], [2, 5], [], [], [1, 3], [], [4, 2], []]
     * Output: [null, null, null, 5, 10, null, 5, null, 2]
     */
    @Test
    public void testCorrectFlow()
    {
        service.updatePrice(new UpdatePriceRequest("1", BigDecimal.valueOf(10), 1));
        service.updatePrice(new UpdatePriceRequest("1", BigDecimal.valueOf(5), 2));

        assertEquals(BigDecimal.valueOf(5), service.current("1").price());
        assertEquals(BigDecimal.valueOf(10), service.maximum("1").price());

        service.updatePrice(new UpdatePriceRequest("1", BigDecimal.valueOf(3), 1));

        assertEquals(BigDecimal.valueOf(5), service.maximum("1").price());

        service.updatePrice(new UpdatePriceRequest("1", BigDecimal.valueOf(2), 4));

        assertEquals(BigDecimal.valueOf(2), service.minimum("1").price());
    }
}
