package com.shopee.request.user;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class ResetPasswordRequest {
    @NotBlank
    private String newPassword;

    @NotBlank
    private String confirmPassword;
}
