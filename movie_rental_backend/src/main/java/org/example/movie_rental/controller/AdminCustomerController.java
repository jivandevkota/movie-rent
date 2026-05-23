package org.example.movie_rental.controller;

import lombok.RequiredArgsConstructor;
import org.example.movie_rental.dto.CustomerRentalDto;
import org.example.movie_rental.entity.Customer;
import org.example.movie_rental.repository.CustomerRepository;
import org.example.movie_rental.service.RentalService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/customers")
@RequiredArgsConstructor
public class AdminCustomerController {

    private final CustomerRepository customerRepository;
    private final RentalService rentalService;

    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<Page<Map<String, Object>>> getAllCustomers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "customerId"));
        Page<Map<String, Object>> result = customerRepository.findAll(pageable)
                .map(c -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("customerId", c.getCustomerId());
                    m.put("firstName", c.getFirstName());
                    m.put("lastName", c.getLastName());
                    m.put("email", c.getEmail());
                    m.put("active", c.getActive());
                    m.put("createDate", c.getCreateDate());
                    return m;
                });
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{customerId}")
    @Transactional(readOnly = true)
    public ResponseEntity<Map<String, Object>> getCustomer(@PathVariable Integer customerId) {
        Customer c = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found: " + customerId));
        Map<String, Object> m = new HashMap<>();
        m.put("customerId", c.getCustomerId());
        m.put("firstName", c.getFirstName());
        m.put("lastName", c.getLastName());
        m.put("email", c.getEmail());
        m.put("active", c.getActive());
        m.put("createDate", c.getCreateDate());
        if (c.getAddress() != null) {
            m.put("address", c.getAddress().getAddress());
            m.put("address2", c.getAddress().getAddress2());
            m.put("district", c.getAddress().getDistrict());
            m.put("postalCode", c.getAddress().getPostalCode());
            m.put("phone", c.getAddress().getPhone());
            if (c.getAddress().getCity() != null) {
                m.put("city", c.getAddress().getCity().getCity());
                if (c.getAddress().getCity().getCountry() != null) {
                    m.put("country", c.getAddress().getCity().getCountry().getCountry());
                }
            }
        }
        return ResponseEntity.ok(m);
    }

    @GetMapping("/{customerId}/rentals")
    public ResponseEntity<List<CustomerRentalDto>> getCustomerRentals(@PathVariable Integer customerId) {
        return ResponseEntity.ok(rentalService.getRentalsByCustomerId(customerId));
    }

    @PatchMapping("/{customerId}")
    @Transactional
    public ResponseEntity<Map<String, Object>> updateCustomer(
            @PathVariable Integer customerId,
            @RequestBody Map<String, Object> body) {
        Customer c = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found: " + customerId));
        if (body.containsKey("active")) {
            c.setActive((Boolean) body.get("active"));
        }
        if (body.containsKey("firstName")) {
            c.setFirstName((String) body.get("firstName"));
        }
        if (body.containsKey("lastName")) {
            c.setLastName((String) body.get("lastName"));
        }
        if (body.containsKey("email")) {
            c.setEmail((String) body.get("email"));
        }
        customerRepository.save(c);
        Map<String, Object> m = new HashMap<>();
        m.put("customerId", c.getCustomerId());
        m.put("firstName", c.getFirstName());
        m.put("lastName", c.getLastName());
        m.put("email", c.getEmail());
        m.put("active", c.getActive());
        return ResponseEntity.ok(m);
    }
}
