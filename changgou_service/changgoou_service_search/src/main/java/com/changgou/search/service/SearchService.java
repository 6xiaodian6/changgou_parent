package com.changgou.search.service;

import java.util.Map;

/**
 * @description:
 * @author: yuandian
 * @createTime: 2019-11-17 20:39:08
 **/
public interface SearchService {
    /**
     * 根据参数进行全文检索
     *
     * @param paramMap 参数集合
     * @return 返回检索结果
     * @throws Exception
     */
    Map search(Map<String, String> paramMap) throws Exception;
}
