package com.shopingcart.dream_shops.request;

//import com.shopingcart.dream_shops.model.Category;
import lombok.Data;


import java.math.BigDecimal;

@Data
public class AddProductRequest {
//    private Long id;
//    private String name;
//    private String brand;
//    private Double price;
//    private Integer inventory;
//    private String description;
//    private String category;

    private Long id;
    private String name;
    private String brand;
    private BigDecimal price;
    private int inventory;
    private String description;
    private String category;
}
