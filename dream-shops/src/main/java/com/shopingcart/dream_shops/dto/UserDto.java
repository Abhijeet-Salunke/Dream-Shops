package com.shopingcart.dream_shops.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserDto {
    private Long id;   
    private String firstName;
    private String lastName;
    private String email;
    private List<OrderDto> orders = new ArrayList<>();
    private CartDto cart;

}
