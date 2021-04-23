package com.xgx.redis;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(1)
@OutputTimeUnit(TimeUnit.SECONDS)
@BenchmarkMode(Mode.Throughput)
public class random {
    private ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
    private Random random = new Random();

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder().include(random.class.getName()).build();

        new Runner(options).run();
    }

    @Benchmark
    @Threads(8)
    public void runThreadLocalRandom() {
        for (int i = 0; i < 1000; i++) {
            threadLocalRandom.nextInt();
        }
    }

    @Benchmark
    @Threads(8)
    public void runRandom() {
        for (int i = 0; i < 1000; i++) {
            random.nextInt();
        }
    }

}
