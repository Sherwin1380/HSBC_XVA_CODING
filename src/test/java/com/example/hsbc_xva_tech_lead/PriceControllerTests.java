package com.example.hsbc_xva_tech_lead;

import com.example.hsbc_xva_tech_lead.controller.PriceController;
import com.example.hsbc_xva_tech_lead.dto.Price;
import com.example.hsbc_xva_tech_lead.service.PriceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PriceController.class)
public class PriceControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PriceService priceService;

    @Test
    @WithMockUser
    public void current_authenticatedUser_returnsPrice() throws Exception
    {
        when(priceService.current("1")).thenReturn(new Price("1", BigDecimal.valueOf(5), 2));

        mockMvc.perform(get("/api/price/v1/current/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.instrumentId").value("1"))
                .andExpect(jsonPath("$.price").value(5))
                .andExpect(jsonPath("$.dateTime").value(2));
    }

    @Test
    @WithMockUser
    public void maximum_authenticatedUser_returnsPrice() throws Exception
    {
        when(priceService.maximum("1")).thenReturn(new Price("1", BigDecimal.valueOf(10), 1));

        mockMvc.perform(get("/api/price/v1/maximum/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.instrumentId").value("1"))
                .andExpect(jsonPath("$.price").value(10))
                .andExpect(jsonPath("$.dateTime").value(1));
    }

    @Test
    @WithMockUser
    public void minimum_authenticatedUser_returnsPrice() throws Exception
    {
        when(priceService.minimum("1")).thenReturn(new Price("1", BigDecimal.valueOf(2), 4));

        mockMvc.perform(get("/api/price/v1/minimum/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.instrumentId").value("1"))
                .andExpect(jsonPath("$.price").value(2))
                .andExpect(jsonPath("$.dateTime").value(4));
    }

    @Test
    public void current_unauthenticated_returnsUnauthorized() throws Exception
    {
        mockMvc.perform(get("/api/price/v1/current/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    public void maximum_unknownInstrument_returnsZeroPrice() throws Exception
    {
        when(priceService.maximum("UNKNOWN")).thenReturn(new Price("UNKNOWN", BigDecimal.ZERO, 0));

        mockMvc.perform(get("/api/price/v1/maximum/UNKNOWN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price").value(0))
                .andExpect(jsonPath("$.dateTime").value(0));
    }
}
