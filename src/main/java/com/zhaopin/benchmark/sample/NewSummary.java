package com.zhaopin.benchmark.sample;

import io.prometheus.metrics.core.metrics.Summary;

public class NewSummary extends SummaryWrapper {

    private final Summary summary;

    public NewSummary(String name, String help, double... quantiles) {
        Summary.Builder builder = Summary.builder()
                .name(name)
                .help(help);
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
