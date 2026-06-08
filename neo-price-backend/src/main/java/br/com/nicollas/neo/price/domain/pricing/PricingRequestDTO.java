package br.com.nicollas.neo.price.domain.pricing;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PricingRequestDTO {

    private BigDecimal cost;

    private BigDecimal profitMarginPercentage;

    private BigDecimal taxPercentage;

    private Long customerId;

}
