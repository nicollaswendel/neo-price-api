package br.com.nicollas.neo.price.domain.pricing;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PricingResponseDTO {

    private Long pricingHistoryId;

    private BigDecimal cost;

    private BigDecimal profitMarginPercentage;

    private BigDecimal priceWithoutTax;

    private BigDecimal taxPercentage;

    private BigDecimal salePriceWithTax;

    private BigDecimal taxAmount;

    private BigDecimal profitAmount;

    private LocalDateTime createdAt;

    private String customerName;

}
