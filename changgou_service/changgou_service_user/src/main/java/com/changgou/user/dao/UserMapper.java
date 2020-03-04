package com.changgou.user.dao;

import com.changgou.user.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

public interface UserMapper extends Mapper<User> {
    /**
     * 修改用户积分
     * @param username
     * @param point
     * @return
     */
    @Update("UPDATE tb_user SET points=#{point} WHERE username=#{username}")
    int updateUserPoint(@Param("username") String username,@Param("point") int point);
}
