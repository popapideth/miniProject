package com.example.TestDemo.Pipeline2;


public class ExamplePipeline {
    public static class AdditionInput {
        public final int int1;
        public final int int2;

        public AdditionInput(int int1, int int2) {
            this.int1 = int1;
            this.int2 = int2;
        }
    }
    public static class AddIntegersStep implements Step<AdditionInput, Integer> {
        public Integer process(AdditionInput input) {
            return input.int1 + input.int2;
        }
    }

    public static class IntToStringStep implements Step<Integer, String> {
        public String process(Integer input) {
            return input.toString();
        }
    }

    public static void main(String[] args) {
        Pipeline<AdditionInput, String> pipeline = new Pipeline<>(new AddIntegersStep())
                .pipe(new IntToStringStep());
        System.out.println("input : " + pipeline.execute(new AdditionInput(5, 3))); // outputs 4
        System.out.println("input : " + pipeline.execute(new AdditionInput(10, 3))); // outputs 4
    }
}