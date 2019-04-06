package com.spacex.octopus.service;

import com.spacex.octopus.dto.shop.ShopCreateDTO;
import com.spacex.octopus.dto.shop.ShopDTO;
import com.spacex.octopus.dto.shop.ShopUpdateDTO;

public interface ShopService {
    ShopDTO get(Long shopId);

    ShopDTO create(ShopCreateDTO shopCreateDTO);

    ShopDTO update(ShopUpdateDTO shopUpdateDTO);

    boolean delete(Long shopId);

}
