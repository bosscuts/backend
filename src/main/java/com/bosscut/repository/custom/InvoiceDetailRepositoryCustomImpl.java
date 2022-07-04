package com.bosscut.repository.custom;

import com.bosscut.model.UserIncome;
import com.bosscut.model.UserInvoiceDetail;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Repository
public class InvoiceDetailRepositoryCustomImpl implements InvoiceDetailRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<UserInvoiceDetail> findByStaffIds(List<Long> userIds, LocalDateTime startOfDay, LocalDateTime endOfDay) {
        String sql = "SELECT INVOICE.INVOICE_NUMBER, INVOICE.TOTAL_AMOUNT_PAYMENT, INVOICE.USER_ID , PRODUCT_SERVICE.PRODUCT_SERVICE_NAME, " +
                "PRODUCT_SERVICE.PRICE, INVOICE_DETAIL.QUANTITY, PRODUCT_SERVICE.DESCRIPTION, INVOICE_DETAIL.INVOICE_DETAIL_ID, INVOICE_DETAIL.STAFF_ID, " +
                "INVOICE_DETAIL.CREATED_DATE,  CUSTOMER.CUSTOMER_NAME, CUSTOMER.PHONE, CUSTOMER.ADDRESS, \"user\".FIRST_NAME, \"user\".LAST_NAME FROM INVOICE_DETAIL " +
                "join INVOICE on INVOICE.INVOICE_ID = INVOICE_DETAIL.INVOICE_ID " +
                "join CUSTOMER on CUSTOMER.\"CUSTOMER_ID\" = INVOICE.\"CUSTOMER_ID\" " +
                "join \"user\" on \"user\".\"ID\" = INVOICE_DETAIL.\"STAFF_ID\" " +
                "join PRODUCT_SERVICE on INVOICE_DETAIL.PRODUCT_SERVICE_ID  = PRODUCT_SERVICE.PRODUCT_SERVICE_ID " +
                "where INVOICE_DETAIL.STAFF_ID in (:userIds) " +
                "and INVOICE_DETAIL.CREATED_DATE BETWEEN :startOfDay and :endOfDay";

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("userIds", userIds);
        query.setParameter("startOfDay", startOfDay);
        query.setParameter("endOfDay", endOfDay);
        List<Object[]> listData = query.getResultList();
        if (!CollectionUtils.isEmpty(listData)) {
            List<UserInvoiceDetail> invoiceDetails = new ArrayList<>();
            listData.forEach(data -> {
                UserInvoiceDetail invoiceDetail = new UserInvoiceDetail();
                if (Objects.nonNull(data[0])) {
                    invoiceDetail.setInvoiceNumber(String.valueOf(data[0]));
                }
                if (Objects.nonNull(data[1])) {

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
                if (Objects.nonNull(data[9])) {
                    invoiceDetail.setCreatedDate(String.valueOf(data[9]));
                }
                invoiceDetail.setTotalAmountPayment(new BigDecimal(invoiceDetail.getQuantity() * invoiceDetail.getPrice()));
                invoiceDetail.setStaffName(data[13] + " " + data[14]);
                invoiceDetails.add(invoiceDetail);
            });
            return invoiceDetails;
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    @Transactional
    public List<UserIncome> findByStaffIdsAndRequestType(List<Long> staffIds, String requestType, LocalDateTime startOfDay, LocalDateTime endOfDay) {
        String sql = "SELECT u.FIRST_NAME, u.LAST_NAME, s.AMOUNT as salary " +
                " FROM \"user\" u INNER JOIN SALARY s ON s.STAFF_ID = u.ID " +
                " WHERE u.ID IN (:staffIds) ";
//                " AND id.CREATED_DATE BETWEEN :startOfDay and :endOfDay GROUP BY id.STAFF_ID";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("staffIds", staffIds);
//        query.setParameter("startOfDay", startOfDay);
//        query.setParameter("endOfDay", endOfDay);
        List<Object[]> listData = query.getResultList();
        List<UserIncome> incomes = new ArrayList<>();

        if (!CollectionUtils.isEmpty(listData)) {
            listData.forEach(data -> {
                UserIncome userIncome = new UserIncome();
                if (Objects.nonNull(data[0])) {
                    userIncome.setStaffName(data[0] + " " + data[1]);
                }
                if (Objects.nonNull(data[2])) {
                    userIncome.setSalary(Integer.valueOf(String.valueOf(data[2])));
                }
                incomes.add(userIncome);
            });
        }
        return incomes;
    }
}
