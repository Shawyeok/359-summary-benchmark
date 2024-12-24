package com.zhaopin.benchmark.sample;

public abstract class SummaryWrapper {

    protected double defaultError(double quantile) {
        if (quantile == 0.0 || quantile == 1.0) {
            return 0.0;
        }
        return (1 - quantile) / 10;
    }

    protected abstract void observe(double amt);
}
