package com.iss.auth;

//import com.iss.auth.mapper.UserMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@SpringBootApplication
@EnableDiscoveryClient
public class AuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);

//        // 读取配置文件
//        InputStream inputStream = null;
//        try {
//            inputStream = Resources.getResourceAsStream("mybatis-config.xml");
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
//        // 获得会话
//        SqlSession sqlSession = sqlSessionFactory.openSession();
//
//        // 获取相关的执行器
//        UserMapper userMapper = sqlSession.getMapper(UserMapper.class );
//        // 执行列表查询
//        List<User> userList = userMapper.list();
//        System.out.println(userList.size());
//
//        // 执行更新提交
//        sqlSession.commit();
//        // 关闭会话
//        sqlSession.close();
    }

}