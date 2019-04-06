package com.spacex.octopus.dto.shop;

import lombok.Data;

@Data
public class ShopCreateDTO {
    private String name;
    private String phone;
    private String mobile;
    private String description;
    private Long cityId;
}
