package com.bosscut.service.impl;

import com.bosscut.dto.InvoiceInternalRequest;
import com.bosscut.dto.InvoicePreview;
import com.bosscut.dto.InvoiceExternalRequest;
import com.bosscut.dto.ServicePreview;
import com.bosscut.entity.Customer;
import com.bosscut.entity.Invoice;
import com.bosscut.entity.InvoiceDetail;
import com.bosscut.entity.ProductService;
import com.bosscut.enums.InvoiceType;
import com.bosscut.model.UserInvoiceDetail;
import com.bosscut.repository.InvoiceDetailRepository;
import com.bosscut.repository.InvoiceRepository;
import com.bosscut.service.CustomerService;
import com.bosscut.service.InvoiceService;
import com.bosscut.service.ProductServiceService;
import liquibase.repackaged.org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class InvoiceServiceImpl implements InvoiceService {
    private final CustomerService customerService;
    private final InvoiceRepository invoiceRepository;
    private final InvoiceDetailRepository invoiceDetailRepository;
    private final ProductServiceService productServiceService;

    public InvoiceServiceImpl(CustomerService customerService, InvoiceRepository invoiceRepository,
                              InvoiceDetailRepository invoiceDetailRepository, ProductServiceService productServiceService) {
        this.customerService = customerService;
        this.invoiceRepository = invoiceRepository;
        this.invoiceDetailRepository = invoiceDetailRepository;
        this.productServiceService = productServiceService;
    }

    @Override
    public void createInvoice(InvoiceExternalRequest invoiceRequestDTO) {
        Optional<Customer> customerOptional = customerService.getCustomerByPhone(invoiceRequestDTO.getCustomerPhone());
        Customer customer = null;
        if (customerOptional.isPresent()) {
            customer = customerOptional.get();
        } else if (!StringUtils.isBlank(invoiceRequestDTO.getCustomerPhone())) {
            customer = new Customer();
            customer.setPhone(invoiceRequestDTO.getCustomerPhone());
            customerService.create(customer);
        }
        Invoice invoice = new Invoice();
        invoice.setInvoiceType(InvoiceType.EXTERNAL.getName());
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
    public void createInvoiceInternal(InvoiceInternalRequest requestDTO) {
        Invoice invoice = new Invoice();
        invoice.setInvoiceType(InvoiceType.EXTERNAL.getName());
        invoice.setInvoiceNumber(String.valueOf(UUID.randomUUID()));
        invoice.setUserId(requestDTO.getUserId());

        Invoice invoiceResult = invoiceRepository.save(invoice);

        InvoiceDetail invoiceDetail = new InvoiceDetail();
        invoiceDetail.setInvoiceId(invoiceResult.getInvoiceId());
        invoiceDetail.setStaffId(requestDTO.getUserId());
        invoiceDetail.setAmount(requestDTO.getAmount());
        invoiceDetail.setRequestType(requestDTO.getRequestType());
        invoiceDetail.setDescription(requestDTO.getDescription());

        invoiceDetailRepository.save(invoiceDetail);
    }

    @Override
    public InvoicePreview previewInvoice(InvoiceExternalRequest requestDTO) {
        String strServiceStaff = requestDTO.getStrServiceStaff();
        String[] serviceStaffIdArr = strServiceStaff.split(",");
        List<String> serviceStaffIds = Arrays.asList(serviceStaffIdArr);

        List<Long> serviceIds = new ArrayList<>();
        serviceStaffIds.forEach(serviceStaffId -> {
            String[] serviceStaffArr = serviceStaffId.split("-");
            String serviceId = serviceStaffArr[0];

            serviceIds.add(Long.valueOf(serviceId));
        });

        List<ProductService> productServices = productServiceService.findAllByIds(serviceIds);

        int totalAmount = productServices.stream().mapToInt(ProductService::getPrice).sum();

        InvoicePreview invoicePreview = new InvoicePreview();
        List<ServicePreview> servicePreviews = new ArrayList<>();

        productServices.forEach(item -> {
            ServicePreview servicePreviewDTO = new ServicePreview();
            servicePreviewDTO.setServiceProductName(item.getProductServiceName());
            servicePreviewDTO.setQuantity(1);
            servicePreviewDTO.setTotalPrice(item.getPrice() * servicePreviewDTO.getQuantity());
            servicePreviews.add(servicePreviewDTO);
        });

        invoicePreview.setServicePreviews(servicePreviews);
        invoicePreview.setTotalAmount(totalAmount);
        return invoicePreview;
    }

    @Override
    public List<UserInvoiceDetail> getInvoiceByStaffId(String staffIds) {
        LocalDateTime startOfDay = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        String[] staffIdArr = staffIds.split(",");
        List<Long> idStaffs = Stream.of(staffIdArr).map(Long::valueOf).collect(Collectors.toList());

        return invoiceDetailRepository.findByStaffIds(idStaffs, startOfDay, endOfDay);
    }

    @Override
    public List<UserInvoiceDetail> getInvoiceByStaffIdInMonth(String staffIds) {
        LocalDate initial = LocalDate.now();
        LocalDateTime startOfMonth = initial.withDayOfMonth(1).atStartOfDay();
        LocalDateTime endOfMonth = initial.withDayOfMonth(initial.getMonth().length(initial.isLeapYear()))
                .atStartOfDay().withHour(23).withMinute(59).withSecond(59).withNano(999);

        String[] staffIdArr = staffIds.split(",");
        List<Long> idStaffs = Stream.of(staffIdArr).map(Long::valueOf).collect(Collectors.toList());

        return invoiceDetailRepository.findByStaffIds(idStaffs, startOfMonth, endOfMonth);
    }
}
