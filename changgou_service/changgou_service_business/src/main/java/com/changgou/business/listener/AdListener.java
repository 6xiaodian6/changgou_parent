package com.changgou.business.listener;

import okhttp3.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @description:
 * @author: yuandian
 * @createTime: 2019-11-15 19:00:15
 **/
@Component
public class AdListener {
    /**
     * 这个监听的注解只有作用在方法上才能展示效果
     * @param position 发送的消息
     */
    @RabbitListener(queues = "ad_update_queue")
    public void messageHandle(String position){
        //用okHttp进行请求的发送
        OkHttpClient okHttpClient = new OkHttpClient();
        String url = "http://192.168.200.128/ad_update?position=" + position;
        Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
        //接收请求发送的状态，也就是发送的请求是否成功
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //打印发送失败信息
                e.printStackTrace();
                System.out.println("请求发送失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("请求发送成功" + response.message());
            }
        });
    }
}
