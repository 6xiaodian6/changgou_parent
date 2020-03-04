package com.changgou.goods.feign;

import com.changgou.entity.Result;
import com.changgou.goods.pojo.Sku;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @description:
 * @author: yuandian
 * @createTime: 2019-11-15 20:06:27
 **/
@FeignClient(name = "goods")
public interface SkuFeign {
    /**
     * 根据id查询sku结果集
     *
     * @param spuId spu的id
     * @return
     */
    @GetMapping("/sku/spu/{spuId}")
    List<Sku> findListSpuById(@PathVariable("spuId") String spuId);

    /**
     * 根据id查询sku信息
     *
     * @param id
     * @return
     */
    @GetMapping("/sku/{id}")
    Result<Sku> findById(@PathVariable("id") String id);

    /**
     * 扣减库存并增加销量
     * @param username
     * @return
     */
    @PostMapping("/sku/decr/count")
    Result decrCount(@RequestParam("username") String username);
}
