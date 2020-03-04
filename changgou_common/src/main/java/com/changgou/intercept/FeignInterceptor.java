package com.changgou.intercept;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * @description: 通过该类实现令牌信息的传递，从而实现跨服务之间的认证信息的传递和校验
 * @author: yuandian
 * @createTime: 2019-11-25 20:13:59
 **/
@Component
public class FeignInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes!=null){
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            if (request!=null){
                Enumeration<String> headerNames = request.getHeaderNames();
                if(headerNames!=null){
                    while (headerNames.hasMoreElements()){
                        String headerName = headerNames.nextElement();
                        if ("authorization".equals(headerName)){
                            String headerValue = request.getHeader(headerName);
                            //令牌信息传递
                            requestTemplate.header(headerName,headerValue);
                        }
                    }
                }
            }
        }
    }
}
