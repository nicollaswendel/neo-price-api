package br.com.nicollas.neo.price.mapper;

import br.com.nicollas.neo.price.domain.model.PricingHistory;
import br.com.nicollas.neo.price.domain.pricing.PricingRequestDTO;
import br.com.nicollas.neo.price.domain.pricing.PricingResponseDTO;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PricingHistoryMapper {

    @Mapping(target = "pricingHistoryId", ignore = true)
    PricingHistory toEntity(PricingRequestDTO dto);

    @Mapping(target = "customerName", source = "customer.name")
    PricingResponseDTO toResponse(PricingHistory pricingHistory);

    List<PricingResponseDTO> toResponseList(List<PricingHistory> pricingHistories);

}
