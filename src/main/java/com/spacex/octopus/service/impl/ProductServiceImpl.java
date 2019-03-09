package com.spacex.octopus.service.impl;

import com.spacex.octopus.repository.mapper.ProductMapper;
import com.spacex.octopus.repository.po.ProductPO;
import com.spacex.octopus.service.ProductService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Resource
    private ProductMapper productMapper;

    @Override
    public ProductPO get(Long productId) {
        Example example = new Example(ProductPO.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id", productId);
        List<ProductPO> productPOs = productMapper.selectByExample(example);

        if (productPOs == null || productPOs.isEmpty()) {
            return null;
        }

        return productPOs.get(0);
    }

    @Override
    public List<ProductPO> getByShopId(Long shopId) {
        Example example = new Example(ProductPO.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("shopId", shopId);
        List<ProductPO> productPOs = productMapper.selectByExample(example);
        return productPOs;
    }
}
