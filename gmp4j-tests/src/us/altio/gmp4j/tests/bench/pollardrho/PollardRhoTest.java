package us.altio.gmp4j.tests.bench.pollardrho;

import java.security.SecureRandom;

import junit.framework.Assert;

import org.junit.Test;

import us.altio.gmp4j.BigInteger;

public class PollardRhoTest {

	@Test
	public void testPollardRho() {
		while (true) {
			String i = java.math.BigInteger.probablePrime(32, new SecureRandom()).multiply(java.math.BigInteger.probablePrime(32, new SecureRandom())).toString();
			//String i = BigInteger.random(80).multiply(BigInteger.random(80)).toString();
			System.out.println("["+i+"] ");

			GMP4JTrivialPollardRho gpr = new GMP4JTrivialPollardRho();
			JMBPollardRho jpr = new JMBPollardRho();
			
			long t1=System.nanoTime();
			BigInteger r1 = gpr.factor(new BigInteger(i));
			BigInteger r12 = new BigInteger(i).divide(r1);
			BigInteger r13 = r1.multiply(r12);
			long t2=System.nanoTime();
			java.math.BigInteger r2 = jpr.factor(new java.math.BigInteger(i));
			java.math.BigInteger r22 = new java.math.BigInteger(i).divide(r2);
			java.math.BigInteger r23 = r2.multiply(r22);
			long t3=System.nanoTime();
			
			System.out.println((t2-t1)/1000000+" "+(t3-t2)/1000000);
			String rs2=r23.toString();
			String rs1=r13.toString();
			if (!rs2.equals(rs1))
			{
				System.out.println("\n JM"+rs1+"/GMP"+rs2);
			}
			Assert.assertEquals(rs2, rs1);

		}
	}
}
