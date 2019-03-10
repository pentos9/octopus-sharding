package com.spacex.octopus.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ShopDTO {
    private Long id;
    private String name;
    private String phone;
    private String mobile;
    private String description;
    private Date createdTime;
    private Date updatedTime;
}
