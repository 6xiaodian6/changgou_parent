package com.changgou.search.listener;

import com.changgou.config.RabbitMQConfig;
import com.changgou.search.service.ESManagerService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: yuandian
 * @createTime: 2019-11-16 16:29:25
 **/
@Component
public class GoodsDownListener {
    @Autowired
    private ESManagerService esManagerService;

    /**
     * 根据数据的改动去修改es中的数据
     *
     * @param spuId 商品id
     */
    @RabbitListener(queues = RabbitMQConfig.SEARCH_DEL_QUEUE)
    public void searchAdd(String spuId) {
        System.out.println("接收到下架消息");
        esManagerService.delSkuListBySpuId(spuId);
    }
}
