package com.example.TestDemo.Pipeline;

public interface Pipe<IN, OUT> {
    OUT process(IN input);
}