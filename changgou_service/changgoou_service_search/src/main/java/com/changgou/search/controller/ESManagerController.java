package com.changgou.search.controller;

import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.search.service.ESManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: yuandian
 * @createTime: 2019-11-16 20:51:28
 **/
@RestController
@RequestMapping("/esmanager")
public class ESManagerController {
    @Autowired
    private ESManagerService esManagerService;

    @GetMapping("/create")
    public Result createIndex() {
        esManagerService.createIndexAndMapping();
        return new Result(true, StatusCode.OK, "创建索引和映射成功");
    }

    @GetMapping("/importAll")
    public Result importAll() {
        esManagerService.importAll();
        return new Result(true, StatusCode.OK, "导入数据成功");
    }
}
