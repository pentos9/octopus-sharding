package com.spacex.octopus.controller;

import com.spacex.octopus.repository.po.ProductPO;
import com.spacex.octopus.service.ProductService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class ProductController {

    @Resource
    private ProductService productService;

    @RequestMapping(value = "product", method = RequestMethod.GET)
    public ProductPO get(Long productId) {
        ProductPO productPO = productService.get(productId);
        return productPO;
    }

    @RequestMapping(value = "product/mget", method = RequestMethod.GET)
    public List<ProductPO> mget(Long shopId) {
        List<ProductPO> productPOs = productService.getByShopId(shopId);
        return productPOs;
    }
}
