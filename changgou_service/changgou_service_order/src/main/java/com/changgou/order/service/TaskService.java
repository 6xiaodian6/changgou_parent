package com.changgou.order.service;

import com.changgou.order.pojo.Task;

/**
 * @description:
 * @author: yuandian
 * @createTime: 2019-11-27 21:29:20
 **/
public interface TaskService {
    /**
     * 删除订单原任务
     * @param task
     */
    void delTask(Task task);
}
