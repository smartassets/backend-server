package org.industrial.iot.monitoring.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaAuditing
@EntityScan("org.industrial.iot.monitoring.model")
@EnableJpaRepositories("org.industrial.iot.monitoring.repository")
@ComponentScan(basePackages = { "org.industrial.iot.monitoring.controller", "org.industrial.iot.monitoring.repository",
    "org.industrial.iot.monitoring.controller.filter", "org.industrial.iot.monitoring.model", "org.industrial.iot.monitoring.application" })
public class MonitoringApplication {

    public static void main(String[] args) {
        SpringApplication.run(MonitoringApplication.class, args);
    }

}
