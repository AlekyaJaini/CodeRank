package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "org.example.coderank")
public class Main {
    public static void main(String[] args) {
        System.setProperty("user.timezone", "Asia/Kolkata");
        java.util.TimeZone.setDefault(java.util.TimeZone.getTimeZone("Asia/Kolkata"));
        System.out.println("========================changes reflectinggg========");

        System.out.println("JVM Default TimeZone (old API): " + java.util.TimeZone.getDefault().getID());
        System.out.println("JVM Default ZoneId (new API):  " + java.time.ZoneId.systemDefault());

        SpringApplication.run(Main.class, args);
    }
}