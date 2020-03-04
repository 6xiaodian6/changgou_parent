package com.changgou.goods.feign;

import com.changgou.entity.Result;
import com.changgou.goods.pojo.Spu;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @description:
 * @author: yuandian
 * @createTime: 2019-11-20 11:08:47
 **/
@FeignClient(name = "goods")
public interface SpuFeign {
    /**
     * 根据spuid查询spu信息
     *
     * @param id
     * @return
     */
    @GetMapping("/spu/findSpuById/{id}")
    Result<Spu> findSpuById(@PathVariable("id") String id);
}
