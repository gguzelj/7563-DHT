package org.fiuba.d2.controller;

import org.fiuba.d2.service.RingService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
public class ShutdownController {

    private ApplicationContext context;
    private RingService ringService;
    @Value("${server.port}")
    private String port;

    public ShutdownController(ApplicationContext context, RingService ringService) {
        this.context = context;
        this.ringService = ringService;
    }

    @PostMapping("/shutdown")
    public void shutdownContext() {
        ringService.removeLocalNode();

        File file = new File("/tmp/" + port + ".mv.db");
        file.delete();

        SpringApplication.exit(context, () -> 0);
        System.exit(0);
    }
}
