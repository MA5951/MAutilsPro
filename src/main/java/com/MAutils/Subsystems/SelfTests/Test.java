
package com.MAutils.Subsystems.SelfTests;

import java.util.function.BooleanSupplier;

public class Test {

    public final BooleanSupplier testCondition;
    public final Runnable testAction;
    public final String testName;
    public final Double testTimeCap;

    public Test(String testName, BooleanSupplier testCondition, Runnable testAction,  Double testTimeCap) {
        this.testCondition = testCondition;
        this.testAction = testAction;
        this.testName = testName;
        this.testTimeCap = testTimeCap;
    }


}
