package com.example.TestDemo.Pipeline2;


public interface Step<I, O> {
    public static class StepException extends RuntimeException {
        public StepException(Throwable t) {
            super(t);
        }
    }
    public O process(I input) throws StepException;
}