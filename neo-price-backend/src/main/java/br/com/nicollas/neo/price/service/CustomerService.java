package br.com.nicollas.neo.price.service;

import br.com.nicollas.neo.price.domain.dto.customer.CustomerRequestDTO;
import br.com.nicollas.neo.price.domain.dto.customer.CustomerResponseDTO;
import br.com.nicollas.neo.price.domain.model.Customer;
import br.com.nicollas.neo.price.domain.model.User;
import br.com.nicollas.neo.price.mapper.CustomerMapper;
import br.com.nicollas.neo.price.repository.CustomerRepository;
import br.com.nicollas.neo.price.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerMapper customerMapper;

    public List<CustomerResponseDTO> findAll() {

        User loggedUser = getLoggedUser();

        return customerMapper.toResponseList(
                customerRepository.findByUser(loggedUser)
        );
    }

    public CustomerResponseDTO findById(Long id) {

        User loggedUser = getLoggedUser();

        Customer customer = customerRepository
                .findByCustomerIdAndUser(id, loggedUser)
                .orElseThrow(() ->
                        new EntityNotFoundException("Customer not found."));

        return customerMapper.toResponse(customer);
    }

    public CustomerResponseDTO update(Long id, CustomerRequestDTO request) {

        User loggedUser = getLoggedUser();

        Customer customer = customerRepository
                .findByCustomerIdAndUser(id, loggedUser)
                .orElseThrow(() ->
                        new EntityNotFoundException("Customer not found."));

        customerMapper.updateCustomerFromDTO(request, customer);

        customerRepository.save(customer);

        return customerMapper.toResponse(customer);
    }

    public void delete(Long id) {

        User loggedUser = getLoggedUser();

        Customer customer = customerRepository
                .findByCustomerIdAndUser(id, loggedUser)
                .orElseThrow(() ->
                        new EntityNotFoundException("Customer not found."));

        customerRepository.delete(customer);
    }

    public CustomerResponseDTO create(CustomerRequestDTO request) {

        User loggedUser = getLoggedUser();

        Customer customer = customerMapper.toEntity(request);

        customer.setUser(loggedUser);

        customerRepository.save(customer);

        return customerMapper.toResponse(customer);
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
