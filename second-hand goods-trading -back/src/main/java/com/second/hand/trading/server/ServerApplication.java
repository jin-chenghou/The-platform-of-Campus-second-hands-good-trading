package com.second.hand.trading.server;

import com.second.hand.trading.server.utils.OrderTaskHandler;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication()
@MapperScan("com.second.hand.trading.server.dao")
public class ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
        OrderTaskHandler.run();
    }

}
