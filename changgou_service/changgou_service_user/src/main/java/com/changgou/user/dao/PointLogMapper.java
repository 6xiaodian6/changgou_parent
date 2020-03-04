package com.changgou.user.dao;

import com.changgou.user.pojo.PointLog;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

/**
 * @description:
 * @author: yuandian
 * @createTime: 2019-11-27 21:20:13
 **/
public interface PointLogMapper extends Mapper<PointLog> {
    /**
     * 判断当前的任务是否操作过
     *
     * @param orderId
     * @return
     */
    @Select("select * from tb_point_log where order_id=#{orderId}")
    PointLog findPointLogByOrderId(@Param("orderId") String orderId);
}
