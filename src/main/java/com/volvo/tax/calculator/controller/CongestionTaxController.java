package com.volvo.tax.calculator.controller;

import com.volvo.tax.calculator.dto.CongestionTaxRequest;
import com.volvo.tax.calculator.dto.CongestionTaxResponse;
import com.volvo.tax.calculator.service.CongestionTaxCalculatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/calculator")
public class CongestionTaxController {

    private final CongestionTaxCalculatorService service;

    @PostMapping
    public CongestionTaxResponse getTax(@Valid @RequestBody CongestionTaxRequest request) {
        int taxAmount = service.getTaxAmount(request);
        return CongestionTaxResponse.builder()
                .taxAmount(taxAmount)
                .build();
    }

}
