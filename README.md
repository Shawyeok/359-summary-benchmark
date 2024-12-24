This project is a simple benchmark test for the `Summary` metric in the Prometheus Java client library.
It compares the performance of `Summary` across different library versions (`0.16.0` and `1.3.5`).

```shell
mvn clean package
java -jar target/summary-benchmarks.jar
```