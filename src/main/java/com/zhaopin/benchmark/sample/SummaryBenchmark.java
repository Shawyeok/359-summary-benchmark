/*
 * Copyright (c) 2014, Oracle America, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.zhaopin.benchmark.sample;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 3, time = 3, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 3, time = 15, timeUnit = TimeUnit.SECONDS)
@Fork(1)
@Threads(Threads.MAX)
public class SummaryBenchmark {

    @State(Scope.Benchmark)
    public static class SummaryState {

        @Param({"1.x", "0.x"})
        private String version;

        @Param({"BASIC", "ADVANCED"})
        private String detailLevel;

        private SummaryWrapper summary;

        @Setup(Level.Trial)
        public void setup() {
            double[] quantiles;
            if ("BASIC".equals(detailLevel)) {
                quantiles = new double[]{0.5};
            } else if ("ADVANCED".equals(detailLevel)) {
                quantiles = new double[]{0.5, 0.9, 0.99};
            } else {
                throw new IllegalArgumentException("Unknown detail level: " + detailLevel);
            }
            if ("1.x".equals(version)) {
                this.summary = new NewSummary("new_summary_benchmark", "The new summary benchmark", quantiles);
            } else if ("0.x".equals(version)) {
                this.summary = new OldSummary("old_summary_benchmark", "The old summary benchmark", quantiles);
            } else {
                throw new IllegalArgumentException("Unknown version: " + version);
            }
        }
    }

    @Benchmark
    public void summaryObserve(SummaryState state) {
        state.summary.observe(ThreadLocalRandom.current().nextInt(5000));
    }
}
