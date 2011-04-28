package us.altio.gmp4j;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class BigInteger extends Number implements Comparable<BigInteger>, Externalizable {
	private Pointer native_ptr;
	private static volatile long seedUniquifier = 8682522807148012L;
	public static long count = 0;

	public static BigInteger MINUSONE = null;
	public static BigInteger ZERO = null;
	public static BigInteger ONE = null;
	public static BigInteger TEN = null;
	private static boolean prngInitialized;
	private static Pointer prngState;

	static {
		System.loadLibrary("gmp4j");
		natInitializeLibrary();
		MINUSONE = new BigInteger(-1l);
		ZERO = new BigInteger(0l);
		ONE = new BigInteger(1l);
		TEN = new BigInteger(10l);
	}

	public BigInteger(long v) {
		natMpzInitSetSi(v);
	}

	public BigInteger() {
		natInitialize();
	}

	public BigInteger(String val) {
		this(val, 10);
	}

	public BigInteger(String val, int radix) {
		// check radix 2..36
		this();
		natMpzSetStr(val, radix);
	}

	private BigInteger(Pointer p) {
		native_ptr = p;
	}

	public String toString() {
		return toString(10);
	}

	public String toString(int radix) {
		return natMpzGetStr(radix);
	}

	@Override
	public double doubleValue() {
		return natGetD(this.native_ptr);
	}

	@Override
	public float floatValue() {
		return (float) doubleValue();
	}

	@Override
	public int intValue() {
		return (int) longValue();
	}

	@Override
	public long longValue() {
		return natGetSi(this.native_ptr);
	}

	protected void finalize() throws Throwable {
		count--;
		if (count==0 && prngInitialized)
		{
			natMyFreePrngState(prngState);
			prngState=null;
			prngInitialized=false;
		}
		natFinalize();
	}

	private native Pointer natMpzAdd(Pointer p1, Pointer p2);

	private native Pointer natMpzSub(Pointer p1, Pointer p2);

	private native Pointer natMpzMul(Pointer p1, Pointer p2);

	private native void natMpzInitSetSi(long v);

	private native int natMpzSetStr(String s, int radix);

	private native String natMpzGetStr(int radix);

	
	private native Pointer natMpzPowUi(Pointer p, long v);
	private native Pointer natMpzTdivQ(Pointer p1, Pointer p2);
	private native Pointer natMpzTdivR(Pointer p1, Pointer p2);
	private native boolean natMpzDivisible(Pointer p1, Pointer p2);
	private native Pointer[] natMpzTdivQR(Pointer p1, Pointer p2);
	private native Pointer natMpzMod(Pointer p1, Pointer p2);
	private native Pointer natMpzPowm(Pointer p1, Pointer p2, Pointer p3);
	private native Pointer natMpzInvert(Pointer p1, Pointer p2);
	private native Pointer[] natMpzSqrtRem(Pointer p);
	private native Pointer natMpzSqrt(Pointer p);
	private native boolean natMpzPerfectSquare(Pointer p);
	private native Pointer[] natMpzRootRem(Pointer p, long pow);
	private native Pointer natMpzRoot(Pointer p, long pow);
	
	static private native Pointer natMpzGcd(Pointer p1, Pointer p2);
	static private native Pointer natMpzLcm(Pointer p1, Pointer p2);
	static private native Pointer natMpzFac(long p);
	static private native Pointer natMpzBin(Pointer p1, long k);
	static private native Pointer natMpzFib(long v);
	static private native Pointer[] natMpzFib2(long v);
	
	static private native Pointer natMpzRandinit(long l);
	private native int natMpzProbabPrime(Pointer p, int certainty);
	private native Pointer natMpzNextprime(Pointer nativePtr);
	static private native Pointer natMpzUrandomm(Pointer prngstate, Pointer p);
	static private native Pointer natMpzUrandomb(Pointer prngstate, int bitLength);

	private native Pointer natMyMpzShl(Pointer p, long n);
	private native Pointer natMyMpzShr(Pointer p, long n);
	private native Pointer natMpzAnd(Pointer p1, Pointer p2);
	private native Pointer natMpzOr(Pointer p1, Pointer p2);
	private native Pointer natMpzXor(Pointer p1, Pointer p2);
	private native Pointer natMpzNot(Pointer p);
	private native int natMpzTstBit(Pointer p, long n);
	private native Pointer natMpzSetBit(Pointer p, long n);
	private native Pointer natMpzClrBit(Pointer p, long n);
	private native Pointer natMpzFlipBit(Pointer p, long n);
	
	private native long natMpzScan1(Pointer nativePtr);
	
	private native long natMpzSizeInBase2(Pointer p);
	private native long natMpzPopcount(Pointer p);

	
	private native Pointer natMpzNeg(Pointer p);

	private native Pointer natMpzAbs(Pointer p);

	private native int natMpzSgn(Pointer p);
	private native int natMpzCmp(Pointer p1, Pointer p2);

	private native double natGetD(Pointer p);

	private native long natGetSi(Pointer p);

	private native void natInitialize();

	private native void natFinalize();
	private static native void natMyFreePrngState(Pointer p);

	private static native void natInitializeLibrary();

	@Override
	public boolean equals(Object p) {
		if (p==null || !(p instanceof BigInteger))
			return false;
		return natMpzCmp(this.native_ptr, ((BigInteger)p).native_ptr) == 0;
	}
	
	@Override
	public int compareTo(BigInteger p) {
		return natMpzCmp(this.native_ptr, p.native_ptr);
	}


	public BigInteger abs() {
		return new BigInteger(natMpzAbs(this.native_ptr));
	}


	public BigInteger add(BigInteger p) {
		return new BigInteger(natMpzAdd(this.native_ptr, p.native_ptr));
	}


	public BigInteger and(BigInteger p) {
		return new BigInteger(natMpzAnd(this.native_ptr, p.native_ptr));
	}


	public BigInteger andNot(BigInteger p) {
		return new BigInteger(natMpzAnd(this.native_ptr,natMpzNot(p.native_ptr)));
	}


	public int bitCount() {
		return (int)natMpzPopcount(this.native_ptr);
	}

	public int bitLength() {
		return (int)natMpzSizeInBase2(this.native_ptr);
	}


	public BigInteger clearBit(int n) {
		return new BigInteger(natMpzClrBit(this.native_ptr, n));
	}


	public BigInteger divide(BigInteger p) {
		return new BigInteger(natMpzTdivQ(this.native_ptr, p.native_ptr));
	}


	public BigInteger[] divideAndRemainder(BigInteger p) {
		Pointer[] ret = natMpzTdivQR(this.native_ptr, p.native_ptr);
		BigInteger[] result = new BigInteger[] { new BigInteger(ret[0]),
				new BigInteger(ret[1]) };
		return result;
	}


	public BigInteger flipBit(int n) {
		return new BigInteger(natMpzFlipBit(this.native_ptr, n));
	}


	public BigInteger gcd(BigInteger p) {
		return new BigInteger(natMpzGcd(this.native_ptr, p.native_ptr));
	}


	public int getLowestSetBit() {
		return (int)natMpzScan1(this.native_ptr);
	}

	public boolean isProbablePrime(int certainty) {
		/* MpzProbabPrime may return 0, 1, 2
			where 0 is definitely NOT prime
			1 is not definitely prime
			and 2 is definitely prime
		 */
		return natMpzProbabPrime(this.native_ptr, certainty)==0?false:true;
	}

	public BigInteger max(BigInteger val) {
		if (compareTo(val)>=0)
			return this;
		else return val;
	}


	public BigInteger min(BigInteger val) {
		if (compareTo(val)<=0)
			return this;
		else return val;
	}


	public BigInteger mod(BigInteger p) {
		return new BigInteger(natMpzMod(this.native_ptr,
				p.native_ptr));
	}


	public BigInteger modInverse(BigInteger p) {
		return new BigInteger(natMpzInvert(this.native_ptr, p.native_ptr));
	}


	public BigInteger modPow(BigInteger pow, BigInteger mod) {
		return new BigInteger(natMpzPowm(this.native_ptr, pow.native_ptr,
				mod.native_ptr));
	}


	public BigInteger multiply(BigInteger p) {
		return new BigInteger(natMpzMul(this.native_ptr, p.native_ptr));
	}


	public BigInteger negate() {
		return new BigInteger(natMpzNeg(this.native_ptr));
	}


	public BigInteger nextProbablePrime() {
		return new BigInteger(natMpzNextprime(this.native_ptr));
	}

	public BigInteger not() {
		return new BigInteger(natMpzNot(this.native_ptr));
	}


	public BigInteger or(BigInteger p) {
		return new BigInteger(natMpzOr(this.native_ptr, p.native_ptr));
	}


	public BigInteger pow(int p) {
		return new BigInteger(natMpzPowUi(this.native_ptr, p));
	}


	public BigInteger remainder(BigInteger p) {
		return new BigInteger(natMpzTdivR(this.native_ptr, p.native_ptr));
	}


	public BigInteger setBit(int n) {
		return new BigInteger(natMpzSetBit(this.native_ptr, n));
	}


	public BigInteger shiftLeft(int n) {
		return new BigInteger(natMyMpzShl(this.native_ptr, n));
	}


	public BigInteger shiftRight(int n) {
		return new BigInteger(natMyMpzShr(this.native_ptr, n));
	}


	public int signum() {
		return natMpzSgn(this.native_ptr);
	}

	public BigInteger subtract(BigInteger p) {
		return new BigInteger(natMpzSub(this.native_ptr, p.native_ptr));
	}


	public boolean testBit(int n) {
		return natMpzTstBit(this.native_ptr, n)==1?true:false;
	}


	public byte[] toByteArray() {
		// TODO Auto-generated method stub
		return null;
	}


	public BigInteger xor(BigInteger p) {
		return new BigInteger(natMpzXor(this.native_ptr, p.native_ptr));
	}
	
	/* STATIC METHODS */
	
	/*
	 * rnd is effectively disregarded. safe to pass null - builtin random generator will be used.
	 * GMP makes no guarantees of security of generated numer.
	 */
	public int probablePrime(int certainty) {
		return natMpzProbabPrime(this.native_ptr, certainty);
	}
	

	public static BigInteger valueOf(long val) {
		return new BigInteger(val);
	}
	
	/* EXTENSION METHODS */
	
	public BigInteger divQuotent(BigInteger p) {
		return new BigInteger(natMpzTdivQ(this.native_ptr, p.native_ptr));
	}

	public BigInteger divRemainder(BigInteger p) {
		return new BigInteger(natMpzTdivR(this.native_ptr, p.native_ptr));
	}
	
	public boolean divisible(BigInteger p) {
		return natMpzDivisible(this.native_ptr, p.native_ptr);
	}
	

	public BigInteger[] div(BigInteger p) {
		Pointer[] ret = natMpzTdivQR(this.native_ptr, p.native_ptr);
		BigInteger[] result = new BigInteger[] { new BigInteger(ret[0]),
				new BigInteger(ret[1]) };
		return result;
	}

	public BigInteger[] sqrt() {
		Pointer[] ret = natMpzSqrtRem(this.native_ptr);
		BigInteger[] result = new BigInteger[] { new BigInteger(ret[0]),
				new BigInteger(ret[1]) };
		return result;
	}

	public BigInteger sqrtIntegerPart() {
		return new BigInteger(natMpzSqrt(this.native_ptr));
	}
	
	public boolean isSqrtWithoutRemainder() {
		return natMpzPerfectSquare(this.native_ptr);
	}

	public BigInteger[] root(long pow) {
		Pointer[] ret = natMpzRootRem(this.native_ptr, pow);
		BigInteger[] result = new BigInteger[] { new BigInteger(ret[0]),
				new BigInteger(ret[1]) };
		return result;
	}

	public BigInteger rootIntegerPart(long pow) {
		return new BigInteger(natMpzRoot(this.native_ptr, pow));
	}

	static public BigInteger lcm(BigInteger p1, BigInteger p2) {
		return new BigInteger(natMpzLcm(p1.native_ptr, p2.native_ptr));
	}

	static public BigInteger factorial(long p) {
		return new BigInteger(natMpzFac(p));
	}

	static public BigInteger binomal(BigInteger n, long k) {
		return new BigInteger(natMpzBin(n.native_ptr, k));
	}

	static public BigInteger fibonacci(long p) {
		return new BigInteger(natMpzFib(p));
	}

	/*
	 * Returns fibonacci(p) and fibonacci(p-1). In that order.
	*/
	static public BigInteger[] fibonacciPair(long p) {
		Pointer[] ret = natMpzFib2(p);
		BigInteger[] result = new BigInteger[] { new BigInteger(ret[0]),
				new BigInteger(ret[1]) };
		return result;
	}
	
	static private void initializePrngIfNeeded()
	{
		if (!prngInitialized)
		{
			prngState = natMpzRandinit(System.nanoTime()+seedUniquifier);
			prngInitialized=true;
		}
	}
	/*
	 * Generates uniformely distributed random number betweeen 0 and max.
	 */
	static public BigInteger random(BigInteger max) {
		initializePrngIfNeeded();
		return new BigInteger(natMpzUrandomm(prngState, max.native_ptr));
	}
	
	/*
	 * Generates uniformely distributed random number betweeen 0 and (2^n)-1.
	 */
	static public BigInteger random(int bitLength) {
		initializePrngIfNeeded();
		return new BigInteger(natMpzUrandomb(prngState, bitLength));
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		// TODO Auto-generated method stub
		
	}

	
}
