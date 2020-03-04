package com.changgou.search.dao;

import com.changgou.search.pojo.SkuInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @description:
 * @author: yuandian
 * @createTime: 2019-11-16 08:23:40
 **/
public interface ESManagerMapper extends ElasticsearchRepository<SkuInfo,Long> {
}
