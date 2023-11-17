package com.example.phase2.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.CreditCardNumber;
import org.springframework.format.annotation.NumberFormat;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BankCardResponseDto {
    @NotBlank
    String creditCardNumber;
}
