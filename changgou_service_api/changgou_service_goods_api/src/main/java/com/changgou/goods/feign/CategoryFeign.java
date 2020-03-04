package com.changgou.goods.feign;

import com.changgou.entity.Result;
import com.changgou.goods.pojo.Category;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @description:
 * @author: yuandian
 * @createTime: 2019-11-20 10:58:09
 **/
@FeignClient(name = "goods")
public interface CategoryFeign {
    /**
     * 通过id查询分类信息
     *
     * @param id
     * @return
     */
    @GetMapping("/category/{id}")
    Result<Category> findById(@PathVariable("id") Integer id);
}

