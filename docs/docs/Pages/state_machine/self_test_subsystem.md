# Subsystem Self Test Framework

MAutils provides a flexible and efficient framework for building **self-testing subsystems**. This enables rapid and automated subsystem verification in the pit or during diagnostics.

Every `StateSubsystem` must implement the `getSelfTest()` function.

---

## Overview

Each `StateSubsystem` must implement the following method:

```java
Command getSelfTest();
```

This method should return a command that executes a series of validation tests.

---

## Test Definition

Tests are encapsulated by the `Test` class:

```java
Test(String name, BooleanSupplier testCondition, Runnable testAction, double timeCap)
```

Each `Test` includes:

* `testName`: A human-readable name
* `testCondition`: Logic that determines pass/fail
* `testAction`: What to do during the test
* `testTimeCap`: Timeout in seconds for the condition to pass

---

## Constructing a Self Test

To define a self test, instantiate a `SelfSystemTest` and chain calls to `addTest()`:

```java
public Command getSelfTest() {
    return selfSystemTest
        .addTest(new Test("Encoder Sync", () -> Math.abs(getPosition() - getAbsolutePosition()) <= 2, () -> setVoltage(0.5), 0.5))
        .addTest(new Test("Max Position", () -> getPosition() > MAX_POS - 2, () -> setPosition(MAX_POS), 0.8))
        .addTest(new Test("Min Position", () -> getPosition() < MIN_POS + 2, () -> setPosition(MIN_POS), 0.8))
        .createCommand();
}
```

Each test runs **sequentially**, evaluating the condition within the allowed time.

---

## Telemetry

Telemetry is automatically published to NetworkTables:

```
/Subsystem/<SubsystemName>/SelfTest/
```

### Self Test Status

* `"Starting Self Test For <SubsystemName>"`
* `"Passed, Finished Self Test For <SubsystemName>"`
* `"Failed, Finished Self Test For <SubsystemName>"`

### Test Status

For each individual test:

* `"Running test: <TestName>"`
* `"Test timed out: <TestName>"`
* `"Test passed: <TestName>"`
* `"Test finished: <TestName>"`

