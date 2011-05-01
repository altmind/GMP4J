package us.altio.gmp4j.tests;

import java.util.Random;

public class TestUtils {

	static String genStringFromChars(int len, String chars)
	{
		StringBuffer sb = new StringBuffer();
		Random r= new Random();
		for (int i=0;i<len;i++)
		{
			sb.append(chars.charAt(r.nextInt(chars.length())));
		}
		return sb.toString();
	}
}
