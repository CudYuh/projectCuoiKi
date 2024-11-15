package com.example.btl.btl.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminChangePassForm {
    private String oldPass;
    private String newPass;
    private String confirmNewPass;
}
