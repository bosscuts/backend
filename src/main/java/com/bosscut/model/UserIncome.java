package com.bosscut.model;

import java.util.Objects;

public class UserIncome {
    private String staffName;
    private Integer salary;
    private Integer cash;
    private Integer totalCompensation;
    private Integer totalCommission;

    public UserIncome() {
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public Integer getSalary() {
        return Objects.nonNull(salary) ? salary : 0;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }

    public Integer getCash() {
        return Objects.nonNull(cash) ? cash : 0;
    }

    public void setCash(Integer cash) {
        this.cash = cash;
    }

    public Integer getTotalCompensation() {
        return Objects.nonNull(totalCompensation) ? totalCompensation : 0;
    }

    public void setTotalCompensation(Integer totalCompensation) {
        this.totalCompensation = totalCompensation;
    }

    public Integer getTotalCommission() {
        return Objects.nonNull(totalCommission) ? totalCommission : 0;
    }

    public void setTotalCommission(Integer totalCommission) {
        this.totalCommission = totalCommission;
    }
}
