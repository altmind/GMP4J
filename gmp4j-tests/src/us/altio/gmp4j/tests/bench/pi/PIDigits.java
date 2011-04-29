package us.altio.gmp4j.tests.bench.pi;

import junit.framework.Assert;

import org.junit.Test;

public class PIDigits {

	@Test
	public void testPIDigits() {
		GMP4JPIDigits gpd = new GMP4JPIDigits();
		JMBPIDigits jpd = new JMBPIDigits();

		for (int i = 1000; i < 16000; i+=1000) {
			long t1=System.nanoTime();
			String s1=gpd.showDigits(i);
			long t2=System.nanoTime();
			System.gc();
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
			}
			long t3=System.nanoTime();
			String s2=jpd.showDigits(i);
			System.gc();
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
			}
			long t4=System.nanoTime();
			System.out.println(i+"\t"+(t2-t1)/1000000+"\t"+(t4-t3)/1000000);
			Assert.assertEquals(s1, s2);
			
		}
	}

}
