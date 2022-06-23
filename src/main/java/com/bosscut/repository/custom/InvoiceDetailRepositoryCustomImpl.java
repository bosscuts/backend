package com.bosscut.repository.custom;

import com.bosscut.model.UserInvoiceDetail;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.*;

@Repository
public class InvoiceDetailRepositoryCustomImpl implements InvoiceDetailRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<UserInvoiceDetail> findByStaffIds(List<Long> userIds) {
        String sql = "SELECT INVOICE.INVOICE_NUMBER, INVOICE.TOTAL_AMOUNT_PAYMENT, INVOICE.USER_ID , PRODUCT_SERVICE.PRODUCT_SERVICE_NAME, " +
                "PRODUCT_SERVICE.PRICE, PRODUCT_SERVICE.QUANTITY, PRODUCT_SERVICE.DESCRIPTION, INVOICE_DETAIL.INVOICE_DETAIL_ID, INVOICE_DETAIL.STAFF_ID, " +
                "INVOICE_DETAIL.CREATED_DATE,  CUSTOMER.CUSTOMER_NAME, CUSTOMER.PHONE, CUSTOMER.ADDRESS  FROM INVOICE_DETAIL " +
                "join INVOICE on INVOICE.INVOICE_ID = INVOICE_DETAIL.INVOICE_ID " +
                "join CUSTOMER on CUSTOMER.\"CUSTOMER_ID\"  = INVOICE.\"CUSTOMER_ID\" " +
                "join PRODUCT_SERVICE  on INVOICE_DETAIL.PRODUCT_SERVICE_ID  = PRODUCT_SERVICE.PRODUCT_SERVICE_ID " +
                "where INVOICE_DETAIL.STAFF_ID in (:userIds)";

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("userIds", userIds);
        List<Object[]> listData = query.getResultList();

        if (!CollectionUtils.isEmpty(listData)) {
            List<UserInvoiceDetail> invoiceDetails = new ArrayList<>();
            listData.forEach(data -> {
                UserInvoiceDetail invoiceDetail = new UserInvoiceDetail();
                if (Objects.nonNull(data[0])) {
                    invoiceDetail.setInvoiceNumber(String.valueOf(data[0]));
                }
                if (Objects.nonNull(data[1])) {
                    invoiceDetail.setTotalAmountPayment(new BigDecimal(String.valueOf(data[1])));
                }
                if (Objects.nonNull(data[2])) {
                    invoiceDetail.setUserId(Long.valueOf(String.valueOf(data[2])));
                }
                if (Objects.nonNull(data[3])) {
                    invoiceDetail.setProductServiceName(String.valueOf(data[3]));
                }
                if (Objects.nonNull(data[4])) {
                    invoiceDetail.setPrice(Integer.valueOf(String.valueOf(data[4])));
                }
                if (Objects.nonNull(data[5])) {
                    invoiceDetail.setQuantity(Integer.valueOf(String.valueOf(data[5])));
                }
                if (Objects.nonNull(data[6])) {
                    invoiceDetail.setDescription(String.valueOf(data[6]));
                }
                if (Objects.nonNull(data[10])) {
                    invoiceDetail.setDescription(String.valueOf(data[10]));
                }
                if (Objects.nonNull(data[11])) {
                    invoiceDetail.setPhone(String.valueOf(data[11]));
                }
                if (Objects.nonNull(data[12])) {
                    invoiceDetail.setAddress(String.valueOf(data[12]));
                }
                invoiceDetails.add(invoiceDetail);
            });
            return invoiceDetails;
        } else {
            return Collections.emptyList();
        }
    }
}
