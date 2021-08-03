package com.practice.modulebase.vm;

import lombok.Data;

@Data
public class JwtTokenVM {

    private String token;
    private String refreshToken;
}
