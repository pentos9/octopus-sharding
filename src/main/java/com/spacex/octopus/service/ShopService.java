package com.spacex.octopus.service;

import com.spacex.octopus.repository.po.ShopPO;

public interface ShopService {
    ShopPO get(Long shopId);
}
