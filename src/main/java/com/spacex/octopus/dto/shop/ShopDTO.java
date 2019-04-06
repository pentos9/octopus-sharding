package com.spacex.octopus.dto.shop;

import lombok.Data;

import java.util.Date;

@Data
public class ShopDTO {
    private Long shopId;
    private String name;
    private String phone;
    private String mobile;
    private String description;
    private Date createdTime;
    private Date updatedTime;
}
