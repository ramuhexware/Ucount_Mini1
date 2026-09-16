package com.freddie.ucount.notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms
public class NotificationAuditApplication {
    public static void main(String[] args) {
        SpringApplication.run(NotificationAuditApplication.class, args);
    }
}
