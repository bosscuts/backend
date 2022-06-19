package com.bosscut.service.impl;

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
}
