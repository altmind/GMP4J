package us.altio.gmp4j.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.collections.Bag;
import org.apache.commons.collections.HashBag;
import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import us.altio.gmp4j.BigInteger;

public class UnitTestRandomValuesCompareWithJMB {

	static final long RUNCYCLES = 10000;

	@Before
	public void setUp() throws Exception {
		System.gc();
	}

	@Test
	public void testEquals() {
		Random r = new Random();
		for (int i = 0; i < RUNCYCLES; i++) {

			BigInteger bi = BigInteger.random(32);
			String bis = bi.toString();
			while (r.nextFloat() < 0.9) {
				BigInteger bi2 = new BigInteger(bis);
				Assert.assertTrue(bi2.equals(bi));
				BigInteger bi3 = bi2.add(BigInteger.ONE);
				Assert.assertTrue(!bi3.equals(bi));
			}
		}
	}

	@Test
	public void testToString() {
		Random r = new Random();
		for (int i = 0; i < RUNCYCLES; i++) {
			String s = getRandomNumString(r,64);
			BigInteger bi = new BigInteger(s);
			Assert.assertEquals(s, bi.toString());
		}
	}

	@Test
	/* Fill List of BIs, measure free mem, remove ref to list,
	 * run gc and expect at least 100K of mem to reclaim.
	 */
	/*
	 * Removed this test because its results are not deterministic.
	 * http://martinfowler.com/articles/nonDeterminism.html
	 */
	public void testFinalize() {
		/*long t1 = Runtime.getRuntime().freeMemory();
		List<BigInteger> bis = new ArrayList<BigInteger>();
		for (int i = 0; i < RUNCYCLES; i++) {
			bis.add(BigInteger.random(1280));
		}
		long t2 = Runtime.getRuntime().freeMemory();
		System.gc();
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
		}
		bis=null;
		System.gc();
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
		}
		long t3 = Runtime.getRuntime().freeMemory();
		System.err.println("Reclaimed: "+(t3-t2)/1000+"K of mem");
		Assert.assertTrue((t3-t2)>100000);*/
	}

	@Test
	public void testIntValue() {
		Random r = new Random();
		for (int i = 0; i < RUNCYCLES; i++) {
			int randInt=r.nextInt();
			BigInteger bi = new BigInteger(randInt);
			Assert.assertEquals(randInt,bi.intValue());
		}
	}

	@Test
	public void testLongValue() {
		Random r = new Random();
		for (int i = 0; i < RUNCYCLES; i++) {
			long randLong=r.nextLong();
			BigInteger bi = new BigInteger(randLong);
			Assert.assertEquals(randLong, bi.longValue());
		}
	}

	@Test
	public void testFloatValue() {
		Random r = new Random();
		for (int i = 0; i < RUNCYCLES; i++) {
			long randLong=r.nextLong();
			BigInteger bi = new BigInteger(randLong);
			Assert.assertEquals(randLong, bi.floatValue(), 0.00001);
		}
	}

	@Test
	public void testDoubleValue() {
		Random r = new Random();
		for (int i = 0; i < RUNCYCLES; i++) {
			long randLong=r.nextLong();
			BigInteger bi = new BigInteger(randLong);
			Assert.assertEquals(randLong, bi.doubleValue(), 0.00001);
		}
	}

	@Test
	public void testBigIntegerLong() {
		Random r = new Random();
		for (int i = 0; i < RUNCYCLES; i++) {
			long randLong=r.nextLong();
			BigInteger bi = new BigInteger(randLong);
			Assert.assertEquals(randLong, bi.longValue());
		}
	}

	@Test
	public void testBigInteger() {
		BigInteger bi = new BigInteger();
		Assert.assertEquals(0l, bi.longValue());
	}

