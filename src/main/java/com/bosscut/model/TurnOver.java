package com.bosscut.model;

public class TurnOver {
    private Integer totalSalary;
    private Integer totalCash;
    private Integer totalCompensation;
    private Integer totalCommission;
    private Integer totalRevenue;

    public TurnOver() {
        this.totalSalary = 0;
        this.totalCash = 0;
        this.totalCompensation = 0;
        this.totalCommission = 0;
        this.totalRevenue = 0;
    }

    public Integer getTotalSalary() {
        return totalSalary;
    }

    public void setTotalSalary(Integer totalSalary) {
        this.totalSalary = totalSalary;
    }

    public Integer getTotalCash() {
        return totalCash;
    }

    public void setTotalCash(Integer totalCash) {
        this.totalCash = totalCash;
    }

    public Integer getTotalCompensation() {
        return totalCompensation;
    }

    public void setTotalCompensation(Integer totalCompensation) {
        this.totalCompensation = totalCompensation;
    }

    public Integer getTotalCommission() {
        return totalCommission;
    }

    public void setTotalCommission(Integer totalCommission) {
        this.totalCommission = totalCommission;
    }

    public Integer getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(Integer totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
}
