package com.sparta.itsminesingle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
//(exclude =  SecurityAutoConfiguration.class )//시큐리티 제외
public class ItsmineSingleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ItsmineSingleApplication.class, args);
    }

}
