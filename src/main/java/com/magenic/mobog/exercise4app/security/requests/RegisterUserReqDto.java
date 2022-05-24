package com.magenic.mobog.exercise4app.security.requests;

import lombok.Builder;
import lombok.Data;

@Data
public class RegisterUserReqDto {
    private String username;
    private String password;
}
