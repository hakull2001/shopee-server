package com.shopee.request.email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendEmailRequest {
    @NotBlank
    private String to;

    @NotBlank
    private String content;

    private String title;

}
