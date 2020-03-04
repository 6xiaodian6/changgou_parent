package com.changgou.search.controller;

import com.changgou.entity.Page;
import com.changgou.search.pojo.SkuInfo;
import com.changgou.search.service.ESManagerService;
import com.changgou.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

/**
 * @description:
 * @author: yuandian
 * @createTime: 2019-11-17 21:06:44
 **/
@Controller
@RequestMapping("/sku_search")
public class SearchController {
    @Autowired
    private ESManagerService esManagerService;
    @Autowired
    private SearchService searchService;

    /**
     * 对搜索的入参带有特殊符号进行处理
     *
     * @param searchMap 参数集合
     */
    public void handlerSearchMap(Map<String, String> searchMap) {
        if (null != searchMap) {
            Set<Map.Entry<String, String>> entries = searchMap.entrySet();
            entries.forEach(entry -> {
                if (entry.getKey().startsWith("spec_")) {
                    searchMap.put(entry.getKey(), entry.getValue().replace("+", "%2B"));
                }
            });
        }
    }

    @GetMapping
    @ResponseBody
    public Map search(@RequestBody Map<String, String> paramMap) throws Exception {
        this.handlerSearchMap(paramMap);
        Map resultMap = searchService.search(paramMap);
        return resultMap;
    }

    /**
     * 搜索页面跳转以及数据获取操作
     *
     * @param searchMap 查询条件集合
     * @param model     返回数据模型
     * @return 返回页面
     */
    @GetMapping("/list")
    public String search(@RequestParam Map<String, String> searchMap, Model model) throws Exception {
        //特殊符号处理
        this.handlerSearchMap(searchMap);
        //执行查询返回值
        Map resultMap = searchService.search(searchMap);
        //添加数据信息，用于携带回前台页面进行解析
        model.addAttribute("searchMap", searchMap);
        model.addAttribute("result", resultMap);
        StringBuilder url = new StringBuilder("/sku_search/list");
        //添加url拼接条件
        if (searchMap != null && searchMap.size() > 0) {
            //是有查询条件的
            url.append("?");
            for (String paramkey : searchMap.keySet()) {
                if (!"sortRule".equals(paramkey) && !"sortField".equals(paramkey) && !"pageNum".equals(paramkey)) {
                    url.append(paramkey).append("=").append(searchMap.get(paramkey)).append("&");
                }
            }
            //http://localhost:9009/sku_search/list?keywords=手机&spec_网络制式=4g
            String urlString = url.toString();
            //去除路径上的最后一个&符号
            urlString = urlString.substring(0, urlString.length() - 1);
            model.addAttribute("url", urlString);
        } else {
            model.addAttribute("url", url);
        }
        Page<SkuInfo> page = new Page<>(Long.parseLong(resultMap.get("totalPages").toString()),Integer.parseInt(resultMap.get("pageNum").toString()),Integer.parseInt(resultMap.get("pageSize").toString()));
        model.addAttribute("page",page);
        return "search";
    }
}
