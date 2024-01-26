package com.volvo.tax.calculator.repository;

import com.volvo.tax.calculator.data.TaxConfiguration;

public interface TaxConfigurationRepository {

  TaxConfiguration getTaxConfigurationByCity(String city);
}
