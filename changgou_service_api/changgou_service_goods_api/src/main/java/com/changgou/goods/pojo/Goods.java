package com.changgou.goods.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * @description: 商品组合实体类，因为采用的是spring注解的方式实现的创建对象
 * 所以连构造方法都不需要写了
 * @author: yuandian
 * @createTime: 2019-11-12 08:32:51
 **/
public class Goods implements Serializable {
    private Spu spu;
    private List<Sku> skuList;

    public Spu getSpu() {
        return spu;
    }

    public void setSpu(Spu spu) {
        this.spu = spu;
    }

    public List<Sku> getSkuList() {
        return skuList;
    }

    public void setSkuList(List<Sku> skuList) {
        this.skuList = skuList;
    }
}
