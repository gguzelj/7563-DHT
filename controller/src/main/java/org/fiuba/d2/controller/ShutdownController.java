package org.fiuba.d2.controller;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShutdownController {

    private ApplicationContext context;

    public ShutdownController(ApplicationContext context) {
        this.context = context;
    }

    @PostMapping("/shutdown")
    public void shutdownContext() {
        //TODO add REMOVE event
        ((ConfigurableApplicationContext) context).close();
    }
}
