package br.com.nicollas.neo.price.controller;

import br.com.nicollas.neo.price.domain.dto.customer.CustomerRequestDTO;
import br.com.nicollas.neo.price.domain.dto.customer.CustomerResponseDTO;
import br.com.nicollas.neo.price.service.CustomerService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping
    public ResponseEntity<@NonNull CustomerResponseDTO> create(@RequestBody CustomerRequestDTO request) {
        CustomerResponseDTO response = customerService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<@NonNull List<CustomerResponseDTO>> findAll(){
        List<CustomerResponseDTO> customers = customerService.findAll();
        return ResponseEntity.ok().body(customers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<@NonNull CustomerResponseDTO> findById(@PathVariable Long id){
        CustomerResponseDTO customer = customerService.findById(id);
        return ResponseEntity.ok().body(customer);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<@NonNull CustomerResponseDTO> update(@PathVariable Long id, @RequestBody CustomerRequestDTO request) {
        CustomerResponseDTO response = customerService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<@NonNull Void> delete(@PathVariable Long id){
        customerService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
