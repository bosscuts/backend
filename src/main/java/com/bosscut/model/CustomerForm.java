package com.bosscut.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.bosscut.validator.Phone;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class CustomerForm {

    @Size(min=2, max=30)
    private String name;

    private String email;

    @NotNull
    @Min(18)
    @Max(100)
    private Integer age;

    @Phone(message = "Số điện thoại không hợp lệ!")
    private String phone;
}