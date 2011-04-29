package us.altio.gmp4j.tests.bench.naivefib;

import java.math.BigInteger;
import java.util.ArrayList;

public class JMBFibonacci {  
    ArrayList<BigInteger> fibList= new ArrayList<BigInteger>();  

    public BigInteger fib(int n)  
    {         
        BigInteger f;  
        if(n <= 1)  
        {  
            f = BigInteger.ONE;  
            if(fibList.size() <= 1)  
                fibList.add(f);  
            return f;  
        }  
        f = (n < fibList.size()) ? (fibList.get(n - 2).add(fibList.get(n - 1))) : fib(n - 2).add(fib(n - 1));  
        if(n >= fibList.size()) fibList.add(f);  
        return f;  
    }  

}  