package us.altio.gmp4j.tests.bench.naivefib;

import java.util.ArrayList;

import us.altio.gmp4j.BigInteger;

public class GMP4JFibonacci {
	ArrayList<BigInteger> fibList = new ArrayList<BigInteger>();

	public BigInteger fib(int n) {
		BigInteger f;
		if (n <= 1) {
			f = BigInteger.ONE;
			if (fibList.size() <= 1)
				fibList.add(f);
			return f;
		}
		if (n <= 2) {
			f = BigInteger.ONE;
			if (fibList.size() <= 2)
				fibList.add(f);
			return f;
		}
		f = (n < fibList.size()) ? (fibList.get(n - 2).add(fibList.get(n - 1)))
				: fib(n - 2).add(fib(n - 1));
		if (n >= fibList.size())
			fibList.add(f);
		return f;
	}

}