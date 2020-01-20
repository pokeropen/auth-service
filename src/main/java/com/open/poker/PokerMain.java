package com.open.poker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(scanBasePackages = {"com.open.poker"})
@EnableJpaAuditing
public class PokerMain {
    public static void main(String[] args) {
        SpringApplication.run(PokerMain.class, args);
    }
}
