package com.fsdm.test.bitcoinbj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class BitcoinBjApplication {
    public static void main(String[] args) {
        SpringApplication.run(BitcoinBjApplication.class, args);
    }
}
