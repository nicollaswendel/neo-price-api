package br.com.nicollas.neo.price.mapper;

import br.com.nicollas.neo.price.domain.dto.customer.CustomerRequestDTO;
import br.com.nicollas.neo.price.domain.dto.customer.CustomerResponseDTO;
import br.com.nicollas.neo.price.domain.model.Customer;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    @Mapping(target = "customerId", ignore = true)
    Customer toEntity(CustomerRequestDTO dto);

    CustomerResponseDTO toResponse(Customer customer);

    List<CustomerResponseDTO> toResponseList(List<Customer> customers);

    // Diz ao MapStruct para ignorar campos null vindos de um update.
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "customerId", ignore = true)
    void updateCustomerFromDTO(CustomerRequestDTO dto, @MappingTarget Customer entity);
    // O @MappingTarget está dizendo: "Atualize essa entidade existente ao invés de criar uma nova".

}
