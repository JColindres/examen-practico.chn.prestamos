package com.chn.prestamos.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistroRequest {

    private String username;
    private String password;
    private String rol;
}