	@Test
	/*
	 * This method copies testToString() test
	 */
	public void testBigIntegerString() {
		Random r = new Random();
		for (int i = 0; i < RUNCYCLES; i++) {
			String s = getRandomNumString(r,64);
			BigInteger bi = new BigInteger(s);
			Assert.assertEquals(s, bi.toString());
		}
	}

	@Test
	public void testBigIntegerStringInt() {
		Random r = new Random();
		for (int i = 0; i < RUNCYCLES; i++) {
			String s = getRandomNumString(r,64);
			java.math.BigInteger jmbi = new java.math.BigInteger(s);
			int base = r.nextInt(26)+10;
			String jmbis=jmbi.toString(base);
			BigInteger bi = new BigInteger(jmbis,base);
			Assert.assertEquals(s, bi.toString());
		}
	}

	@Test
	public void testToStringInt() {
		Random r = new Random();
		for (int i = 0; i < RUNCYCLES; i++) {
			String s = getRandomNumString(r,64);
			java.math.BigInteger jmbi = new java.math.BigInteger(s);
			int base = r.nextInt(26)+10;
			String jmbis=jmbi.toString(base);
			BigInteger bi = new BigInteger(jmbis,base);
			Assert.assertEquals(jmbis, bi.toString(base));
		}
	}

	private String getRandomNumString(Random r, int len) {
		String s = StringUtils.stripStart(TestUtils.genStringFromChars(r
				.nextInt(len) + 2, "0123456789"), "0");
		if (s.equals(""))
			s = "0";
		if (!s.equals("0") && r.nextBoolean() == true) {
			s = "-" + s;
		}
		return s;
	}

	@Test
	public void testCompareTo() {
		Random r = new Random();
		for (int i = 0; i < RUNCYCLES; i++) {
			BigInteger bi1 = BigInteger.random(r.nextInt(256)+16);
			java.math.BigInteger jmbi1 = new java.math.BigInteger(bi1.toString());
			BigInteger bi2 = BigInteger.random(r.nextInt(256)+16);
			java.math.BigInteger jmbi2 = new java.math.BigInteger(bi2.toString());
			Assert.assertEquals(jmbi2.compareTo(jmbi1), bi2.compareTo(bi1));
		}
	}

	@Test
	public void testAbs() {
		Random r = new Random();
		for (int i = 0; i < RUNCYCLES; i++) {
			int bitness = r.nextInt(64)+16; 
			BigInteger biPositive = BigInteger.random(bitness);
			BigInteger bi = biPositive.subtract(new BigInteger(2l).pow(bitness-1));
			java.math.BigInteger jmbi = new java.math.BigInteger(bi.toString());
			if (bi.signum()==-1)
			{
				Assert.assertEquals(BigInteger.ZERO,bi.add(bi.abs()));
			}
			System.err.println(bi.toString());
			Assert.assertEquals(jmbi.abs().toString(), bi.abs().toString());
		}
	}

	@Test
	@Ignore
	public void testAdd() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testAnd() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testAndNot() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testBitCount() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testBitLength() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testClearBit() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testDivide() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testDivideAndRemainder() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testFlipBit() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testGcd() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testGetLowestSetBit() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testIsProbablePrime() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testMax() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testMin() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testMod() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testModInverse() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testModPow() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testMultiply() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testNegate() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testNextProbablePrime() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testNot() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testOr() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testPow() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testRemainder() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testSetBit() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testShiftLeft() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testShiftRight() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testSignum() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testSubtract() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testTestBit() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testToByteArray() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testXor() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testProbablePrime() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testValueOf() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testDivQuotent() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testDivRemainder() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testDivisible() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testDiv() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testSqrt() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testSqrtIntegerPart() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testIsSqrtWithoutRemainder() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testRoot() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testRootIntegerPart() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testLcm() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testFactorial() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testBinomal() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testFibonacci() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testFibonacciPair() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testRandomBigInteger() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testRandomInt() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testNumber() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testByteValue() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testShortValue() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testHashCode() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testClone() {
		fail("Not yet implemented");
	}

}
