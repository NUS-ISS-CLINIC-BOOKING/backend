package com.team.clinic;

// [加入编程导航](https://www.code-nav.cn/) 深耕编程提升【两年半】、国内净值【最高】的编程社群、用心服务【20000+】求学者、帮你自学编程【不走弯路】

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动类
 *
 * @author <a href="https://github.com/liclinic">程序员鱼皮</a>
 * @from <a href="https://clinic.icu">编程导航知识星球</a>
 */
@SpringBootApplication
@MapperScan("com.team.clinic.mapper")
public class UserCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserCenterApplication.class, args);
    }

}

// https://github.com/liclinic