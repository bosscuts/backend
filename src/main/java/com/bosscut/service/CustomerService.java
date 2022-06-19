package com.bosscut.service;

import com.bosscut.entity.Customer;
import java.util.Optional;

public interface CustomerService {
    Optional<Customer> getCustomerByPhone(String phone);
    Customer create(Customer customer);
}
