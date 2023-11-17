package com.example.phase2.dto;

import jakarta.validation.constraints.*;
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
public class BankCardRequestDto {
    @Positive
    Long orderId;
    @NotBlank
    @Size(min = 16, max = 16, message = "secondPassword length must be 16.")
    String creditCardNumber;
    @NotBlank
    @NumberFormat
    @Size(min = 3, max = 4, message = "secondPassword length must be between 3 and 4 characters.")
    String cvv2;
    @NumberFormat
    @Size(min = 6, max = 12, message = "secondPassword length must be between 6 and 12 characters.")
    @NotBlank
    String secondPassword;
    Integer captchaId;
    String captcha;
}
