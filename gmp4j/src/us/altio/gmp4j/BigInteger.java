package us.altio.gmp4j;

import us.altio.gmp4j.Pointer;

@SuppressWarnings("serial")
public class BigInteger extends Number implements Comparable<BigInteger>/* , Cloneable , Serializable, Externalizable*/ {
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
		natMpzInitSetSi(0l);
	}

	public BigInteger(String val) {
		this(val, 10);
	}

	// TODO: @altmind perform effective JMB->BI conversion, not using string
	public BigInteger(java.math.BigInteger bi) {
		this(bi.toString());
	}

	public BigInteger(String val, int radix) {
		// check radix 2..36
		this();
		natMpzSetStr(val, radix);
	}

	private BigInteger(Pointer p) {
		if (p == null)
			throw new NullPointerException("GMP operation failed.");
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
		if (count == 0 && prngInitialized) {
			natMyFreePrngState(prngState);
			prngState = null;
			prngInitialized = false;
		}
		natFinalize();
	}

	private native Pointer natMpzAdd(Pointer p1, Pointer p2);

	private native Pointer natMpzSub(Pointer p1, Pointer p2);

	private native Pointer natMpzMul(Pointer p1, Pointer p2);

	private native void natMpzInitSetSi(long v);

	private native int natMpzSetStr(String s, int radix);

	private native String natMpzGetStr(int radix);

	private native Pointer natMpzPowUi(Pointer p, int v);

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

	static private native Pointer natMpzUrandomb(Pointer prngstate,
			int bitLength);

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

	//private native void natInitialize();

	private native void natFinalize();

	private static native void natMyFreePrngState(Pointer p);

	private static native void natInitializeLibrary();

	@Override
	public boolean equals(Object p) {
		if (p == null || !(p instanceof BigInteger))
			return false;
		return natMpzCmp(this.native_ptr, ((BigInteger) p).native_ptr) == 0;
	}

	@Override
	public int compareTo(BigInteger p) {
		if (p == null)
			throw new NullPointerException("Null argument was passed");
		return natMpzCmp(this.native_ptr, p.native_ptr);
	}
	
	@Override 
	/**
	 * TODO: find a better/elegant way of hashing.
	 */
	public int hashCode()
	{
		return this.toString().hashCode();
	}
	
	public BigInteger abs() {
		return new BigInteger(natMpzAbs(this.native_ptr));
	}

	public BigInteger add(BigInteger p) {
		if (p == null)
			throw new NullPointerException("Null argument was passed");
		return new BigInteger(natMpzAdd(this.native_ptr, p.native_ptr));
	}

	public BigInteger and(BigInteger p) {
		if (p == null)
			throw new NullPointerException("Null argument was passed");
		return new BigInteger(natMpzAnd(this.native_ptr, p.native_ptr));
	}

	public BigInteger andNot(BigInteger p) {
		if (p == null)
			throw new NullPointerException("Null argument was passed");
		return new BigInteger(natMpzAnd(this.native_ptr,
				natMpzNot(p.native_ptr)));
	}

	public int bitCount() {
		return (int) natMpzPopcount(this.native_ptr);
	}

	public int bitLength() {
		return (int) natMpzSizeInBase2(this.native_ptr);
	}

	public BigInteger clearBit(int n) {
		if (n < 0)
			throw new IllegalArgumentException("n should be >= 0");
		return new BigInteger(natMpzClrBit(this.native_ptr, n));
	}

	public BigInteger divide(BigInteger p) {
		if (p == null)
			throw new NullPointerException("Null argument was passed");
		if (p.signum() == 0)
			throw new ArithmeticException("Cannot divide by zero.");
		return new BigInteger(natMpzTdivQ(this.native_ptr, p.native_ptr));
	}

	public BigInteger[] divideAndRemainder(BigInteger p) {
		if (p == null)
			throw new NullPointerException("Null argument was passed");
		if (p.signum() == 0)
			throw new ArithmeticException("Cannot divide by zero.");
		Pointer[] ret = natMpzTdivQR(this.native_ptr, p.native_ptr);
		BigInteger[] result = new BigInteger[] { new BigInteger(ret[0]),
				new BigInteger(ret[1]) };
		return result;
	}

	public BigInteger flipBit(int n) {
		if (n < 0)
			throw new IllegalArgumentException("n should be >= 0");
		return new BigInteger(natMpzFlipBit(this.native_ptr, n));
	}

	public BigInteger gcd(BigInteger p) {
		if (p == null)
			throw new NullPointerException("Null argument was passed");
		return new BigInteger(natMpzGcd(this.native_ptr, p.native_ptr));
	}

	public int getLowestSetBit() {
		return (int) natMpzScan1(this.native_ptr);
	}

	public boolean isProbablePrime(int certainty) {
		/*
		 * MpzProbabPrime may return 0, 1, 2 where 0 is definitely NOT prime 1
		 * is not definitely prime and 2 is definitely prime
		 */
		if (certainty < 0)
			throw new IllegalArgumentException("certainty should be >= 0");
		return natMpzProbabPrime(this.native_ptr, certainty) == 0 ? false
				: true;
	}

	public BigInteger max(BigInteger val) {
		if (val == null)
			throw new NullPointerException("Null argument was passed");
		if (compareTo(val) >= 0)
			return this;
		else
			return val;
	}

	public BigInteger min(BigInteger val) {
		if (val == null)
			throw new NullPointerException("Null argument was passed");
		if (compareTo(val) <= 0)
			return this;
		else
			return val;
	}

	public BigInteger mod(BigInteger p) {
		if (p == null)
			throw new NullPointerException("Null argument was passed");
		if (p.signum()<=0)
			throw new ArithmeticException("Modulus not positive");
		return new BigInteger(natMpzMod(this.native_ptr, p.native_ptr));
	}

	public BigInteger modInverse(BigInteger p) {
		if (p == null)
			throw new NullPointerException("Null argument was passed");
		if (p.signum()<=0)
			throw new ArithmeticException("Modulus not positive");
		return new BigInteger(natMpzInvert(this.native_ptr, p.native_ptr));
	}

	public BigInteger modPow(BigInteger pow, BigInteger mod) {
		if (pow == null || mod == null)
			throw new NullPointerException("Null argument was passed");
		if (pow.signum() == -1)
			throw new IllegalArgumentException("negative pow are forbidden");
		if (mod.signum()<=0)
			throw new ArithmeticException("Modulus not positive");
		return new BigInteger(natMpzPowm(this.native_ptr, pow.native_ptr,
				mod.native_ptr));
	}

	public BigInteger multiply(BigInteger p) {
		if (p == null)
			throw new NullPointerException("Null argument was passed");
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
		if (p == null)
			throw new NullPointerException("Null argument was passed");
		return new BigInteger(natMpzOr(this.native_ptr, p.native_ptr));
	}

	public BigInteger pow(int p) {
		if (p < 0)
			throw new IllegalArgumentException("p should be >= 0");
		return new BigInteger(natMpzPowUi(this.native_ptr, p));
	}

	public BigInteger remainder(BigInteger p) {
		if (p == null)
			throw new NullPointerException("Null argument was passed");
		if (p.signum() == 0)
			throw new ArithmeticException("Division by zero");
		return new BigInteger(natMpzTdivR(this.native_ptr, p.native_ptr));
	}

	public BigInteger setBit(int n) {
		if (n < 0)
			throw new IllegalArgumentException("n should be >= 0");
		return new BigInteger(natMpzSetBit(this.native_ptr, n));
	}

	public BigInteger shiftLeft(int n) {
		if (n < 0)
			throw new IllegalArgumentException("n should be >= 0");
		return new BigInteger(natMyMpzShl(this.native_ptr, n));
	}

	public BigInteger shiftRight(int n) {
		if (n < 0)
			throw new IllegalArgumentException("n should be >= 0");
		return new BigInteger(natMyMpzShr(this.native_ptr, n));
	}

	public int signum() {
		return natMpzSgn(this.native_ptr);
	}

	public BigInteger subtract(BigInteger p) {
		if (p == null)
			throw new NullPointerException("Null argument was passed");
		return new BigInteger(natMpzSub(this.native_ptr, p.native_ptr));
	}

	public boolean testBit(int n) {
		if (n < 0)
			throw new IllegalArgumentException("n should be >= 0");
		return natMpzTstBit(this.native_ptr, n) == 1 ? true : false;
	}

	public byte[] toByteArray() {
		throw new RuntimeException("Not Implemented");
	}

	public BigInteger xor(BigInteger p) {
		if (p == null)
			throw new NullPointerException("Null argument was passed");
		return new BigInteger(natMpzXor(this.native_ptr, p.native_ptr));
	}

	public static BigInteger valueOf(long val) {
		return new BigInteger(val);
	}

	/* EXTENSION METHODS */

	public BigInteger divQuotent(BigInteger p) {
		if (p == null)
			throw new NullPointerException("Null argument was passed");
		if (p.signum() == 0)
			throw new ArithmeticException("Cannot divide by zero.");
		return new BigInteger(natMpzTdivQ(this.native_ptr, p.native_ptr));
	}

	public BigInteger divRemainder(BigInteger p) {
		if (p == null)
			throw new NullPointerException("Null argument was passed");
		if (p.signum() == 0)
			throw new ArithmeticException("Cannot divide by zero.");
		return new BigInteger(natMpzTdivR(this.native_ptr, p.native_ptr));
	}

	public boolean divisible(BigInteger p) {
		if (p == null)
			throw new NullPointerException("Null argument was passed");
		if (p.signum() == 0)
			throw new ArithmeticException("Cannot divide by zero.");
		return natMpzDivisible(this.native_ptr, p.native_ptr);
	}

	public BigInteger[] sqrt() {
		if (this.signum() == -1)
			throw new ArithmeticException(
					"Cannot take sqrt of negative nubmer.");
		Pointer[] ret = natMpzSqrtRem(this.native_ptr);
		BigInteger[] result = new BigInteger[] { new BigInteger(ret[0]),
				new BigInteger(ret[1]) };
		return result;
	}

	public BigInteger sqrtIntegerPart() {
		if (this.signum() == -1)
			throw new ArithmeticException(
					"Cannot take sqrt of negative nubmer.");
		return new BigInteger(natMpzSqrt(this.native_ptr));
	}

	public boolean isSqrtWithoutRemainder() {
		return natMpzPerfectSquare(this.native_ptr);
	}

	public BigInteger[] root(long pow) {
		if (pow % 2 == 0 && this.signum() == -1)
			throw new ArithmeticException(
					"Cannot take even-root of negative number.");
		Pointer[] ret = natMpzRootRem(this.native_ptr, pow);
		BigInteger[] result = new BigInteger[] { new BigInteger(ret[0]),
				new BigInteger(ret[1]) };
		return result;
	}

	public BigInteger rootIntegerPart(long pow) {
		if (pow % 2 == 0 && this.signum() == -1)
			throw new ArithmeticException(
					"Cannot take even-root of negative number.");
		return new BigInteger(natMpzRoot(this.native_ptr, pow));
	}

	public BigInteger lcm( BigInteger p) {
		if (p == null)
			throw new NullPointerException("Null argument was passed");
		return new BigInteger(natMpzLcm(this.native_ptr, p.native_ptr));
	}

	static public BigInteger factorial(long p) {
		if (p<0)
			throw new IllegalArgumentException("p should be >= 0");
		return new BigInteger(natMpzFac(p));
	}

	static public BigInteger binomal(BigInteger n, long k) {
		if (n == null)
			throw new NullPointerException("Null argument was passed");
		if (n.signum()<=0 || k<=0)
			throw new IllegalArgumentException("Both n and k should be positive");
		return new BigInteger(natMpzBin(n.native_ptr, k));
	}

	static public BigInteger fibonacci(long p) {
		if (p<0)
			throw new IllegalArgumentException("p should be >= 0");
		return new BigInteger(natMpzFib(p));
	}

	/*
	 * Returns fibonacci(p) and fibonacci(p-1). In that order.
	 */
	static public BigInteger[] fibonacciPair(long p) {
		if (p<0)
			throw new IllegalArgumentException("p should be >= 0");
		Pointer[] ret = natMpzFib2(p);
		BigInteger[] result = new BigInteger[] { new BigInteger(ret[0]),
				new BigInteger(ret[1]) };
		return result;
	}

	static private void initializePrngIfNeeded() {
		if (!prngInitialized) {
			prngState = natMpzRandinit(System.nanoTime() + seedUniquifier);
			prngInitialized = true;
		}
	}

	/*
	 * Generates uniformely distributed random number betweeen 0 and max.
	 */
	static public BigInteger random(BigInteger max) {
		if (max.signum()<=0)
			throw new IllegalArgumentException("max should be positive");
		initializePrngIfNeeded();
		return new BigInteger(natMpzUrandomm(prngState, max.native_ptr));
	}

	/*
	 * Generates uniformely distributed random number betweeen 0 and (2^n)-1.
	 */
	static public BigInteger random(int bitLength) {
		if (bitLength < 1)
			throw new IllegalArgumentException("bitLength should be > 0");
		initializePrngIfNeeded();
		return new BigInteger(natMpzUrandomb(prngState, bitLength));
	}

}
