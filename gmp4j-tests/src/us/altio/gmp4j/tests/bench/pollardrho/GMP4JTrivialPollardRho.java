package us.altio.gmp4j.tests.bench.pollardrho;

import us.altio.gmp4j.BigInteger;

public class GMP4JTrivialPollardRho {

    private final static BigInteger ONE  = BigInteger.ONE;
    private final static BigInteger TWO  = new BigInteger(2l);

    
    protected BigInteger rho(BigInteger N) {
        BigInteger divisor;
        BigInteger c  = BigInteger.random(N.bitLength());
        BigInteger x  = BigInteger.random(N.bitLength());
        BigInteger xx = x;

        if (N.divisible(TWO)) return TWO;

        do {
            x  =  x.multiply(x).mod(N).add(c).mod(N);
            xx = xx.multiply(xx).mod(N).add(c).mod(N);
            xx = xx.multiply(xx).mod(N).add(c).mod(N);
            divisor = x.subtract(xx).gcd(N);
        } while(divisor.equals(ONE));

        return divisor;
    }

    public BigInteger factor(BigInteger N) {
        if (N.compareTo(ONE) == 0) throw new IllegalArgumentException("Cannot factor ONE");
        if (N.isProbablePrime(20)) { return N;}
        BigInteger divisor = rho(N);
        return factor(divisor);
    }
	
	
}
