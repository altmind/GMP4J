package us.altio.gmp4j.tests.bench.naivefib;

import junit.framework.Assert;

import org.junit.Test;

public class NaiveFibTest {

	@Test
	public void testTakashiBenchmark() {
		GMP4JFibonacci gf = new GMP4JFibonacci();
		JMBFibonacci jf = new JMBFibonacci();
		for (int i = 1000; i <= 500000; i *= 2) {
			Long t1 = System.nanoTime();
			String bi1 = gf.fib(i).toString();
			Long t2 = System.nanoTime();
			System.out.print("G["+(t2 - t1) / 1000000 + "] ");
			String bi2 = jf.fib(i).toString();
			Long t3 = System.nanoTime();
			System.out.println("J["+(t3 - t2) / 1000000+"]");
			Assert.assertEquals(bi2, bi1);
			System.gc();
		}
	}
}
