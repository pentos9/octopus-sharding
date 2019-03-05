package com.spacex.octopus.repository.po;

import lombok.Data;

import javax.persistence.Id;
import java.util.Date;

@Data
public class ProductPO {
    @Id
    private Long id;
    private Long productId;
    private Long price;
    private Date createdTime;
    private Date updatedTime;
}
