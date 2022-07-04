package com.bosscut.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * A Salary.
 */
@Entity
@Table(name = "salary")
public class Salary extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long salaryId;

    @Column(name = "staff_id")
    private String staffId;

    @Column(name = "amount")
    private Integer amount;

    public Salary() {
    }

    public Long getSalaryId() {
        return salaryId;
    }

    public void setSalaryId(Long salaryId) {
        this.salaryId = salaryId;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
