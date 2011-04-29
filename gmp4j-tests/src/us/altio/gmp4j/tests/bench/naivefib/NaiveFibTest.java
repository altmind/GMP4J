package us.altio.gmp4j.tests.bench.naivefib;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;

import us.altio.gmp4j.BigInteger;

public class NaiveFibTest {


	@Test

	public void testTakashiBenchmark() {
		GMP4JFibonacci gf = new GMP4JFibonacci();
		JMBFibonacci jf = new JMBFibonacci();
		for (int i = 4000; i <= 128000; i += 8000) {
			Long t1 = System.nanoTime();
			String bi1 = gf.fib(i).toString();
			Long t2 = System.nanoTime();
			System.out.print(i+","+(t2 - t1) / 1000000 + ",");
			String bi0 = BigInteger.fibonacci(i).toString();
			Long t3 = System.nanoTime();
			System.out.print((t3 - t2) / 1000000+",");
			String bi2 = jf.fib(i).toString();
			Long t4 = System.nanoTime();
			System.out.println((t4 - t3) / 1000000);
			Assert.assertEquals(bi0, bi1);
			Assert.assertEquals(bi2, bi1);
			System.gc();
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
			}
		}
	}

}
