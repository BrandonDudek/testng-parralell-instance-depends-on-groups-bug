package xyz.swatt.sample.testng.bug;

import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class DependsOnMethodsSample {

    static Duration startTime;

    @DataProvider
    public static Iterator<Object[]> factoryData() {

        //------------------------ Pre-Checks ----------------------------------

        //------------------------ CONSTANTS -----------------------------------

        //------------------------ Variables -----------------------------------
        startTime = Duration.ofMillis(System.currentTimeMillis());

        List<Object[]> data = new LinkedList();

        //------------------------ Code ----------------------------------------
        for(int i = 0; i < 1000; i++) {
            data.add(new Object[] {i});
        }

        return data.iterator();
    }

    final int INSTANCE_NUM;

    AtomicInteger dependentMethodsThatRanCount = new AtomicInteger();

    @Factory(dataProvider = "factoryData")
    public DependsOnMethodsSample(int _i) {
        INSTANCE_NUM = _i;
        System.out.println("DependsOnMethodsSample-"+_i);
    }

    @DataProvider
    public Object[][] instanceData(ITestContext _iTestContext) {
    	return new Object[][] {
    			{INSTANCE_NUM},
    	};
    }

    @Test(dataProvider = "instanceData")
    public void a(int _instanceNum) {
        dependentMethodsThatRanCount.incrementAndGet();
    }

    @Test(dataProvider = "instanceData")
    public void b(int _instanceNum) {
        dependentMethodsThatRanCount.incrementAndGet();
    }

    @Test(dataProvider = "instanceData")
    public void c(int _instanceNum) {
        dependentMethodsThatRanCount.incrementAndGet();
    }

    @Test(dataProvider = "instanceData")
    public void d(int _instanceNum) {
        dependentMethodsThatRanCount.incrementAndGet();
    }

    @Test(dataProvider = "instanceData", dependsOnMethods = {"a", "b"})
    public void e(int _instanceNum) {
        Assert.assertTrue(dependentMethodsThatRanCount.get() == 4, "Tests Methods ran out of order!");
    }

    @AfterSuite
    public void after() {
        System.out.println("Duration: " + Duration.ofMillis(System.currentTimeMillis()).minus(startTime));
    }
}
