package us.altio.gmp4j.tests.bench.takashi;

import java.math.BigInteger;

/**
 * Takahashi Daisuke Fibonacci calculation.
 * Translation from python at 
 * http://www.stevekrenzel.com/articles/fibonacci
 *
 */
public class JMBTakashi {

	BigInteger MINUSTWO = new BigInteger("-2");
	BigInteger TWO = new BigInteger("2");
	BigInteger FIVE = new BigInteger("5");
	BigInteger SIX = new BigInteger("6");

	public static void main(String[] args) {
		JMBTakashi f = new JMBTakashi();
		System.out.println(f.fib(1400000).toString().length());
	}

	public double logBase(double n, int base) {
		return Math.log(n) / Math.log(base);
	}

	public BigInteger fib(long l) {
		if (l == 0)
			throw new RuntimeException("l should not be 0");
		BigInteger lbi = BigInteger.valueOf(l);
		BigInteger F = BigInteger.ONE;
		BigInteger L = BigInteger.ONE;
		BigInteger sign = MINUSTWO;
		long exp = (long) logBase(l, 2);
		BigInteger mask = TWO.pow((int) exp);
		for (int i = 0; i < exp - 1; i++) {
			mask = mask.shiftRight(1);
			//BigInteger F2 = F.pow(2);
			BigInteger F2 = F.multiply(F);
			//BigInteger FL2 = (F.add(L)).pow(2);
			BigInteger intrm=F.add(L);
			BigInteger FL2 = intrm.multiply(intrm);
			System.out.println("FL2 sz: "+(long)(FL2.bitLength()/8));
			F = FL2.subtract(F2.multiply(SIX)).shiftRight(1).subtract(sign);
			System.out.println("F sz: "+(long)(F.bitLength()/8));
			// l&mask
			if (mask.and(lbi).signum() != 0) {
				BigInteger temp = FL2.shiftRight(2).add(F2);
				L = temp.add(F.shiftLeft(1));
				F = temp;
			} else {
				L = FIVE.multiply(F2).add(sign);
			}
			if (mask.and(lbi).signum() != 0)
				sign = MINUSTWO;
			else
				sign = TWO;

		}
		if (lbi.and(mask.shiftRight(1)).signum() == 0) {
			return F.multiply(L);
		} else {
			return F.add(L).shiftRight(1).multiply(L).subtract(sign.shiftRight(1));
		}
	}

}
