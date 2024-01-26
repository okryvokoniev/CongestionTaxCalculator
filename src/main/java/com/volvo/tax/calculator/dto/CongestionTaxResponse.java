package com.volvo.tax.calculator.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CongestionTaxResponse {
    private int taxAmount;
}
