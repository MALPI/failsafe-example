# Failsafe Example
This project contains an example project for using [Failsafe](https://github.com/jhalterman/failsafe) and [Failsafe-Actuator](https://github.com/zalando-incubator/failsafe-actuator) with Spring Boot.

The *payment-service* folder contains an example project where client calls are wrapped with Failsafe.

The *solvency-service* folder contains a webserver which is used to mock the service which *payment-service* is calling.