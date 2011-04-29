package us.altio.gmp4j.tests;

import org.junit.Assert;
import org.junit.Test;

import us.altio.gmp4j.BigInteger;

public class testDivQR {

	@Test
	public void testDivQR() {
		for (int i = 1; i <=2000000; i++) {
			BigInteger bi2 = BigInteger.random(256000);
			BigInteger bi1 = BigInteger.random(3200);
			BigInteger bi0[] = bi2.divideAndRemainder(bi1);
			Assert.assertEquals(bi2, bi0[0].multiply(bi1).add(bi0[1]));
			if (i%2000==0)
			{
				System.gc();
				//System.out.println(bi2);
			}
		}
	}
}
