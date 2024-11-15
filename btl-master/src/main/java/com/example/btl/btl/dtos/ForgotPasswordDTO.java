package com.example.btl.btl.dtos;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ForgotPasswordDTO {

    private String username;
    private String email;
    private String sdt;
}
