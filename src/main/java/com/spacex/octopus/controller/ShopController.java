package com.spacex.octopus.controller;

import com.spacex.octopus.dto.shop.ShopCreateDTO;
import com.spacex.octopus.dto.shop.ShopDTO;
import com.spacex.octopus.service.ShopService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class ShopController {

    @Resource
    private ShopService shopService;

    @RequestMapping(value = "shop", method = RequestMethod.GET)
    public ShopDTO get(Long shopId) {
        ShopDTO shopDTO = shopService.get(shopId);
        return shopDTO;
    }

    @RequestMapping(value = "shop", method = RequestMethod.POST)
    public ShopDTO save(@RequestBody ShopCreateDTO shopCreateDTO) {
        for (int i = 0; i < 100; i++) {
            shopCreateDTO.setCityId(Long.valueOf(i));
            ShopDTO shopDTO = shopService.create(shopCreateDTO);
        }

        return null;
    }
}
