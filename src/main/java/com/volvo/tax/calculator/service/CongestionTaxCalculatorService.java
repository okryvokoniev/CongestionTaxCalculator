package com.volvo.tax.calculator.service;

import com.volvo.tax.calculator.dto.CongestionTaxRequest;

public interface CongestionTaxCalculatorService {

  int getTaxAmount(CongestionTaxRequest request);
}
