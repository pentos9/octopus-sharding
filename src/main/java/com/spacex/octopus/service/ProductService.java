package com.spacex.octopus.service;

import com.spacex.octopus.repository.po.ProductPO;

import java.util.List;

public interface ProductService {
    ProductPO get(Long productId);

    List<ProductPO> getByShopId(Long shopId);
}
