package com.spacex.octopus.repository.po;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "shop")
public class ShopPO {
    @Id
    private Long id;
    private String name;
    private String phone;
    private String mobile;
    private String description;
    private Date createdTime;
    private Date updatedTime;
}
