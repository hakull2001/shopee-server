package com.shopee.request.user;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class ForgotPasswordRequest {
    @NotBlank
    private String email;
}
