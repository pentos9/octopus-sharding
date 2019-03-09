package com.spacex.octopus.controller;

import com.spacex.octopus.dto.ShopDTO;
import com.spacex.octopus.repository.po.ShopPO;
import com.spacex.octopus.service.ShopService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class ShopController {

    @Resource
    private ShopService shopService;

    @RequestMapping(value = "shop", method = RequestMethod.GET)
    public ShopPO get(Long shopId) {
        ShopPO shopPO = shopService.get(shopId);
        return shopPO;
    }
}
