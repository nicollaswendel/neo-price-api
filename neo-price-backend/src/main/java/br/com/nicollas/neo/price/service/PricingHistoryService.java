package br.com.nicollas.neo.price.service;

import br.com.nicollas.neo.price.domain.model.Customer;
import br.com.nicollas.neo.price.domain.model.PricingHistory;
import br.com.nicollas.neo.price.domain.model.User;
import br.com.nicollas.neo.price.domain.pricing.PricingRequestDTO;
import br.com.nicollas.neo.price.domain.pricing.PricingResponseDTO;
import br.com.nicollas.neo.price.mapper.PricingHistoryMapper;
import br.com.nicollas.neo.price.repository.CustomerRepository;
import br.com.nicollas.neo.price.repository.PricingHistoryRepository;
import br.com.nicollas.neo.price.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PricingHistoryService {

    @Autowired
    private PricingHistoryRepository pricingHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PricingHistoryMapper pricingHistoryMapper;

    public List<PricingResponseDTO> findAll() {

        User loggedUser = getLoggedUser();

        return pricingHistoryMapper.toResponseList(
                pricingHistoryRepository.findByUser(loggedUser)
        );
    }

    public PricingResponseDTO findById(Long id) {

        User loggedUser = getLoggedUser();

        PricingHistory pricingHistory = pricingHistoryRepository
                .findBypricingHistoryIdAndUser(id, loggedUser)
                .orElseThrow(() ->
                        new EntityNotFoundException("Pricing not found."));

        return pricingHistoryMapper.toResponse(pricingHistory);
    }

    public PricingResponseDTO create(PricingRequestDTO request) {

        if (request.getCustomerId() == null) {
            throw new IllegalArgumentException(
                    "Customer is required to save a pricing."
            );
        }

        User loggedUserUser = getLoggedUser();

        User user = userRepository.findById(loggedUserUser.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found."));

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException("Customer not found."));

        PricingHistory pricingHistory =
                calculatePricing(request, user, customer);

        pricingHistoryRepository.save(pricingHistory);

        return pricingHistoryMapper.toResponse(pricingHistory);
    }

    public PricingResponseDTO calculate(PricingRequestDTO request) {

        User loggedUserUser = getLoggedUser();

        User user = userRepository.findById(loggedUserUser.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found."));

        PricingHistory pricingHistory =
                calculatePricing(request, user, null);

        return pricingHistoryMapper.toResponse(pricingHistory);
    }

    private PricingHistory calculatePricing(
            PricingRequestDTO request,
            User user,
            Customer customer
    ) {

        // Validações
        if (request.getCost() == null ||
                request.getCost().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Cost must be greater than zero.");
        }

        if (request.getProfitMarginPercentage() == null ||
                request.getProfitMarginPercentage().compareTo(BigDecimal.ZERO) <= 0 ||
                request.getProfitMarginPercentage().compareTo(new BigDecimal("100")) >= 0) {
            throw new IllegalArgumentException(
                    "Profit margin percentage must be greater than 0 and less than 100."
            );
        }

        if (request.getTaxPercentage() == null ||
                request.getTaxPercentage().compareTo(BigDecimal.ZERO) <= 0 ||
                request.getTaxPercentage().compareTo(new BigDecimal("100")) >= 0) {
            throw new IllegalArgumentException(
                    "Tax percentage must be greater than 0 and less than 100."
            );
        }

        BigDecimal ONE_HUNDRED = new BigDecimal("100");

        BigDecimal cost = request.getCost();

        BigDecimal profitMarginPercentage =
                request.getProfitMarginPercentage();

        BigDecimal taxPercentage =
                request.getTaxPercentage();

        // preço sem imposto
        BigDecimal priceWithoutTax =
                cost.divide(
                        BigDecimal.ONE.subtract(
                                profitMarginPercentage.divide(
                                        ONE_HUNDRED,
                                        10,
                                        RoundingMode.HALF_UP
                                )
                        ),
                        2,
                        RoundingMode.HALF_UP
                );

        // venda com imposto
        BigDecimal salePriceWithTax =
                priceWithoutTax.divide(
                        BigDecimal.ONE.subtract(
                                taxPercentage.divide(
                                        ONE_HUNDRED,
                                        10,
                                        RoundingMode.HALF_UP
                                )
                        ),
                        2,
                        RoundingMode.HALF_UP
                );

        // imposto em reais
        BigDecimal taxAmount =
                salePriceWithTax.subtract(priceWithoutTax);

        // lucro em reais
        BigDecimal profitAmount =
                priceWithoutTax.subtract(cost);

        PricingHistory pricingHistory = new PricingHistory();

        pricingHistory.setCost(cost);
        pricingHistory.setProfitMarginPercentage(profitMarginPercentage);

        pricingHistory.setPriceWithoutTax(priceWithoutTax);

        pricingHistory.setTaxPercentage(taxPercentage);

        pricingHistory.setSalePriceWithTax(salePriceWithTax);

        pricingHistory.setTaxAmount(taxAmount);

        pricingHistory.setProfitAmount(profitAmount);

        pricingHistory.setCreatedAt(LocalDateTime.now());

        pricingHistory.setUser(user);

        pricingHistory.setCustomer(customer);

        return pricingHistory;
    }


    public void delete(Long id) {

            User loggedUser = getLoggedUser();

            PricingHistory pricingHistory = pricingHistoryRepository
                    .findBypricingHistoryIdAndUser(id, loggedUser)
                    .orElseThrow(() ->
                            new EntityNotFoundException("Pricing not found."));

        pricingHistoryRepository.delete(pricingHistory);
        }

    private User getLoggedUser() {

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new EntityNotFoundException("User not found."));
    }

}
