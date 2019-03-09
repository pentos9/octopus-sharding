package com.spacex.octopus.service.impl;

import com.spacex.octopus.repository.mapper.ShopMapper;
import com.spacex.octopus.repository.po.ShopPO;
import com.spacex.octopus.service.ShopService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ShopServiceImpl implements ShopService {
    @Resource
    private ShopMapper shopMapper;

    @Override
    public ShopPO get(Long shopId) {
        Example example = new Example(ShopPO.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id", shopId);
        List<ShopPO> shopPOs = shopMapper.selectByExample(example);

        if (shopPOs == null || shopPOs.isEmpty()) {
            return null;
        }

        return shopPOs.get(0);
    }
}
