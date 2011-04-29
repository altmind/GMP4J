#include <jni.h>
#include "common.h"
#include <gmp.h>
#include <assert.h>
#include <stdlib.h>
#include <stdio.h>
#include "../bin/us_altio_gmp4j_BigInteger.h"

/*
 * us.altio.gmp4j.BigInteger
 *
 * Fast Math operations for Java using native GMP.
 *
 * NB on implementation: most methods receive params, perform operations and return result in NEW GMP object
 * this pointer to GMP object is then passed to java's BI wrapper constuctor: private BigInteger(Pointer p).
 */

/*
 * TODO: @altmind For better effectiveness we should implement saving/restoring GMP object from byte array. This
 * 	 could be used for serialization.
 * TODO: @altmind generalize creation of 2 array-objects of BI.
 */

#ifdef __cplusplus
extern "C" {
#endif

/*
 * Pointer to (us.altio.gmp4j.Pointer)native_ptr in class
 */
static jfieldID native_ptr;

JNIEXPORT void JNICALL Java_us_altio_gmp4j_BigInteger_natInitializeLibrary
(JNIEnv *env, jclass this)
{
	/*
	 * Setup manual memory management
	 */
	mp_set_memory_functions(NULL, NULL, NULL);
	native_ptr = (*env)->GetFieldID(env, this, "native_ptr", "Lus/altio/gmp4j/Pointer;");
}
/*
 * BI add BI
 */
JNIEXPORT jobject JNICALL Java_us_altio_gmp4j_BigInteger_natMpzAdd(JNIEnv * env,
		jobject this, jobject that1, jobject that2) {
	mpz_ptr new;
	jobject native_ptr_fld;
	new = (mpz_ptr) Common_malloc(env, sizeof(mpz_t));
	mpz_init(new);
	mpz_add(new, (mpz_ptr) Common_GetRawData(env, that1),
			(mpz_ptr) Common_GetRawData(env, that2));
	native_ptr_fld = Common_NewRawDataObject(env, new);
	return native_ptr_fld;
}

/*
 * BI sub BI
 */
JNIEXPORT jobject JNICALL Java_us_altio_gmp4j_BigInteger_natMpzSub(JNIEnv * env,
		jobject this, jobject that1, jobject that2) {
	mpz_ptr new;
	jobject native_ptr_fld;
	new = (mpz_ptr) Common_malloc(env, sizeof(mpz_t));
	mpz_init(new);
	mpz_sub(new, (mpz_ptr) Common_GetRawData(env, that1),
			(mpz_ptr) Common_GetRawData(env, that2));
	native_ptr_fld = Common_NewRawDataObject(env, new);
	return native_ptr_fld;
}
/*
 * BI mul BI
 */
JNIEXPORT jobject JNICALL Java_us_altio_gmp4j_BigInteger_natMpzMul(JNIEnv * env,
		jobject this, jobject that1, jobject that2) {
	mpz_ptr new;
	jobject native_ptr_fld;
	new = (mpz_ptr) Common_malloc(env, sizeof(mpz_t));
	mpz_init(new);
	mpz_mul(new, (mpz_ptr) Common_GetRawData(env, that1),
			(mpz_ptr) Common_GetRawData(env, that2));
	native_ptr_fld = Common_NewRawDataObject(env, new);
	return native_ptr_fld;
}
/*
 * initialize (Pointer)BI with java signed long
 */
JNIEXPORT void JNICALL Java_us_altio_gmp4j_BigInteger_natMpzInitSetSi
(JNIEnv * env, jobject this, jlong val) {
	mpz_ptr new;
	jobject native_ptr_fld;
	new = (mpz_ptr) Common_malloc(env, sizeof(mpz_t));
	mpz_init_set_si(new, val);
	native_ptr_fld = Common_NewRawDataObject(env, new);
	(*env)->SetObjectField(env, this, native_ptr, native_ptr_fld);
}
/*
 * finalizer called from wrapper. clears memory, occupied by GMP object.
 */
JNIEXPORT void JNICALL Java_us_altio_gmp4j_BigInteger_natFinalize
(JNIEnv * env, jobject this)
{
	mpz_ptr ref;

	ref=(mpz_ptr) Common_GetRawData(env, (*env)->GetObjectField(env, this,
					native_ptr));

	if (ref != NULL) {
		mpz_clear(ref);
		Common_free(env,ref);
		ref=NULL;
	}
}
/*
 * compare BI and BI. returns 0 if they are equal, -1 if 1st less than 2nd, 1 if 1st bigger than 2nd.
 */
JNIEXPORT jint JNICALL Java_us_altio_gmp4j_BigInteger_natMpzCmp(JNIEnv * env,
		jobject this, jobject that1, jobject that2) {
	int result;
	result = mpz_cmp((mpz_ptr) Common_GetRawData(env, that1),
			(mpz_ptr) Common_GetRawData(env, that2));
	if (result < 0)
		return ((jint) - 1);
	else if (result == 0)
		return ((jint) 0);
	else
		return ((jint) 1);
}
/*
 * negate BI.
 */
JNIEXPORT jobject JNICALL Java_us_altio_gmp4j_BigInteger_natMpzNeg(JNIEnv * env,
		jobject this, jobject that) {
	mpz_ptr new;
	jobject native_ptr_fld;
	new = (mpz_ptr) Common_malloc(env, sizeof(mpz_t));
	mpz_init(new);
	mpz_neg(new, (mpz_ptr) Common_GetRawData(env, that));
	native_ptr_fld = Common_NewRawDataObject(env, new);
	return native_ptr_fld;
}
/*
 * return absolute value of BI
 */
JNIEXPORT jobject JNICALL Java_us_altio_gmp4j_BigInteger_natMpzAbs(JNIEnv * env,
		jobject this, jobject that) {
	mpz_ptr new;
	jobject native_ptr_fld;
	new = (mpz_ptr) Common_malloc(env, sizeof(mpz_t));
	mpz_init(new);
	mpz_abs(new, (mpz_ptr) Common_GetRawData(env, that));
	native_ptr_fld = Common_NewRawDataObject(env, new);
	return native_ptr_fld;
}
/*
 * Initialize GMP object with zero value. Basically useless, and should be replaced to signed int constructor.
 * TODO: @altmind remove this method in favor of Si initialize
 */
JNIEXPORT void JNICALL Java_us_altio_gmp4j_BigInteger_natInitialize
(JNIEnv *env, jobject this)
{
	mpz_ptr new;
	jobject native_ptr_fld;
	new = (mpz_ptr) Common_malloc(env, sizeof(mpz_t));
	mpz_init(new);
	native_ptr_fld = Common_NewRawDataObject(env, new);
	(*env)->SetObjectField(env, this, native_ptr, native_ptr_fld);
}

/*
 * initialize GMP object from String. accepts radix param. 
 *
 * The base may vary from 2 to 62, or if base is 0, then the leading characters are used: 0x and 0X for hexadecimal, 0b and 0B for binary, 0 for octal, or decimal otherwise.
 * For bases up to 36, case is ignored; upper-case and lower-case letters have the same value.
 * For bases 37 to 62, upper-case letter represent the usual 10..35 while lower-case letter represent 36..61.
 */
JNIEXPORT jint JNICALL Java_us_altio_gmp4j_BigInteger_natMpzSetStr(JNIEnv * env,
		jobject this, jstring str, jint radix) {
	mpz_ptr ref;
	const char *bytes;
	int result;

	ref = (mpz_ptr) Common_GetRawData(env, (*env)->GetObjectField(env, this,
			native_ptr));
	bytes = (*env)->GetStringUTFChars(env, str, NULL);
	result = mpz_set_str(ref, bytes, (int) radix);
	Common_free_cstring(env, str, bytes);
	return result;
}

/* get String representation of GMP Object */
JNIEXPORT jstring JNICALL Java_us_altio_gmp4j_BigInteger_natMpzGetStr(
		JNIEnv * env, jobject this, jint radix) {
	mpz_srcptr ref;
	char *cstr;
	jstring result;

	ref = (mpz_srcptr) Common_GetRawData(env, (*env)->GetObjectField(env, this,
			native_ptr));
	cstr = mpz_get_str(NULL, (int) radix, ref);
	result = (*env)->NewStringUTF(env, cstr);
	Common_free(env, cstr);
	return (result);
}

/*
 * return signed int for passed BI.
 *
 * If op fits into a signed long int return the value of op. Otherwise return the least significant part of op, with the same sign as op.
 */
JNIEXPORT jlong JNICALL Java_us_altio_gmp4j_BigInteger_natGetSi(JNIEnv * env,
		jobject this, jobject that) {
	return (mpz_get_si((mpz_ptr) Common_GetRawData(env, that)));
}

/*
 *  BI pow unsigned int.
 */
JNIEXPORT jobject JNICALL Java_us_altio_gmp4j_BigInteger_natMpzPowUi(
		JNIEnv *env, jobject this, jobject that, jint v) {
	mpz_ptr new;
	jobject native_ptr_fld;
	new = (mpz_ptr) Common_malloc(env, sizeof(mpz_t));
	mpz_init(new);
	mpz_pow_ui(new, (mpz_ptr) Common_GetRawData(env, that),
			(unsigned int)v);
	native_ptr_fld = Common_NewRawDataObject(env, new);
	return native_ptr_fld;
}

/*
 * truncate division: quotent part as BI.
 */
JNIEXPORT jobject JNICALL Java_us_altio_gmp4j_BigInteger_natMpzTdivQ(
		JNIEnv * env, jobject this, jobject that1, jobject that2) {

	mpz_ptr new;
	jobject native_ptr_fld;
	new = (mpz_ptr) Common_malloc(env, sizeof(mpz_t));
	mpz_init(new);
	mpz_tdiv_q(new, (mpz_ptr) Common_GetRawData(env, that1),
			(mpz_ptr) Common_GetRawData(env, that2));
	native_ptr_fld = Common_NewRawDataObject(env, new);
	return native_ptr_fld;
}

/*
 * truncate division: remainder part as BI.
 */
JNIEXPORT jobject JNICALL Java_us_altio_gmp4j_BigInteger_natMpzTdivR(
		JNIEnv * env, jobject this, jobject that1, jobject that2) {
	mpz_ptr new;
	jobject native_ptr_fld;
	new = (mpz_ptr) Common_malloc(env, sizeof(mpz_t));
	mpz_init(new);
	mpz_tdiv_r(new, (mpz_ptr) Common_GetRawData(env, that1),
			(mpz_ptr) Common_GetRawData(env, that2));
	native_ptr_fld = Common_NewRawDataObject(env, new);
	return native_ptr_fld;
}

/*
 * is that1 divisable by that2
 */
JNIEXPORT jboolean JNICALL Java_us_altio_gmp4j_BigInteger_natMpzDivisible(
		JNIEnv * env, jobject this, jobject that1, jobject that2) {
	int result;
	result = mpz_divisible_p((mpz_ptr) Common_GetRawData(env, that1),
			(mpz_ptr) Common_GetRawData(env, that2));
	if (result != 0)
		return (jboolean) 1;
	else
		return (jboolean) 0;
}

/*
 * truncate division: quotent and remainder parts as Pointer[2] respectively.
 */
JNIEXPORT jobjectArray JNICALL Java_us_altio_gmp4j_BigInteger_natMpzTdivQR(
		JNIEnv * env, jobject this, jobject that1, jobject that2) {
	mpz_ptr newq,newr;
	jobject native_ptr_fldq, native_ptr_fldr;
	jclass bigIntegerClass;
	jobjectArray result;
	newq = (mpz_ptr) Common_malloc(env, sizeof(mpz_t));
	newr = (mpz_ptr) Common_malloc(env, sizeof(mpz_t));
	mpz_init(newq);
	mpz_init(newr);

	mpz_tdiv_qr(newq, newr, (mpz_ptr) Common_GetRawData(env, that1),
			(mpz_ptr) Common_GetRawData(env, that2));

	native_ptr_fldq = Common_NewRawDataObject(env, newq);
	native_ptr_fldr = Common_NewRawDataObject(env, newr);
	bigIntegerClass = (*env)->FindClass(env, "us/altio/gmp4j/Pointer");
	if (bigIntegerClass == NULL) {
		mpz_clear(newq);
		Common_free(env, newq);
		newq=NULL;
		mpz_clear(newr);
		Common_free(env, newr);
		newr=NULL;
		Common_ThrowException (env, "java/lang/ClassNotFoundException", "Cannot find Pointer interface.");
		return NULL;
	}
	result = (*env)->NewObjectArray(env, 2, bigIntegerClass, NULL);
	if (result == NULL) {
		mpz_clear(newq);
		Common_free(env, newq);
		newq=NULL;
		mpz_clear(newr);
		Common_free(env, newr);
		newr=NULL;
		Common_ThrowException (env, "java/lang/Exception", "Failed to create array.");
		return NULL;
	}
	(*env)->SetObjectArrayElement(env, result, 0, native_ptr_fldq);
	(*env)->SetObjectArrayElement(env, result, 1, native_ptr_fldr);
	return result;
}
/*
 * BI modulo BI.
 */
JNIEXPORT jobject JNICALL Java_us_altio_gmp4j_BigInteger_natMpzMod(JNIEnv * env,
		jobject this, jobject that1, jobject that2) {
	mpz_ptr new;
	jobject native_ptr_fld;
	new = (mpz_ptr) Common_malloc(env, sizeof(mpz_t));
	mpz_init(new);
	mpz_mod(new, (mpz_ptr) Common_GetRawData(env, that1),
			(mpz_ptr) Common_GetRawData(env, that2));
	native_ptr_fld = Common_NewRawDataObject(env, new);
	return native_ptr_fld;
}
/*
 * BI pow BI modulo BI.
 *
 * NEGATIVE exp CAN CAUSE DIVISION BY ZERO AND JVM CRASH. NEGATIVE exp ARE FORBIDDEN AND WILL RESULT IN NULL POINTER EXCEPTION IN BI CONSTRUCTOR.
 * TODO: @altmind add DIVISION BY ZERO handling code and lift this limitiation.
 * TODO: @altmind dig into GMP documentation: what kind of DBZ exception is thrown: SIGFPE, C++ DivideByZero exception(we don't use C++ feats), other platform dependend?
 *
 * Negative exp is supported if an inverse base^-1 mod mod exists (see mpz_invert). If an inverse doesn't exist then a divide by zero is raised.
 */
JNIEXPORT jobject JNICALL
Java_us_altio_gmp4j_BigInteger_natMpzPowm(JNIEnv * env, jobject this,
		jobject that1, jobject that2, jobject that3) {
	mpz_ptr new;
	jobject native_ptr_fld;

	if (mpz_sgn((mpz_ptr) Common_GetRawData(env, that2))<0)
	{
		Common_ThrowException (env, "java/lang/ArithmeticException", "Negative values for power are not supported yet.");
	       	return NULL;
	}

	new = (mpz_ptr) Common_malloc(env, sizeof(mpz_t));
	mpz_init(new);
	mpz_powm(new, (mpz_ptr) Common_GetRawData(env, that1),
			(mpz_ptr) Common_GetRawData(env, that2), (mpz_ptr) Common_GetRawData(env,
					that3));
	native_ptr_fld = Common_NewRawDataObject(env, new);
	return native_ptr_fld;
}
/*
 * BI modulo-inverse BI.
 */
JNIEXPORT jobject JNICALL Java_us_altio_gmp4j_BigInteger_natMpzInvert(
		JNIEnv * env, jobject this, jobject that1, jobject that2) {
	mpz_ptr new;
	jobject native_ptr_fld;
	int result;
	new = (mpz_ptr) Common_malloc(env, sizeof(mpz_t));
	mpz_init(new);
	result = mpz_invert(new, (mpz_ptr) Common_GetRawData(env, that1),
			(mpz_ptr) Common_GetRawData(env, that2));
	if (result==0)
	{
		mpz_clear(new);
		Common_free(env, new);
		new=NULL;
		Common_ThrowException (env, "java/lang/ArithmeticException", "Cannot find inverse for given value.");
		return NULL;
	}
	native_ptr_fld = Common_NewRawDataObject(env, new);
	return native_ptr_fld;
}
/*
 * sqrt(BI) + remainder in BI[2] respectively.
 */
JNIEXPORT jobjectArray JNICALL Java_us_altio_gmp4j_BigInteger_natMpzSqrtRem(
		JNIEnv * env, jobject this, jobject that) {
	mpz_ptr newq, newr;
	jobject native_ptr_fldr,native_ptr_fldq;
	jclass bigIntegerClass;
	jobjectArray result;

	newq = (mpz_ptr) Common_malloc(env, sizeof(mpz_t));
	mpz_init(newq);
	newr = (mpz_ptr) Common_malloc(env, sizeof(mpz_t));
	mpz_init(newr);
	mpz_sqrtrem(newq, newr, (mpz_ptr) Common_GetRawData(env, that));

	native_ptr_fldq = Common_NewRawDataObject(env, newq);
	native_ptr_fldr = Common_NewRawDataObject(env, newr);
	bigIntegerClass = (*env)->FindClass(env, "us/altio/gmp4j/Pointer");
	if (bigIntegerClass == NULL) {
		mpz_clear(newq);
		Common_free(env, newq);
		newq=NULL;
		mpz_clear(newr);
		Common_free(env, newr);
		newr=NULL;
		Common_ThrowException (env, "java/lang/ClassNotFoundException", "Cannot find Pointer interface.");
		return NULL;
	}
	result = (*env)->NewObjectArray(env, 2, bigIntegerClass, NULL);
	if (result == NULL) {
		mpz_clear(newq);
		Common_free(env, newq);
		newq=NULL;
		mpz_clear(newr);
		Common_free(env, newr);
		newr=NULL;
		Common_ThrowException (env, "java/lang/Exception", "Failed to create array.");
		return NULL;
	}
	(*env)->SetObjectArrayElement(env, result, 0, native_ptr_fldq);
	(*env)->SetObjectArrayElement(env, result, 1, native_ptr_fldr);
	return result;
}
/*
 * truncated sqrt of BI as BI.
 */
JNIEXPORT jobject JNICALL Java_us_altio_gmp4j_BigInteger_natMpzSqrt(JNIEnv *env,
		jobject this, jobject that) {
	mpz_ptr new;
	jobject native_ptr_fld;
	new = (mpz_ptr) Common_malloc(env, sizeof(mpz_t));
	mpz_init(new);
	mpz_sqrt(new, (mpz_ptr) Common_GetRawData(env, that));
	native_ptr_fld = Common_NewRawDataObject(env, new);
	return native_ptr_fld;
}
/*
 * whether given value is perfect square or in other words, if sqrt provides no remainder
 */
JNIEXPORT jboolean JNICALL Java_us_altio_gmp4j_BigInteger_natMpzPerfectSquare(
		JNIEnv *env, jobject this, jobject that) {
	int result;
	result = mpz_perfect_square_p((mpz_ptr) Common_GetRawData(env, that));
	if (result != 0)
		return (jboolean) 1;
	else
		return (jboolean) 0;
}
/*
 * return pow-root and remainder AS BI[2] respectively.
 */
JNIEXPORT jobjectArray JNICALL Java_us_altio_gmp4j_BigInteger_natMpzRootRem(
		JNIEnv *env, jobject this, jobject that, jlong pow) {
	mpz_ptr newq, newr;
	jobject native_ptr_fldr,native_ptr_fldq;
	jclass bigIntegerClass;
	jobjectArray result;

	newq = (mpz_ptr) Common_malloc(env, sizeof(mpz_t));
	mpz_init(newq);
	newr = (mpz_ptr) Common_malloc(env, sizeof(mpz_t));
	mpz_init(newr);
	mpz_rootrem(newq, newr, (mpz_ptr) Common_GetRawData(env, that),
			(unsigned long) pow);

	native_ptr_fldq = Common_NewRawDataObject(env, newq);
	native_ptr_fldr = Common_NewRawDataObject(env, newr);
	bigIntegerClass = (*env)->FindClass(env, "us/altio/gmp4j/Pointer");
	if (bigIntegerClass == NULL) {
		mpz_clear(newq);
		Common_free(env, newq);
		newq=NULL;
		mpz_clear(newr);
		Common_free(env, newr);
		newr=NULL;
		Common_ThrowException (env, "java/lang/ClassNotFoundException", "Cannot find Pointer interface.");
		return NULL;
	}
	result = (*env)->NewObjectArray(env, 2, bigIntegerClass, NULL);
	if (result == NULL) {
		mpz_clear(newq);
		Common_free(env, newq);
		newq=NULL;
		mpz_clear(newr);
		Common_free(env, newr);
		newr=NULL;
		Common_ThrowException (env, "java/lang/Exception", "Failed to create array.");
		return NULL;
	}
	(*env)->SetObjectArrayElement(env, result, 0, native_ptr_fldq);
	(*env)->SetObjectArrayElement(env, result, 1, native_ptr_fldr);
	return result;

}

/*
 * return pow-root of BI
 */
JNIEXPORT jobject JNICALL Java_us_altio_gmp4j_BigInteger_natMpzRoot(JNIEnv *env,
		jobject this, jobject that, jlong pow) {
	mpz_ptr new;
	jobject native_ptr_fld;
	new = (mpz_ptr) Common_malloc(env, sizeof(mpz_t));
	mpz_init(new);
	mpz_root(new, (mpz_ptr) Common_GetRawData(env, that), (unsigned long) pow);
	native_ptr_fld = Common_NewRawDataObject(env, new);
	return native_ptr_fld;
}

/*
 * GCD of 2 BIs
 */
JNIEXPORT jobject JNICALL Java_us_altio_gmp4j_BigInteger_natMpzGcd(JNIEnv *env,
		jclass this, jobject that1, jobject that2) {
	mpz_ptr new;
	jobject native_ptr_fld;
	new = (mpz_ptr) Common_malloc(env, sizeof(mpz_t));
	mpz_init(new);
	mpz_gcd(new, (mpz_ptr) Common_GetRawData(env, that1),
			(mpz_ptr) Common_GetRawData(env, that2));
	native_ptr_fld = Common_NewRawDataObject(env, new);
	return native_ptr_fld;
}

/*
 * LCM of 2 BIs
 */
JNIEXPORT jobject JNICALL Java_us_altio_gmp4j_BigInteger_natMpzLcm(JNIEnv *env,
		jclass this, jobject that1, jobject that2) {
	mpz_ptr new;
	jobject native_ptr_fld;
	new = (mpz_ptr) Common_malloc(env, sizeof(mpz_t));
	mpz_init(new);
	mpz_lcm(new, (mpz_ptr) Common_GetRawData(env, that1),
			(mpz_ptr) Common_GetRawData(env, that2));
	native_ptr_fld = Common_NewRawDataObject(env, new);
	return native_ptr_fld;
}
/*
 * Factorial n
 */
JNIEXPORT jobject JNICALL Java_us_altio_gmp4j_BigInteger_natMpzFac(JNIEnv *env,
		jclass this, jlong n) {
	mpz_ptr new;
	jobject native_ptr_fld;
	new = (mpz_ptr) Common_malloc(env, sizeof(mpz_t));
	mpz_init(new);
	mpz_fac_ui(new, (unsigned long) n);
	native_ptr_fld = Common_NewRawDataObject(env, new);
	return native_ptr_fld;
}
/*
 * Binary coefficient(BI,n).
 */
JNIEXPORT jobject JNICALL Java_us_altio_gmp4j_BigInteger_natMpzBin(JNIEnv *env,
		jclass this, jobject that, jlong n) {
	mpz_ptr new;
	jobject native_ptr_fld;
	new = (mpz_ptr) Common_malloc(env, sizeof(mpz_t));
	mpz_init(new);
	mpz_bin_ui(new, (mpz_ptr) Common_GetRawData(env, that), (unsigned long) n);
	native_ptr_fld = Common_NewRawDataObject(env, new);
	return native_ptr_fld;
}

/*
 * Fibonacci n
 */
JNIEXPORT jobject JNICALL Java_us_altio_gmp4j_BigInteger_natMpzFib(JNIEnv *env,
		jclass this, jlong n) {
	mpz_ptr new;
	jobject native_ptr_fld;
	new = (mpz_ptr) Common_malloc(env, sizeof(mpz_t));
	mpz_init(new);
	mpz_fib_ui(new, (unsigned long) n);
	native_ptr_fld = Common_NewRawDataObject(env, new);
	return native_ptr_fld;
}

/*
 * return Fibonacci for n and Fibonacci for n-1 as BI[2] respectively.
 */
JNIEXPORT jobjectArray JNICALL Java_us_altio_gmp4j_BigInteger_natMpzFib2(
		JNIEnv *env, jclass this, jlong n) {
	mpz_ptr r1,r2;
	jobject native_ptr_r1,native_ptr_r2;
	jclass bigIntegerClass;
	jobjectArray result;

	r1 = (mpz_ptr) Common_malloc(env, sizeof(mpz_t));
	mpz_init(r1);
	r2 = (mpz_ptr) Common_malloc(env, sizeof(mpz_t));
	mpz_init(r2);
	mpz_fib2_ui(r1, r2, (unsigned long) n);

	native_ptr_r1 = Common_NewRawDataObject(env, r1);
	native_ptr_r2 = Common_NewRawDataObject(env, r2);
	bigIntegerClass = (*env)->FindClass(env, "us/altio/gmp4j/Pointer");
	if (bigIntegerClass == NULL) {
		mpz_clear(r1);
		Common_free(env, r1);
		r1=NULL;
		mpz_clear(r2);
		Common_free(env, r2);
		r2=NULL;
		Common_ThrowException (env, "java/lang/ClassNotFoundException", "Cannot find Pointer interface.");
		return NULL;
	}
	result = (*env)->NewObjectArray(env, 2, bigIntegerClass, NULL);
	if (result == NULL) {
		mpz_clear(r1);
		Common_free(env, r1);
		r1=NULL;
		mpz_clear(r2);
		Common_free(env, r2);
		r2=NULL;
		Common_ThrowException (env, "java/lang/Exception", "Failed to create array.");
		return NULL;
	}
	(*env)->SetObjectArrayElement(env, result, 0, native_ptr_r1);
	(*env)->SetObjectArrayElement(env, result, 1, native_ptr_r2);
	return result;
}

/*
 * BI SHL n
 */
JNIEXPORT jobject JNICALL Java_us_altio_gmp4j_BigInteger_natMyMpzShl(
		JNIEnv *env, jobject this, jobject that, jlong n) {
	mpz_ptr new;
	jobject native_ptr_fld;
	new = (mpz_ptr) Common_malloc(env, sizeof(mpz_t));
	mpz_init(new);
	mpz_mul_2exp(new, (mpz_ptr) Common_GetRawData(env, that), (unsigned long) n);
	native_ptr_fld = Common_NewRawDataObject(env, new);
	return native_ptr_fld;
}

/*
 * BI SHR n
 */
JNIEXPORT jobject JNICALL Java_us_altio_gmp4j_BigInteger_natMyMpzShr(
		JNIEnv *env, jobject this, jobject that, jlong n) {
	mpz_ptr new;
	jobject native_ptr_fld;
	new = (mpz_ptr) Common_malloc(env, sizeof(mpz_t));
	mpz_init(new);
	mpz_fdiv_q_2exp(new, (mpz_ptr) Common_GetRawData(env, that), (unsigned long) n);
	native_ptr_fld = Common_NewRawDataObject(env, new);
	return native_ptr_fld;
}
/*
 * BI And BI
 */
JNIEXPORT jobject JNICALL Java_us_altio_gmp4j_BigInteger_natMpzAnd(JNIEnv *env,
		jobject this, jobject that1, jobject that2) {
	mpz_ptr new;
	jobject native_ptr_fld;
	new = (mpz_ptr) Common_malloc(env, sizeof(mpz_t));
	mpz_init(new);
	mpz_and(new, (mpz_ptr) Common_GetRawData(env, that1),
			(mpz_ptr) Common_GetRawData(env, that2));
	native_ptr_fld = Common_NewRawDataObject(env, new);
	return native_ptr_fld;
}

/*
 * BI or BI
 */
JNIEXPORT jobject JNICALL Java_us_altio_gmp4j_BigInteger_natMpzOr(JNIEnv *env,
		jobject this, jobject that1, jobject that2) {
	mpz_ptr new;
	jobject native_ptr_fld;
	new = (mpz_ptr) Common_malloc(env, sizeof(mpz_t));
	mpz_init(new);
	mpz_ior(new, (mpz_ptr) Common_GetRawData(env, that1),
			(mpz_ptr) Common_GetRawData(env, that2));
	native_ptr_fld = Common_NewRawDataObject(env, new);
	return native_ptr_fld;
}
/*
 * BI xor BI
 */
JNIEXPORT jobject JNICALL Java_us_altio_gmp4j_BigInteger_natMpzXor(JNIEnv *env,
		jobject this, jobject that1, jobject that2) {
	mpz_ptr new;
	jobject native_ptr_fld;
	new = (mpz_ptr) Common_malloc(env, sizeof(mpz_t));
	mpz_init(new);
	mpz_xor(new, (mpz_ptr) Common_GetRawData(env, that1),
			(mpz_ptr) Common_GetRawData(env, that2));
	native_ptr_fld = Common_NewRawDataObject(env, new);
	return native_ptr_fld;
}
/*
 * not BI
 */
JNIEXPORT jobject JNICALL Java_us_altio_gmp4j_BigInteger_natMpzNot(JNIEnv *env,
		jobject this, jobject that) {
	mpz_ptr new;
	jobject native_ptr_fld;
	new = (mpz_ptr) Common_malloc(env, sizeof(mpz_t));
	mpz_init(new);
	mpz_com(new, (mpz_ptr) Common_GetRawData(env, that));
	native_ptr_fld = Common_NewRawDataObject(env, new);
	return native_ptr_fld;
}
/*
 * Test wether n bit in BI is set
 */
JNIEXPORT jint JNICALL Java_us_altio_gmp4j_BigInteger_natMpzTstBit(JNIEnv *env,
		jobject this, jobject that, jlong n) {
	int result;
	result = mpz_tstbit((mpz_ptr) Common_GetRawData(env, that), (unsigned long) n);
	return ((jint)result);
}
/*
 * Set n bit in BI
 */
JNIEXPORT jobject JNICALL Java_us_altio_gmp4j_BigInteger_natMpzSetBit(
		JNIEnv *env, jobject this, jobject that, jlong n) {
	mpz_ptr new;
	jobject native_ptr_fld;
	new = (mpz_ptr) Common_malloc(env, sizeof(mpz_t));
	mpz_init(new);
	mpz_init_set(new, (mpz_ptr) Common_GetRawData(env, that));
	mpz_setbit(new, (unsigned long) n);
	native_ptr_fld = Common_NewRawDataObject(env, new);
	return native_ptr_fld;
}
/*
 * Clear n bit in BI
 */
JNIEXPORT jobject JNICALL Java_us_altio_gmp4j_BigInteger_natMpzClrBit(
		JNIEnv *env, jobject this, jobject that, jlong n) {
	mpz_ptr new;
	jobject native_ptr_fld;
	new = (mpz_ptr) Common_malloc(env, sizeof(mpz_t));
	mpz_init(new);
	mpz_init_set(new, (mpz_ptr) Common_GetRawData(env, that));
	mpz_clrbit(new, (unsigned long) n);
	native_ptr_fld = Common_NewRawDataObject(env, new);
	return native_ptr_fld;
}
/*
 * Flip n bit in BI
 */
JNIEXPORT jobject JNICALL Java_us_altio_gmp4j_BigInteger_natMpzFlipBit(
		JNIEnv *env, jobject this, jobject that, jlong n) {
	mpz_ptr new;
	jobject native_ptr_fld;
	new = (mpz_ptr) Common_malloc(env, sizeof(mpz_t));
	mpz_init(new);
	mpz_init_set(new, (mpz_ptr) Common_GetRawData(env, that));
	mpz_combit(new, (unsigned long) n);
	native_ptr_fld = Common_NewRawDataObject(env, new);
	return native_ptr_fld;
}
/*
 * Size of BI as power of 2
 */
JNIEXPORT jlong JNICALL Java_us_altio_gmp4j_BigInteger_natMpzSizeInBase2(
		JNIEnv * env, jobject this, jobject that) {
	return (signed long) mpz_sizeinbase((mpz_ptr) Common_GetRawData(env, that), 2);
}

/*
 * Static method to create random state
 */
JNIEXPORT jobject JNICALL Java_us_altio_gmp4j_BigInteger_natMpzRandinit
  (JNIEnv * env, jclass this, jlong stateInit)
{
	//stateInit is not used
	gmp_randstate_t* new;
	jobject native_ptr_fld;
	new = (gmp_randstate_t*) Common_malloc(env, sizeof(gmp_randstate_t));
	gmp_randinit_default(*new);
	native_ptr_fld = Common_NewRawDataObject(env, new);
	return native_ptr_fld;
}
/*
 * return if numer is probably prime.
 * certainty eps controls how many such tests are done, 5 to 10 is a reasonable number, more will reduce the chances of a composite being returned as “probably prime”.
 *
 * Return 2 if n is definitely prime, return 1 if n is probably prime (without being certain), or return 0 if n is definitely composite.
*/
JNIEXPORT jint JNICALL Java_us_altio_gmp4j_BigInteger_natMpzProbabPrime
  (JNIEnv * env, jobject this, jobject that, jint certainty)
{
	int result;
	result = mpz_probab_prime_p((mpz_ptr) Common_GetRawData(env, that),certainty);
	return ((jint)result);
}
/*
 * return next BI for given
 */
JNIEXPORT jobject JNICALL Java_us_altio_gmp4j_BigInteger_natMpzNextprime
  (JNIEnv * env, jobject this, jobject that)
{
	mpz_ptr new;
	jobject native_ptr_fld;
	new = (mpz_ptr) Common_malloc(env, sizeof(mpz_t));
	mpz_init(new);
	mpz_nextprime(new, (mpz_ptr) Common_GetRawData(env, that));
	native_ptr_fld = Common_NewRawDataObject(env, new);
	return native_ptr_fld;
}
/*
 * get random number between 0 and max
 */
JNIEXPORT jobject JNICALL Java_us_altio_gmp4j_BigInteger_natMpzUrandomm
  (JNIEnv * env, jclass this, jobject prngstate, jobject max)
{
	mpz_ptr new;
	jobject native_ptr_fld;
	new = (mpz_ptr) Common_malloc(env, sizeof(mpz_t));
	mpz_init(new);
	mpz_urandomm(new, *((gmp_randstate_t*)Common_GetRawData(env, prngstate)), (mpz_ptr) Common_GetRawData(env, max));
	native_ptr_fld = Common_NewRawDataObject(env, new);
	return native_ptr_fld;
}
/*
 * get random number between 0 and (2^n)-1
 */
JNIEXPORT jobject JNICALL Java_us_altio_gmp4j_BigInteger_natMpzUrandomb
  (JNIEnv * env , jclass this, jobject prngstate, jint bitLength)
{
	mpz_ptr new;
	jobject native_ptr_fld;
	new = (mpz_ptr) Common_malloc(env, sizeof(mpz_t));
	mpz_init(new);
	mpz_urandomb(new, *((gmp_randstate_t*) Common_GetRawData(env, prngstate)), bitLength);
	native_ptr_fld = Common_NewRawDataObject(env, new);
	return native_ptr_fld;
}
/*
 * find first set bit in BI. for negative number result is - unlimited
 * TODO: check if off-by-one error present
 */
JNIEXPORT jlong JNICALL Java_us_altio_gmp4j_BigInteger_natMpzScan1
  (JNIEnv * env, jobject this, jobject that)
{
	return ((jlong)mpz_scan1((mpz_ptr) Common_GetRawData(env, that),0));
}
/*
 * set number of bits in BI
 */
JNIEXPORT jlong JNICALL Java_us_altio_gmp4j_BigInteger_natMpzPopcount
  (JNIEnv * env, jobject this, jobject that)
{
	return ((jlong)mpz_popcount((mpz_ptr) Common_GetRawData(env, that)));
}

/*
 * signum
 */
JNIEXPORT jint JNICALL Java_us_altio_gmp4j_BigInteger_natMpzSgn
  (JNIEnv * env, jobject this, jobject that)
{
	return ((jint)mpz_sgn((mpz_ptr)Common_GetRawData(env, that)));
}
/*
 * free PRNG state. called only when globally no BI instances exist.
 */
JNIEXPORT void JNICALL Java_us_altio_gmp4j_BigInteger_natMyFreePrngState
  (JNIEnv * env, jclass this, jobject prngState)
{
	gmp_randclear(Common_GetRawData(env, prngState));
}

#ifdef __cplusplus
}
#endif

