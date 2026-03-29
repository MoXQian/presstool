/*
 * Copyright 2026 Vipshop Inc. All Rights Reserved.
 */

package com.red.search.thriftpress;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.LockSupport;
public class SimpleRateLimiter {
    private final double targetQps;          // 目标 QPS（预热结束后稳定在这个值）
    private final long warmUpNanos;          // 预热时长（纳秒）
    private final long startTimeNanos;       // 限流器启动时间
//    private final long intervalNanos;        // 每个请求之间的最小时间间隔
    private final AtomicLong nextPermitTime; // 下一次允许通过的时间（纳秒）
//    public SimpleRateLimiter(double qps) {
//        if (qps <= 0) {
//            throw new IllegalArgumentException("qps must be > 0");
//        }
//        this.intervalNanos = (long) (1_000_000_000L / qps);
//        this.nextPermitTime = new AtomicLong(System.nanoTime());
//    }
    public SimpleRateLimiter(double qps, long warmUpSeconds) {
        if (qps <= 0) {
            throw new IllegalArgumentException("qps must be > 0");
        }
        this.targetQps = qps;
        this.warmUpNanos = warmUpSeconds > 0 ? warmUpSeconds * 1_000_000_000L : 0L;
        this.startTimeNanos = System.nanoTime();
        this.nextPermitTime = new AtomicLong(this.startTimeNanos);
    }
//    public void acquire() {
//        while (true) {
//            long now = System.nanoTime();
//            long expected = nextPermitTime.get();
//            if (now < expected) {
//                // 还没到允许时间，睡到 expected 为止
//                LockSupport.parkNanos(expected - now);
//                continue;
//            }
//            long next = expected + intervalNanos;
//            if (nextPermitTime.compareAndSet(expected, next)) {
//                // 成功占用这个时间片，获得一个“令牌”
//                return;
//            }
//            // CAS 失败说明其他线程抢走了这个时间片，重试
//        }
//    }
//}
    /**
     * 计算当前生效 QPS（在 warmUp 期间从低到高线性增长，之后固定为 targetQps）
     */
    private double currentQps(long now) {
        if (warmUpNanos <= 0) {
            return targetQps;
        }
        long elapsed = now - startTimeNanos;
        if (elapsed >= warmUpNanos) {
            return targetQps;
        }
        // 线性预热：随着时间推移从 0 -> targetQps
        double ratio = (double) elapsed / (double) warmUpNanos;
        // 避免一开始是 0，这里给个下限，比如 0.1 倍
        double minFactor = 0.1;
        double factor = minFactor + (1.0 - minFactor) * ratio; // [0.1, 1.0]
        return targetQps * factor;
    }
    public void acquire() {
        while (true) {
            long now = System.nanoTime();
            double qps = currentQps(now);
            // 当前时刻对应的最小间隔
            long intervalNanos = (long) (1_000_000_000L / qps);
            long expected = nextPermitTime.get();
            if (now < expected) {
                // 还没到允许时间，睡到 expected
                LockSupport.parkNanos(expected - now);
                continue;
            }
            long next = expected + intervalNanos;
            if (nextPermitTime.compareAndSet(expected, next)) {
                // 占到时间片，返回
                return;
            }
            // CAS 失败，重试
        }
    }
}
