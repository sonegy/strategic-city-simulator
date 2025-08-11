package com.example.engine.event;

public interface EventRandom {
    double nextDouble();

    static EventRandom fromSeed(long seed) {
        java.util.Random rnd = new java.util.Random(seed);
        return rnd::nextDouble;
    }
}

