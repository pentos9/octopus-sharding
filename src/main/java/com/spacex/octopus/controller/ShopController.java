package com.spacex.octopus.controller;

import com.spacex.octopus.dto.SimpleBooleanResult;
import com.spacex.octopus.dto.exception.InvalidParameterException;
import com.spacex.octopus.dto.shop.ShopCreateDTO;
import com.spacex.octopus.dto.shop.ShopDTO;
import com.spacex.octopus.dto.shop.ShopUpdateDTO;
import com.spacex.octopus.service.ShopService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class ShopController {

    private Logger logger = LoggerFactory.getLogger(ShopController.class);

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

    @RequestMapping(value = "shop/getByIds", method = RequestMethod.GET)
    public List<ShopDTO> getByIds(@RequestParam List<Long> shopIds) {
        List<ShopDTO> shopDTOs = shopService.getByIds(shopIds);
        return shopDTOs;
    }

    @RequestMapping(value = "shop", method = RequestMethod.PUT)
    public ShopDTO update(@RequestBody ShopUpdateDTO shopUpdateDTO) {
        if (shopUpdateDTO == null) {
            throw new InvalidParameterException("shopUpdateDTO can not be null");
        }

        if (shopUpdateDTO.getShopId() == null) {
            throw new InvalidParameterException("shopId can not be null");
        }

        ShopDTO shopDTO = shopService.update(shopUpdateDTO);
        return shopDTO;
    }

    @RequestMapping(value = "shop", method = RequestMethod.DELETE)
    public SimpleBooleanResult delete(Long shopId) {
        Boolean result = shopService.delete(shopId);
        return new SimpleBooleanResult(result);
    }

    @RequestMapping(value = "shop/batch", method = RequestMethod.POST)
    public void batchCreate(Integer current) {
        if (current == null) {
            current = 50;
        }
        doBatchCreate(current);
    }

    public ShopCreateDTO getShopCreateDTO(int index) {
        ShopCreateDTO shopCreateDTO = new ShopCreateDTO();
        shopCreateDTO.setCityId(1L);
        shopCreateDTO.setName("name-batch-" + index);
        shopCreateDTO.setDescription("desc-" + index);
        shopCreateDTO.setMobile("12312312");
        shopCreateDTO.setPhone("18900001234");
        shopCreateDTO.setCityId(Long.valueOf(index));
        return shopCreateDTO;
    }

    public void doBatchCreate(int current) {
        ExecutorService executorService = Executors.newFixedThreadPool(current);
        CountDownLatch triggerLatch = new CountDownLatch(1);
        CountDownLatch workerLatch = new CountDownLatch(current);


        for (int i = 0; i < current; i++) {
            ShopCreateDTO shopCreateDTO = getShopCreateDTO(i);
            executorService.submit(() -> {
                try {
                    triggerLatch.await();
                    shopService.create(shopCreateDTO);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    workerLatch.countDown();
                }

            });
        }

        triggerLatch.countDown();
        try {
            workerLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        executorService.shutdown();

        logger.info("[BatchJob] Finished!");
    }
}
