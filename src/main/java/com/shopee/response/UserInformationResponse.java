package com.shopee.response;

import com.shopee.entity.User;
import com.shopee.enumerations.RoleUser;
import com.shopee.enumerations.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInformationResponse {
    private String token;

    private User user;

}
