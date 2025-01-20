package com.dlz.test.framework.redis.queue;

import com.dlz.test.framework.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

public class Start extends BaseTest {
	@Autowired
	private ITestQueueProviderApi testQueueDeal;

	@Test
	public void queue1Task() {
		for (int i = 0; i < 100; i++) {
			testQueueDeal.sendMessage1("Msg:[" + i+ "]");
			testQueueDeal.sendMessage2(Arrays.asList("Hello ", "World.", String.valueOf(i)));
			testQueueDeal.sendMessage3("Msg3:[" + i + "]");
		}
		try {
			Thread.sleep(100000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    @Test
    public void test() {
        System.out.println(1);
    }
}