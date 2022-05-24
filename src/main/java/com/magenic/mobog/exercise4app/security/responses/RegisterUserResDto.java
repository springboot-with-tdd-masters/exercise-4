package com.magenic.mobog.exercise4app.security.responses;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterUserResDto {
    private String username;
    private String userId;
}
