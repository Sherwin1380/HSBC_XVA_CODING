package com.example.hsbc_xva_tech_lead.controller;

import com.example.hsbc_xva_tech_lead.dto.Price;
import com.example.hsbc_xva_tech_lead.dto.UpdatePriceRequest;
import com.example.hsbc_xva_tech_lead.service.PriceService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/price")
public class PriceController {
    private final PriceService priceService;

    public PriceController(PriceService priceService) {
        this.priceService = priceService;
    }

    @PostMapping("v1/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Price> updatePrice(@RequestBody @Valid UpdatePriceRequest request)
    {
        Price latestPrice = priceService.updatePrice(request);
        return ResponseEntity.ok(latestPrice);
    }

    @GetMapping("v1/current/{instrumentId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Price> current(@PathVariable @NotBlank String instrumentId)
    {
        Price currentPrice = priceService.current(instrumentId);
        return ResponseEntity.ok(currentPrice);
    }

    @GetMapping("v1/maximum/{instrumentId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Price> maximum(@PathVariable @NotBlank String instrumentId)
    {
        Price maximumPrice = priceService.maximum(instrumentId);
        return ResponseEntity.ok(maximumPrice);
    }

    @GetMapping("v1/minimum/{instrumentId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Price> minimum(@PathVariable @NotBlank String instrumentId)
    {
        Price minimumPrice = priceService.minimum(instrumentId);
        return ResponseEntity.ok(minimumPrice);
    }
}
