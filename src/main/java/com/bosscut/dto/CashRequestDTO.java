package com.bosscut.dto;

import java.math.BigDecimal;

public class CashRequestDTO {
    private String staffId;
    private BigDecimal cashAmount;
    private String cashDescription;

    public CashRequestDTO() {
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public BigDecimal getCashAmount() {
        return cashAmount;
    }

    public void setCashAmount(BigDecimal cashAmount) {
        this.cashAmount = cashAmount;
    }

    public String getCashDescription() {
        return cashDescription;
    }

    public void setCashDescription(String cashDescription) {
        this.cashDescription = cashDescription;
    }
}
