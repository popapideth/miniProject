package com.example.TestDemo.Config;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class AsyncTask {

    @Async
    public void testAsync()  {
        System.out.println("Async tash executed by thread - " + Thread.currentThread().getName());

    }
}
