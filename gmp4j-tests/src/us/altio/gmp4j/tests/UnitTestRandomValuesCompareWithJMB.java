package us.altio.gmp4j.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import us.altio.gmp4j.BigInteger;

public class UnitTestRandomValuesCompareWithJMB {

	static final long RUNCYCLES = 1000;

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
		for (int i = 0; i < RUNCYCLES; i++) {
			String s = getRandomNumString(64);
			BigInteger bi = new BigInteger(s);
			Assert.assertEquals(s, bi.toString());
		}
	}

	@Test
	/*
	 * Fill List of BIs, measure free mem, remove ref to list, run gc and expect
	 * at least 100K of mem to reclaim.
	 */
	/*
	 * Removed this test because its results are not deterministic.
	 * http://martinfowler.com/articles/nonDeterminism.html
	 */
	public void testFinalize() {
		/*
		 * long t1 = Runtime.getRuntime().freeMemory(); List<BigInteger> bis =
		 * new ArrayList<BigInteger>(); for (int i = 0; i < RUNCYCLES; i++) {
		 * bis.add(BigInteger.random(1280)); } long t2 =
		 * Runtime.getRuntime().freeMemory(); System.gc(); try {
		 * Thread.sleep(200); } catch (InterruptedException e) { } bis=null;
		 * System.gc(); try { Thread.sleep(200); } catch (InterruptedException
		 * e) { } long t3 = Runtime.getRuntime().freeMemory();
		 * System.err.println("Reclaimed: "+(t3-t2)/1000+"K of mem");
		 * Assert.assertTrue((t3-t2)>100000);
		 */
	}

	@Test
	public void testIntValue() {
		Random r = new Random();
		for (int i = 0; i < RUNCYCLES; i++) {
			int randInt = r.nextInt();
			BigInteger bi = new BigInteger(randInt);
			Assert.assertEquals(randInt, bi.intValue());
		}
	}

	@Test
	public void testLongValue() {
		Random r = new Random();
		for (int i = 0; i < RUNCYCLES; i++) {
			long randLong = r.nextLong();
			BigInteger bi = new BigInteger(randLong);
			Assert.assertEquals(randLong, bi.longValue());
		}
	}

	@Test
	public void testFloatValue() {
		Random r = new Random();
		for (int i = 0; i < RUNCYCLES; i++) {
			int randInt = r.nextInt((int)Math.pow(2, 24)) - ((int)Math.pow(2, 24) / 2);
			BigInteger bi = new BigInteger(randInt);
			Assert.assertEquals(randInt, (long) bi.floatValue());
		}
	}

	@Test
	public void testDoubleValue() {
		Random r = new Random();
		for (int i = 0; i < RUNCYCLES; i++) {
			int randLong = r.nextInt((int)Math.pow(2, 24)) - ((int)Math.pow(2, 24) / 2);
			BigInteger bi = new BigInteger(randLong);
			Assert.assertEquals(randLong, (long) bi.floatValue());
		}
	}

	@Test
	public void testBigIntegerLong() {
		Random r = new Random();
		for (int i = 0; i < RUNCYCLES; i++) {
			long randLong = r.nextLong();
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
		for (int i = 0; i < RUNCYCLES; i++) {
			String s = getRandomNumString(64);
			BigInteger bi = new BigInteger(s);
			Assert.assertEquals(s, bi.toString());
		}
	}

	@Test
	public void testBigIntegerStringInt() {
		Random r = new Random();
		for (int i = 0; i < RUNCYCLES; i++) {
			String s = getRandomNumString(64);
			java.math.BigInteger jmbi = new java.math.BigInteger(s);
			int base = r.nextInt(26) + 10;
			String jmbis = jmbi.toString(base);
			BigInteger bi = new BigInteger(jmbis, base);
			Assert.assertEquals(s, bi.toString());
		}
	}

	@Test
	public void testToStringInt() {
		Random r = new Random();
		for (int i = 0; i < RUNCYCLES; i++) {
			String s = getRandomNumString(64);
			java.math.BigInteger jmbi = new java.math.BigInteger(s);
			int base = r.nextInt(26) + 10;
			String jmbis = jmbi.toString(base);
			BigInteger bi = new BigInteger(jmbis, base);
			Assert.assertEquals(jmbis, bi.toString(base));
		}
	}

	private String getRandomNumString(int len) {
		Random r = new Random();
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
			BigInteger bi1 = BigInteger.random(r.nextInt(256) + 16);
			java.math.BigInteger jmbi1 = new java.math.BigInteger(bi1
					.toString());
			BigInteger bi2 = BigInteger.random(r.nextInt(256) + 16);
			java.math.BigInteger jmbi2 = new java.math.BigInteger(bi2
					.toString());
			Assert.assertEquals(jmbi2.compareTo(jmbi1), bi2.compareTo(bi1));
		}
	}

	@Test
	public void testAbs() {
		for (int i = 0; i < RUNCYCLES; i++) {
			BigInteger bi = getPosNegBI(64);
			java.math.BigInteger jmbi = new java.math.BigInteger(bi.toString());
			if (bi.signum() == -1) {
				Assert.assertEquals(BigInteger.ZERO, bi.add(bi.abs()));
			}
			Assert.assertEquals(jmbi.abs().toString(), bi.abs().toString());
		}
	}

	private BigInteger getPosNegBI(int bits) {
		Random r = new Random();
		int bitness = r.nextInt(bits) + 16;
		BigInteger biPositive = BigInteger.random(bitness);
		BigInteger bi = biPositive
				.subtract(new BigInteger(2l).pow(bitness - 1));
		return bi;
	}

	@Test
	public void testAdd() {
		for (int i = 0; i < RUNCYCLES; i++) {
			BigInteger bi1 = getPosNegBI(128);
			BigInteger bi2 = getPosNegBI(128);
			BigInteger bi3 = bi1.add(bi2);
			java.math.BigInteger jmbi1 = new java.math.BigInteger(bi1
					.toString());
			java.math.BigInteger jmbi2 = new java.math.BigInteger(bi2
					.toString());
			java.math.BigInteger jmbi3 = jmbi1.add(jmbi2);
			Assert.assertEquals(jmbi3.toString(), bi3.toString());
		}

	}

	@Test
	public void testAnd() {
		for (int i = 0; i < RUNCYCLES; i++) {
			BigInteger bi1 = getPosNegBI(128);
			BigInteger bi2 = getPosNegBI(128);
			BigInteger bi3 = bi1.and(bi2);
			java.math.BigInteger jmbi1 = new java.math.BigInteger(bi1
					.toString());
			java.math.BigInteger jmbi2 = new java.math.BigInteger(bi2
					.toString());
			java.math.BigInteger jmbi3 = jmbi1.and(jmbi2);
			Assert.assertEquals(jmbi3.toString(), bi3.toString());
		}
	}

	@Test
	public void testAndNot() {
		for (int i = 0; i < RUNCYCLES; i++) {
			BigInteger bi1 = getPosNegBI(128);
			BigInteger bi2 = getPosNegBI(128);
			BigInteger bi3 = bi1.andNot(bi2);
			java.math.BigInteger jmbi1 = new java.math.BigInteger(bi1
					.toString());
			java.math.BigInteger jmbi2 = new java.math.BigInteger(bi2
					.toString());
			java.math.BigInteger jmbi3 = jmbi1.andNot(jmbi2);
			Assert.assertEquals(jmbi3.toString(), bi3.toString());
		}
	}

	@Test
	public void testBitCount() {
		for (int i = 0; i < RUNCYCLES; i++) {
			BigInteger bi1 = getPosNegBI(256).abs();
			java.math.BigInteger jmbi1 = new java.math.BigInteger(bi1
					.toString());
			Assert.assertEquals(jmbi1.bitCount(), bi1.bitCount());
		}
	}

	@Test
	public void testBitLength() {
		for (int i = 0; i < RUNCYCLES; i++) {
			BigInteger bi1 = getPosNegBI(512);
			java.math.BigInteger jmbi1 = new java.math.BigInteger(bi1
					.toString());
			Assert.assertEquals(jmbi1.bitLength(), bi1.bitLength());
		}
	}

	@Test
	public void testClearBit() {
		for (int i = 0; i < RUNCYCLES; i++) {
			BigInteger bi1 = getPosNegBI(256);
			java.math.BigInteger jmbi1 = new java.math.BigInteger(bi1
					.toString());
			int k = 0;
			for (int j = 0; i < 30; i++) {
				Assert.assertEquals(jmbi1.toString(), bi1.toString());
				bi1 = bi1.clearBit(k);
				jmbi1 = jmbi1.clearBit(k);
				k += j;
			}
		}
	}

	@Test
	public void testDivide() {
		for (int i = 0; i < RUNCYCLES; i++) {
			BigInteger bi1 = getPosNegBI(256);
			BigInteger bi2 = getPosNegBI(128);
			BigInteger bi3 = bi1.divide(bi2);
			java.math.BigInteger jmbi1 = new java.math.BigInteger(bi1
					.toString());
			java.math.BigInteger jmbi2 = new java.math.BigInteger(bi2
					.toString());
			java.math.BigInteger jmbi3 = jmbi1.divide(jmbi2);
			Assert.assertEquals(jmbi3.toString(), bi3.toString());
		}
	}

	@Test
	public void testDivideAndRemainder() {
		for (int i = 0; i < RUNCYCLES; i++) {
			BigInteger bi1 = new BigInteger(getRandomNumString(128));
			BigInteger bi2 = new BigInteger(getRandomNumString(64));
			if (bi2.signum() == 0)
				continue;
			BigInteger bidiv[] = bi1.divideAndRemainder(bi2);
			BigInteger bi3 = bidiv[0].multiply(bi2).add(bidiv[1]);
			Assert.assertEquals(bi1, bi3);

		}
	}

	@Test
	public void testFlipBit() {
		for (int i = 0; i < RUNCYCLES; i++) {
			BigInteger bi1 = getPosNegBI(256);
			java.math.BigInteger jmbi1 = new java.math.BigInteger(bi1
					.toString());
			int k = 0;
			for (int j = 0; i < 30; i++) {
				Assert.assertEquals(jmbi1.toString(), bi1.toString());
				bi1 = bi1.flipBit(k);
				jmbi1 = jmbi1.flipBit(k);
				k += j;
			}
		}
	}

	@Test
	public void testGcd() {
		for (int i = 0; i < RUNCYCLES; i++) {
			BigInteger bi1 = getPosNegBI(128);
			BigInteger bi2 = getPosNegBI(128);

			java.math.BigInteger jmbi1 = new java.math.BigInteger(bi1
					.toString());
			java.math.BigInteger jmbi2 = new java.math.BigInteger(bi2
					.toString());

			Assert.assertEquals(jmbi1.gcd(jmbi2).toString(), bi1.gcd(bi2)
					.toString());
		}
	}

	@Test
	public void testGetLowestSetBit() {
		for (int i = 0; i < RUNCYCLES; i++) {
			BigInteger bi1 = getPosNegBI(128);
			java.math.BigInteger jmbi1 = new java.math.BigInteger(bi1
					.toString());
			Assert.assertEquals(jmbi1.getLowestSetBit(), bi1.getLowestSetBit());
		}
	}

	@Test
	public void testIsProbablePrime() {
		BigInteger lastPrime = new BigInteger(271);
		for (int i = 0; i < RUNCYCLES; i++) {
			Assert.assertTrue(lastPrime.isProbablePrime(20));
			lastPrime = lastPrime.nextProbablePrime();
		}
	}

	@Test
	public void testMax() {
		for (int i = 0; i < RUNCYCLES; i++) {
			BigInteger bi1 = getPosNegBI(128);
			BigInteger bi2 = getPosNegBI(128);

			java.math.BigInteger jmbi1 = new java.math.BigInteger(bi1
					.toString());
			java.math.BigInteger jmbi2 = new java.math.BigInteger(bi2
					.toString());

			Assert.assertEquals(jmbi1.max(jmbi2).toString(), bi1.max(bi2)
					.toString());
		}
	}

	@Test
	public void testMin() {
		for (int i = 0; i < RUNCYCLES; i++) {
			BigInteger bi1 = getPosNegBI(128);
			BigInteger bi2 = getPosNegBI(128);

			java.math.BigInteger jmbi1 = new java.math.BigInteger(bi1
					.toString());
			java.math.BigInteger jmbi2 = new java.math.BigInteger(bi2
					.toString());

			Assert.assertEquals(jmbi1.min(jmbi2).toString(), bi1.min(bi2)
					.toString());
		}
	}

	@Test
	public void testMod() {
		for (int i = 0; i < RUNCYCLES; i++) {
			BigInteger bi1 = getPosNegBI(512);
			BigInteger bi2 = getPosNegBI(128).abs();
			BigInteger bi3 = bi1.mod(bi2);
			java.math.BigInteger jmbi1 = new java.math.BigInteger(bi1
					.toString());
			java.math.BigInteger jmbi2 = new java.math.BigInteger(bi2
					.toString());
			java.math.BigInteger jmbi3 = jmbi1.mod(jmbi2);
			Assert.assertEquals(jmbi3.toString(), bi3.toString());
		}
	}

	@Test
	public void testModInverse() {
		for (int i = 0; i < RUNCYCLES; i++) {
			BigInteger bi1 = getPosNegBI(512);
			BigInteger bi2 = getPosNegBI(64).abs();
			BigInteger bi3 = getPosNegBI(96).abs();
			BigInteger bi4 = bi1.modPow(bi2, bi3);
			java.math.BigInteger jmbi1 = new java.math.BigInteger(bi1
					.toString());
			java.math.BigInteger jmbi2 = new java.math.BigInteger(bi2
					.toString());
			java.math.BigInteger jmbi3 = new java.math.BigInteger(bi3
					.toString());
			java.math.BigInteger jmbi4 = jmbi1.modPow(jmbi2, jmbi3);
			Assert.assertEquals(jmbi4.toString(), bi4.toString());
		}
	}

	@Test
	public void testModPow() {
		for (int i = 0; i < RUNCYCLES; i++) {
			BigInteger bi1 = getPosNegBI(512);
			BigInteger bi2 = getPosNegBI(64).abs();
			BigInteger bi3 = getPosNegBI(128).abs();
			BigInteger bi4 = bi1.modPow(bi2, bi3);
			java.math.BigInteger jmbi1 = new java.math.BigInteger(bi1
					.toString());
			java.math.BigInteger jmbi2 = new java.math.BigInteger(bi2
					.toString());
			java.math.BigInteger jmbi3 = new java.math.BigInteger(bi3
					.toString());
			java.math.BigInteger jmbi4 = jmbi1.modPow(jmbi2, jmbi3);
			Assert.assertEquals(jmbi4.toString(), bi4.toString());
		}
	}

	@Test
	public void testMultiply() {
		for (int i = 0; i < RUNCYCLES; i++) {
			BigInteger bi1 = getPosNegBI(128);
			BigInteger bi2 = getPosNegBI(128);
			BigInteger bi3 = bi1.multiply(bi2);
			java.math.BigInteger jmbi1 = new java.math.BigInteger(bi1
					.toString());
			java.math.BigInteger jmbi2 = new java.math.BigInteger(bi2
					.toString());
			java.math.BigInteger jmbi3 = jmbi1.multiply(jmbi2);
			Assert.assertEquals(jmbi3.toString(), bi3.toString());
		}
	}

	@Test
	public void testNegate() {
		for (int i = 0; i < RUNCYCLES; i++) {
			BigInteger bi1 = getPosNegBI(128);
			Assert.assertEquals(BigInteger.ZERO, bi1.add(bi1.negate()));
			if (bi1.signum() < 0)
				Assert.assertEquals(1, bi1.negate().signum());
			else if (bi1.signum() > 0)
				Assert.assertEquals(-1, bi1.negate().signum());
			else if (bi1.signum() == 0)
				Assert.assertEquals(0, bi1.negate().signum());
		}
	}

	@Test
	// the same as for testIsProbablePrime
	public void testNextProbablePrime() {
		BigInteger lastPrime = new BigInteger(271);
		for (int i = 0; i < RUNCYCLES; i++) {
			Assert.assertTrue(lastPrime.isProbablePrime(20));
			lastPrime = lastPrime.nextProbablePrime();
		}
	}

	@Test
	public void testNot() {
		for (int i = 0; i < RUNCYCLES; i++) {
			BigInteger bi1 = getPosNegBI(256);
			BigInteger bi2 = bi1.not();
			java.math.BigInteger jmbi1 = new java.math.BigInteger(bi1
					.toString());
			java.math.BigInteger jmbi2 = jmbi1.not();
			Assert.assertEquals(jmbi2.toString(), bi2.toString());
		}
	}

	@Test
	public void testOr() {
		for (int i = 0; i < RUNCYCLES; i++) {
			BigInteger bi1 = getPosNegBI(128);
			BigInteger bi2 = getPosNegBI(128);
			BigInteger bi3 = bi1.or(bi2);
			java.math.BigInteger jmbi1 = new java.math.BigInteger(bi1
					.toString());
			java.math.BigInteger jmbi2 = new java.math.BigInteger(bi2
					.toString());
			java.math.BigInteger jmbi3 = jmbi1.or(jmbi2);
			Assert.assertEquals(jmbi3.toString(), bi3.toString());
		}
	}

	@Test
	public void testPow() {
		Random r = new Random();
		for (int i = 0; i < RUNCYCLES; i++) {
			BigInteger bi1 = getPosNegBI(128);
			int power = r.nextInt(24) + 1;
			BigInteger bi3 = bi1.pow(power);
			java.math.BigInteger jmbi1 = new java.math.BigInteger(bi1
					.toString());
			java.math.BigInteger jmbi3 = jmbi1.pow(power);
			Assert.assertEquals(jmbi3.toString(), bi3.toString());
		}
	}

	@Test
	public void testRemainder() {
		for (int i = 0; i < RUNCYCLES; i++) {
			BigInteger bi1 = getPosNegBI(256);
			BigInteger bi2 = getPosNegBI(128);
			BigInteger bi3 = bi1.remainder(bi2);
			java.math.BigInteger jmbi1 = new java.math.BigInteger(bi1
					.toString());
			java.math.BigInteger jmbi2 = new java.math.BigInteger(bi2
					.toString());
			java.math.BigInteger jmbi3[] = jmbi1.divideAndRemainder(jmbi2);
			Assert.assertEquals(jmbi3[1].toString(), bi3.toString());
		}
	}

	@Test
	public void testSetBit() {
		for (int i = 0; i < RUNCYCLES; i++) {
			BigInteger bi1 = getPosNegBI(256);
			java.math.BigInteger jmbi1 = new java.math.BigInteger(bi1
					.toString());
			int k = 0;
			for (int j = 0; i < 30; i++) {
				Assert.assertEquals(jmbi1.toString(), bi1.toString());
				bi1 = bi1.setBit(k);
				jmbi1 = jmbi1.setBit(k);
				k += j;
			}
		}
	}

	@Test
	public void testShiftLeft() {
		for (int i = 0; i < RUNCYCLES; i++) {
			BigInteger bi1 = getPosNegBI(256);
			java.math.BigInteger jmbi1 = new java.math.BigInteger(bi1
					.toString());
			for (int j = 0; i < 10; i++) {
				Assert.assertEquals(jmbi1.toString(), bi1.toString());
				bi1 = bi1.shiftLeft(j);
				jmbi1 = jmbi1.shiftLeft(j);
			}
		}
	}

	@Test
	public void testShiftRight() {
		for (int i = 0; i < RUNCYCLES; i++) {
			BigInteger bi1 = getPosNegBI(256);
			java.math.BigInteger jmbi1 = new java.math.BigInteger(bi1
					.toString());
			for (int j = 0; i < 10; i++) {
				Assert.assertEquals(jmbi1.toString(), bi1.toString());
				bi1 = bi1.shiftLeft(j);
				jmbi1 = jmbi1.shiftLeft(j);
			}
		}
	}

	@Test
	public void testSignum() {
		Assert.assertEquals(1, BigInteger.ONE.signum());
		Assert.assertEquals(0, BigInteger.ZERO.signum());
		Assert.assertEquals(-1, BigInteger.MINUSONE.signum());
		for (int i = 0; i < RUNCYCLES; i++) {
			BigInteger bi1 = getPosNegBI(32);

			java.math.BigInteger jmbi1 = new java.math.BigInteger(bi1
					.toString());

			Assert.assertEquals(jmbi1.signum(), bi1.signum());
		}
	}

	@Test
	public void testSubtract() {
		for (int i = 0; i < RUNCYCLES; i++) {
			BigInteger bi1 = getPosNegBI(128);
			BigInteger bi2 = getPosNegBI(128);
			BigInteger bi3 = bi1.subtract(bi2);
			java.math.BigInteger jmbi1 = new java.math.BigInteger(bi1
					.toString());
			java.math.BigInteger jmbi2 = new java.math.BigInteger(bi2
					.toString());
			java.math.BigInteger jmbi3 = jmbi1.subtract(jmbi2);
			Assert.assertEquals(jmbi3.toString(), bi3.toString());
		}
	}

	@Test
	public void testTestBit() {
		for (int i = 0; i < RUNCYCLES; i++) {
			BigInteger bi1 = getPosNegBI(256);
			java.math.BigInteger jmbi1 = new java.math.BigInteger(bi1
					.toString());
			int k = 0;
			for (int j = 0; i < 30; i++) {
				Assert.assertEquals(jmbi1.testBit(k), bi1.testBit(k));
				k += j;
			}
		}
	}

	@Test
	@Ignore
	public void testToByteArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testXor() {
		for (int i = 0; i < RUNCYCLES; i++) {
			BigInteger bi1 = getPosNegBI(128);
			BigInteger bi2 = getPosNegBI(128);
			BigInteger bi3 = bi1.xor(bi2);
			java.math.BigInteger jmbi1 = new java.math.BigInteger(bi1
					.toString());
			java.math.BigInteger jmbi2 = new java.math.BigInteger(bi2
					.toString());
			java.math.BigInteger jmbi3 = jmbi1.xor(jmbi2);
			Assert.assertEquals(jmbi3.toString(), bi3.toString());
		}
	}

	@Test
	public void testValueOf() {
		Random r = new Random();
		for (int i = 0; i < RUNCYCLES; i++) {
			long l1 = r.nextLong();
			BigInteger bi = BigInteger.valueOf(l1);
			long l2 = Long.parseLong(bi.toString());
			Assert.assertEquals(l1, l2);
		}
	}

	@Test
	public void testDivisible() {
		for (int i = 0; i < RUNCYCLES; i++) {
			BigInteger bi1 = getPosNegBI(128);
			BigInteger bi2 = getPosNegBI(8);
			boolean divisible = bi1.divisible(bi2);
			java.math.BigInteger jmbi1 = new java.math.BigInteger(bi1
					.toString());
			java.math.BigInteger jmbi2 = new java.math.BigInteger(bi2
					.toString());
			java.math.BigInteger[] jmbi3 = jmbi1.divideAndRemainder(jmbi2);
			Assert.assertTrue((jmbi3[1].signum() != 0 && !divisible)
					|| (jmbi3[1].signum() == 0 && divisible));
		}
	}

	@Test
	public void testSqrt() {
		for (int i = 0; i < RUNCYCLES; i++) {
			BigInteger bi1 = new BigInteger(getRandomNumString(128));
			boolean arithmeticExceptionThrown = false;
			try {
				BigInteger bisqrt[] = bi1.sqrt();
				BigInteger bi3 = bisqrt[0].multiply(bisqrt[0]).add(bisqrt[1]);
				Assert.assertEquals(bi1, bi3);
			} catch (ArithmeticException e) {
				arithmeticExceptionThrown = true;
			}
			Assert.assertTrue(bi1.signum() >= 0 || arithmeticExceptionThrown);
		}
	}

	@Test
	public void testSqrtIntegerPart() {
		for (int i = 0; i < RUNCYCLES; i++) {
			BigInteger bi1 = new BigInteger(getRandomNumString(128));
			boolean arithmeticExceptionThrown = false;
			try {
				BigInteger bisqrt[] = bi1.sqrt();
				Assert.assertEquals(bisqrt[0], bi1.sqrtIntegerPart());
			} catch (ArithmeticException e) {
				arithmeticExceptionThrown = true;
			}
			Assert.assertTrue(bi1.signum() >= 0 || arithmeticExceptionThrown);
		}
	}

	@Test
	public void testIsSqrtWithoutRemainder() {
		for (int i = 0; i < RUNCYCLES; i++) {
			BigInteger bi1 = new BigInteger(getRandomNumString(128));
			boolean arithmeticExceptionThrown = false;
			try {
				BigInteger bisqrt[] = bi1.sqrt();
				boolean isSqrtWithoutRem = bi1.isSqrtWithoutRemainder();
				Assert.assertTrue((bisqrt[1].signum() == 0 && isSqrtWithoutRem)
						|| (bisqrt[1].signum() != 0 && !isSqrtWithoutRem));
			} catch (ArithmeticException e) {
				arithmeticExceptionThrown = true;
			}
			Assert.assertTrue(bi1.signum() >= 0 || arithmeticExceptionThrown);
		}
	}

	@Test
	public void testRoot() {
		Random r = new Random();
		for (int i = 0; i < RUNCYCLES; i++) {
			BigInteger bi1 = new BigInteger(getRandomNumString(128));
			int root = r.nextInt(10) + 2;
			boolean arithmeticExceptionThrown = false;
			try {
				BigInteger biroot[] = bi1.root(root);
				BigInteger bi3 = biroot[0].pow(root).add(biroot[1]);
				Assert.assertEquals(bi1, bi3);
			} catch (ArithmeticException e) {
				arithmeticExceptionThrown = true;
			}
			Assert.assertTrue(!(bi1.signum() < 0 && root % 2 == 0)
					|| arithmeticExceptionThrown);

		}
	}

	@Test
	public void testRootIntegerPart() {
		Random r = new Random();
		for (int i = 0; i < RUNCYCLES; i++) {
			BigInteger bi1 = new BigInteger(getRandomNumString(128));
			int root = r.nextInt(10) + 2;
			boolean arithmeticExceptionThrown = false;
			try {
				BigInteger biroot[] = bi1.root(root);
				Assert.assertEquals(biroot[0], bi1.rootIntegerPart(root));
			} catch (ArithmeticException e) {
				arithmeticExceptionThrown = true;
			}
			Assert.assertTrue(!(bi1.signum() < 0 && root % 2 == 0)
					|| arithmeticExceptionThrown);

		}
	}

	@Test
	public void testLcm() {
		for (int i = 0; i < RUNCYCLES; i++) {
			BigInteger bi1 = new BigInteger(getRandomNumString(128));
			BigInteger bi2 = new BigInteger(getRandomNumString(128));
			BigInteger bi3 = bi1.multiply(bi2).abs().divide(bi1.gcd(bi2));
			Assert.assertEquals(bi3, bi1.lcm(bi2));
		}
	}

	@Test
	public void testFactorial() {
		BigInteger lastFac = BigInteger.ONE;
		for (int i = 2; i < RUNCYCLES; i++) {
			lastFac = lastFac.multiply(new BigInteger(i));
			Assert.assertEquals(lastFac, BigInteger.factorial(i));
		}
	}

	@Test
	public void testBinomal() {
		for (int n = 2; n < 2 * Math.sqrt(RUNCYCLES); n++) {
			for (int k = 1; k < n; k++) {
				/*
				 * Checks that C(n,k)=n!/k!*(n-k)!
				 */
				BigInteger bi1 = BigInteger.binomial(new BigInteger(n), k);
				BigInteger bi2_1 = BigInteger.factorial(n);
				BigInteger bi2_2 = BigInteger.factorial(k).multiply(
						BigInteger.factorial(n - k));
				BigInteger bi2_3 = bi2_1.divide(bi2_2);
				Assert.assertEquals(bi2_3, bi1);
			}
		}
	}

	@Test
	public void testFibonacci() {
		Assert.assertEquals(BigInteger.ZERO, BigInteger.fibonacci(0));
		Assert.assertEquals(BigInteger.ONE, BigInteger.fibonacci(1));
		Assert.assertEquals(BigInteger.ONE, BigInteger.fibonacci(1));
		List<BigInteger> fibonacciSeq = new ArrayList<BigInteger>();
		fibonacciSeq.add(BigInteger.ZERO);
		fibonacciSeq.add(BigInteger.ONE);
		fibonacciSeq.add(BigInteger.ONE);

		for (int i = 3; i < RUNCYCLES; i++) {
			BigInteger f = BigInteger.fibonacci(i);
			Assert.assertEquals(f, fibonacciSeq.get(fibonacciSeq.size() - 1)
					.add(fibonacciSeq.get(fibonacciSeq.size() - 2)));
			fibonacciSeq.add(f);
		}
	}

	@Test
	public void testFibonacciPair() {
		Assert.assertEquals(BigInteger.ZERO, BigInteger.fibonacci(0));
		Assert.assertEquals(BigInteger.ONE, BigInteger.fibonacci(1));
		Assert.assertEquals(BigInteger.ONE, BigInteger.fibonacci(1));
		List<BigInteger> fibonacciSeq = new ArrayList<BigInteger>();
		fibonacciSeq.add(BigInteger.ZERO);
		fibonacciSeq.add(BigInteger.ONE);
		fibonacciSeq.add(BigInteger.ONE);

		for (int i = 3; i < RUNCYCLES; i++) {
			BigInteger f[] = BigInteger.fibonacciPair(i);
			Assert.assertEquals(f[0], fibonacciSeq.get(fibonacciSeq.size() - 1)
					.add(fibonacciSeq.get(fibonacciSeq.size() - 2)));
			Assert
					.assertEquals(f[1], fibonacciSeq
							.get(fibonacciSeq.size() - 1));
			fibonacciSeq.add(f[0]);
		}
	}

	@Test
	@Ignore
	/*
	 * http://en.wikipedia.org/wiki/Diehard_tests
	 */
	public void testRandomBigInteger() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	/*
	 * http://en.wikipedia.org/wiki/Diehard_tests
	 */
	public void testRandomInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testByteValue() {
		Random r = new Random();
		for (int i = 0; i < RUNCYCLES; i++) {
			// will overflow, this is ok
			short shortval = (short) r.nextInt(2 ^ 8);
			BigInteger bi1 = new BigInteger(shortval);
			Assert.assertEquals(shortval, bi1.shortValue());
		}
	}

	@Test
	public void testShortValue() {
		Random r = new Random();
		for (int i = 0; i < RUNCYCLES; i++) {
			// will overflow, this is ok
			short shortval = (short) r.nextInt(2 ^ 16);
			BigInteger bi1 = new BigInteger(shortval);
			Assert.assertEquals(shortval, bi1.shortValue());
		}
	}

	@Test
	public void testHashCode() {
		for (int i = 0; i < RUNCYCLES; i++) {
			BigInteger bi1 = getPosNegBI(256);
			BigInteger bi2 = bi1.add(BigInteger.ZERO);
			Assert.assertTrue(bi2.hashCode() == bi1.hashCode());
		}
	}

}
