package us.altio.gmp4j.tests.bench.takashi;

import junit.framework.Assert;

import org.junit.Test;

public class TakashiTest {

	@Test
	public void testTakashiBenchmark() {
		for (int i = 5000; i < 4000000; i *= 2) {
			GMP4JTakashi gt = new GMP4JTakashi();
			JMBTakashi jt = new JMBTakashi();
			Long t1 = System.nanoTime();
			String bi1 = gt.fib(i).toString();
			Long t2 = System.nanoTime();
			System.out.print("G["+(t2 - t1) / 1000000 + "] ");
			String bi2 = jt.fib(i).toString();
			Long t3 = System.nanoTime();
			System.out.println("J["+(t3 - t2) / 1000000+"]");
			Assert.assertEquals(bi2, bi1);
		}
	}
}
