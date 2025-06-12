package com.example.TestDemo.Pipeline2;


public class Pipeline<I, O> {
    private final Step<I, O> current;
    Pipeline(Step<I, O> current) {
        this.current = current;
    }

    <NewO> Pipeline<I, NewO> pipe(Step<O, NewO> next) {
        return new Pipeline<>(input -> next.process(current.process(input)));
    }

    public O execute(I input) throws Step.StepException {
        return current.process(input);
    }
}