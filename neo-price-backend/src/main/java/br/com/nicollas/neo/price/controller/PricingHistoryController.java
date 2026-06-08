package br.com.nicollas.neo.price.controller;

import br.com.nicollas.neo.price.domain.dto.customer.CustomerRequestDTO;
import br.com.nicollas.neo.price.domain.dto.customer.CustomerResponseDTO;
import br.com.nicollas.neo.price.domain.pricing.PricingRequestDTO;
import br.com.nicollas.neo.price.domain.pricing.PricingResponseDTO;
import br.com.nicollas.neo.price.service.PricingHistoryService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/pricing")
public class PricingHistoryController {

    @Autowired
    private PricingHistoryService pricingHistoryService;

    @PostMapping
    public ResponseEntity<@NonNull PricingResponseDTO> create(@RequestBody PricingRequestDTO request) {
        PricingResponseDTO response = pricingHistoryService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/calculate")
    public ResponseEntity<@NonNull PricingResponseDTO> calculate(
            @RequestBody PricingRequestDTO request
    ){
        return ResponseEntity.ok(
                pricingHistoryService.calculate(request)
        );
    }

    @GetMapping
    public ResponseEntity<@NonNull List<PricingResponseDTO>> findAll(){
        List<PricingResponseDTO> pricingResponseList = pricingHistoryService.findAll();
        return ResponseEntity.ok().body(pricingResponseList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<@NonNull PricingResponseDTO> findById(@PathVariable Long id){
        PricingResponseDTO pricing = pricingHistoryService.findById(id);
        return ResponseEntity.ok().body(pricing);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<@NonNull Void> delete(@PathVariable Long id){
        pricingHistoryService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
