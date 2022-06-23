package com.bosscut.service.impl;

import com.bosscut.dto.InvoiceRequestDTO;
import com.bosscut.entity.Customer;
import com.bosscut.entity.Invoice;
import com.bosscut.entity.InvoiceDetail;
import com.bosscut.model.UserInvoiceDetail;
import com.bosscut.repository.InvoiceDetailRepository;
import com.bosscut.repository.InvoiceRepository;
import com.bosscut.service.CustomerService;
import com.bosscut.service.InvoiceService;
import liquibase.repackaged.org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class InvoiceServiceImpl implements InvoiceService {
    private final CustomerService customerService;
    private final InvoiceRepository invoiceRepository;
    private final InvoiceDetailRepository invoiceDetailRepository;

    public InvoiceServiceImpl(CustomerService customerService, InvoiceRepository invoiceRepository, InvoiceDetailRepository invoiceDetailRepository) {
        this.customerService = customerService;
        this.invoiceRepository = invoiceRepository;
        this.invoiceDetailRepository = invoiceDetailRepository;
    }

    @Override
    public void createInvoice(InvoiceRequestDTO invoiceRequestDTO) {
        Optional<Customer> customerOptional = customerService.getCustomerByPhone(invoiceRequestDTO.getCustomerPhone());
        Customer customer = null;
        if (customerOptional.isPresent()) {
            customer = customerOptional.get();
        } else if (!StringUtils.isBlank(invoiceRequestDTO.getCustomerPhone())){
            customer = new Customer();
            customer.setPhone(invoiceRequestDTO.getCustomerPhone());
            customerService.create(customer);
        }
        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber(String.valueOf(UUID.randomUUID()));
        invoice.setCustomerId(Objects.nonNull(customer) ? customer.getCustomerId() : null);
        invoice.setTotalAmountPayment(invoiceRequestDTO.getTotalAmountPayment());
        Invoice invoiceResult = invoiceRepository.save(invoice);

        String strServiceStaff = invoiceRequestDTO.getStrServiceStaff();
        String[] serviceStaffIdArr = strServiceStaff.split(",");

        List<String> serviceStaffIds = Arrays.asList(serviceStaffIdArr);

        List<InvoiceDetail> invoiceDetails = new ArrayList<>();
        serviceStaffIds.forEach(serviceStaffId -> {
            InvoiceDetail invoiceDetail = new InvoiceDetail();
            String[] serviceStaffArr = serviceStaffId.split("-");

            String serviceId = serviceStaffArr[0];
            String staffId = serviceStaffArr[1];

            invoiceDetail.setInvoiceId(invoiceResult.getInvoiceId());
            invoiceDetail.setProductServiceId(Long.valueOf(serviceId));
            invoiceDetail.setStaffId(Long.valueOf(staffId));
            invoiceDetail.setQuantity(1);

            invoiceDetails.add(invoiceDetail);
        });
        invoiceDetailRepository.saveAll(invoiceDetails);
    }

    @Override
    public List<UserInvoiceDetail> getInvoiceByStaffId(String staffId) {
        return invoiceDetailRepository.findByStaffIds(List.of(Long.valueOf(staffId)));
    }
}
