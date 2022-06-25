package com.bosscut.service.impl;

import com.bosscut.dto.CustomerRequestDTO;
import com.bosscut.entity.Customer;
import com.bosscut.repository.CustomerRepository;
import com.bosscut.service.CustomerService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Optional<Customer> getCustomerByPhone(String phone) {
        return customerRepository.findCustomerByPhone(phone);
    }

    @Override
    public Customer create(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Customer create(CustomerRequestDTO requestDTO) {
        Customer customer = new Customer();
        customer.setPhone(requestDTO.getPhone());
        customer.setEmail(requestDTO.getEmail());
        customer.setAddress(requestDTO.getAddress());
        customer.setCustomerName(requestDTO.getFirstName() + " " + requestDTO.getLastName());
        return customerRepository.save(customer);
    }
}
