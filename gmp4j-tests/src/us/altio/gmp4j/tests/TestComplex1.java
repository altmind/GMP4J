package us.altio.gmp4j.tests;

import java.util.Properties;

import org.junit.Test;

import us.altio.gmp4j.BigInteger;


public class TestComplex1 {
	@Test
	public void testComplex1()
	{
		for(int i=0; i<25600; i++)
		{
			BigInteger bi = BigInteger.random(i*i);
			System.out.println((bi.bitLength()/(1024*8))+"KB");
			if (i%100==0) System.gc();
		}
	}
/*
	@Test
	public void testComplex1()
	{
		long t1 = System.nanoTime();
		BigInteger bi = new BigInteger(31337);
		for (int i=0;i<10;i++)
		{
			bi=bi.multiply(bi);
		}
		long dt = System.nanoTime()-t1;
		System.out.println("GMP4J: "+dt/1000);
	}
	@Test
	public void testComplex2()
	{
		long t1 = System.nanoTime();
		java.math.BigInteger bi = new java.math.BigInteger("31337");
		for (int i=0;i<10;i++)
		{
			bi=bi.multiply(bi);
		}
		long dt = System.nanoTime()-t1;
		System.out.println("java.math.BigInteger: "+(dt/1000));
	}*/
}
