package us.altio.gmp4j.tests.bench.pi;

import java.math.BigInteger;



public class JMBPIDigits {

	public String showDigits(int maxDigits) {
		int digits=0;
		StringBuffer sb = new StringBuffer();
		BigInteger TWO = new BigInteger("2");
		BigInteger TEN = new BigInteger("10");
		BigInteger k = TWO;
		BigInteger a = new BigInteger("4");
		BigInteger b = BigInteger.ONE;
		BigInteger a1 = new BigInteger("12");
		BigInteger b1 = a;
		for (int i = 0;; i++) {
			BigInteger p = k.pow(2);
			BigInteger q = (TWO.multiply(k)).add(BigInteger.ONE);
			k = k.add(BigInteger.ONE);
			BigInteger tempa1 = a1;
			BigInteger tempb1 = b1;
			a1 = (p.multiply(a)).add(q.multiply(a1));
			b1 = (p.multiply(b)).add(q.multiply(b1));
			a = tempa1;
			b = tempb1;
			// Print common digits
			BigInteger d = a.divide(b);
			BigInteger d1 = a1.divide(b1);
			while (d.equals(d1)) {
				sb.append(d);
				if (digits>=maxDigits)
					return sb.toString();
				digits++;
				a = TEN.multiply(a.mod(b));
				a1 = TEN.multiply(a1.mod(b1));
				d = a.divide(b);
				d1 = a1.divide(b1);
			}
		}
	}
}