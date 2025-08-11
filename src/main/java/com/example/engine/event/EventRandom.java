package com.example.engine.event;

/**
 * 이벤트 확률 판정에 사용되는 난수 소스 추상화.
 * 테스트 재현성을 위해 시드 기반 구현을 제공합니다.
 */
public interface EventRandom {
    /**
     * [0.0, 1.0) 구간의 균등난수를 반환합니다.
     */
    double nextDouble();

    /**
     * 고정 시드 기반 난수 소스를 생성합니다(재현성 보장).
     */
    static EventRandom fromSeed(long seed) {
        java.util.Random rnd = new java.util.Random(seed);
        return rnd::nextDouble;
    }
}
