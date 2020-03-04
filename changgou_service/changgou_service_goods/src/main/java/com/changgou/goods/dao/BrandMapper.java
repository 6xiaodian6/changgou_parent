package com.changgou.goods.dao;

import com.changgou.goods.pojo.Brand;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface BrandMapper extends Mapper<Brand> {

    /**
     * 根据商品分类名称查询品牌列表
     */
    @Select("SELECT * FROM tb_brand WHERE id IN ( SELECT brand_id FROM tb_category_brand WHERE category_id IN (SELECT tb_category.id FROM tb_category WHERE tb_category.name = #{categoryName}))")
    List<Map> findListByCagetory(@Param("categoryName") String categoryName);

    /**
     * 根据分类查询规格列表
     */
    @Select("SELECT * FROM tb_spec WHERE template_id IN (SELECT tb_category.template_id FROM tb_category WHERE tb_category.name=#{categoryName})")
    List<Map> findSpecByCategory(@Param("categoryName")String categoryName);
    /**
     * 根据分类查询参数列表
     */

    @Select("SELECT * FROM tb_para WHERE template_id IN (SELECT tb_category.template_id FROM tb_category WHERE tb_category.name =#{categoryName})")
    List<Map> findParaByCategory(@Param("categoryName") String categoryName);
}
