package com.zhaopin.benchmark.sample;

import io.prometheus.client.Summary;

public class OldSummary extends SummaryWrapper {

    private final Summary summary;

    public OldSummary(String name, String help, double... quantiles) {
        Summary.Builder builder = Summary.build(name, help);
        for (double quantile : quantiles) {
            builder.quantile(quantile, defaultError(quantile));
        }
        summary = builder.register();
    }

    @Override
    public void observe(double amt) {
        summary.observe(amt);
    }
}
