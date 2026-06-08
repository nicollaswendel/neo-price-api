package br.com.nicollas.neo.price.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
public class PricingHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pricingHistoryId;

    @NotNull(message = "Cost is required.")
    private BigDecimal cost;

    @NotNull(message = "Profit margin percentage is required.")
    private BigDecimal profitMarginPercentage;

    private BigDecimal priceWithoutTax;

    @NotNull(message = "Tax percentage is required.")
    private BigDecimal taxPercentage;

    private BigDecimal salePriceWithTax;

    private BigDecimal taxAmount;

    private BigDecimal profitAmount;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

}
