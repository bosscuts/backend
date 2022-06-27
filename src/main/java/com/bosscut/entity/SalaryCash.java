
package com.bosscut.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A SalaryCash.
 */
@Entity
@Table(name = "salary_cash")
public class SalaryCash extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long salaryCashId;

    @Column(name = "staff_id")
    private String staffId;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "cash_description")
    private String cashDescription;

    public SalaryCash() {
    }

    public Long getSalaryCashId() {
        return salaryCashId;
    }

    public void setSalaryCashId(Long salaryCashId) {
        this.salaryCashId = salaryCashId;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCashDescription() {
        return cashDescription;
    }

    public void setCashDescription(String cashDescription) {
        this.cashDescription = cashDescription;
    }
}
