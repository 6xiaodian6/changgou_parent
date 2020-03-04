package com.changgou.test;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCrypt;

/**
 * @description:
 * @author: yuandian
 * @createTime: 2019-11-21 08:50:13
 **/
public class BcryptTest {
    /**
     * Bcrypt加密方式加密
     */
    @Test
    public void test(){
        BCrypt bCrypt = new BCrypt();
        String gensalt = BCrypt.gensalt();
        String hashpw = BCrypt.hashpw("123456", gensalt);
        System.out.println(hashpw);
        boolean check = BCrypt.checkpw("123456", "$2a$10$K3uchmwne5JeGgtzk88YpuxN8mheOfpsw3kRQYZ2CA8.uHLpiYeUO");
        System.out.println(check);
    }
}
