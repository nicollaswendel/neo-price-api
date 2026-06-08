package br.com.nicollas.neo.price.domain.dto.customer;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CustomerRequestDTO {

    @NotBlank(message = "Customer name is required.")
    private String name;

    private String companyName;

}
