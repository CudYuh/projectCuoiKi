package com.example.btl.btl.dtos;

import java.util.List;

import com.example.btl.btl.models.Shoe;
import com.example.btl.btl.models.ShoeDetail;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShoeFullDetail {
    private Shoe shoe;
    private List<ShoeDetail> shoeDetails;
}
