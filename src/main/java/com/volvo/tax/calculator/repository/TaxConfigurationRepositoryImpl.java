package com.volvo.tax.calculator.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.volvo.tax.calculator.data.TaxConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TaxConfigurationRepositoryImpl implements TaxConfigurationRepository {

  private static final String RESOURCE_LOADING_ERROR = "Error configuration was not loaded, source: %s";
  private final ObjectMapper mapper;
  private final ResourceLoader resourceLoader;
  private final String filePath;
  private Map<String, TaxConfiguration> configurations = new HashMap<>();

  @Autowired
  public TaxConfigurationRepositoryImpl(ObjectMapper mapper, ResourceLoader resourceLoader,
                                        @Value("${configuration.file.path:}") String filePath) {
    this.mapper = mapper;
    this.resourceLoader = resourceLoader;
    this.filePath = filePath;
  }

  @PostConstruct
  public void init() {
    try {
      Resource resource = resourceLoader.getResource(filePath);
      List<TaxConfiguration> taxConfigurations = mapper.readValue(resource.getInputStream(), new TypeReference<>() {});

      for (TaxConfiguration taxConfiguration : taxConfigurations) {
        configurations.put(taxConfiguration.getName(), taxConfiguration);
      }
    } catch (IOException e) {
      throw new RuntimeException(String.format(RESOURCE_LOADING_ERROR, filePath), e);
    }
  }

  @Override
  public TaxConfiguration getTaxConfigurationByCity(final String city) {
    return configurations.get(city);
  }
}
