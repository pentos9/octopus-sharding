package com.spacex.octopus.service.impl;

import com.dangdang.ddframe.rdb.sharding.id.generator.IdGenerator;
import com.google.common.collect.Lists;
import com.spacex.octopus.dto.shop.ShopCreateDTO;
import com.spacex.octopus.dto.shop.ShopDTO;
import com.spacex.octopus.dto.shop.ShopUpdateDTO;
import com.spacex.octopus.repository.mapper.ShopMapper;
import com.spacex.octopus.repository.po.ShopPO;
import com.spacex.octopus.service.ShopService;
import com.spacex.octopus.util.BeanCopyUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class ShopServiceImpl implements ShopService {
    @Resource
    private ShopMapper shopMapper;

    @Resource
    private IdGenerator idGenerator;

    @Override
    public List<ShopDTO> getByIds(List<Long> shopIds) {
        if (CollectionUtils.isEmpty(shopIds)) {
            return Lists.newArrayList();
        }

        Example example = new Example(ShopPO.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("shopId", shopIds);
        List<ShopPO> shopPOs = shopMapper.selectByExample(example);

        if (shopPOs == null || shopPOs.isEmpty()) {
            return null;
        }

        List<ShopDTO> shopDTOs = BeanCopyUtil.mapList(shopPOs, ShopDTO.class);
        return shopDTOs;
    }

    @Override
    public ShopDTO get(Long shopId) {
        Example example = new Example(ShopPO.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("shopId", shopId);
        criteria.andEqualTo("deleted", 0);
        List<ShopPO> shopPOs = shopMapper.selectByExample(example);

        if (shopPOs == null || shopPOs.isEmpty()) {
            return null;
        }

        ShopPO shopPO = shopPOs.get(0);
        ShopDTO shopDTO = BeanCopyUtil.map(shopPO, ShopDTO.class);
        return shopDTO;
    }

    @Override
    public ShopDTO create(ShopCreateDTO shopCreateDTO) {
        ShopPO shopPO = BeanCopyUtil.map(shopCreateDTO, ShopPO.class);
        if (shopPO == null) {
            return null;
        }

        Long shopId = idGenerator.generateId().longValue();
        shopPO.setShopId(shopId);
        shopMapper.insertSelective(shopPO);
        return get(shopId);
    }

    @Override
    public ShopDTO update(ShopUpdateDTO shopUpdateDTO) {
        ShopPO shopPO = BeanCopyUtil.map(shopUpdateDTO, ShopPO.class);
        if (shopPO == null) {
            return null;
        }

        Long shopId = shopUpdateDTO.getShopId();
        Example example = new Example(ShopPO.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("shopId", shopId);

        shopPO.setUpdatedTime(new Date());
        shopMapper.updateByExampleSelective(shopPO, example);

        return get(shopId);
    }

    @Override
    public boolean delete(Long shopId) {
        Example example = new Example(ShopPO.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("shopId", shopId);
        ShopPO shopPO = new ShopPO();
        shopPO.setDeleted(1);
        shopPO.setUpdatedTime(new Date());
        shopMapper.updateByExampleSelective(shopPO, example);
        return true;
    }
}
