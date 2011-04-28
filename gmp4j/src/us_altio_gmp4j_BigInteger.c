#include <jni.h>
#include "common.h"
#include <gmp.h>
#include <assert.h>
#include <stdlib.h>
#include <stdio.h>
#include "../bin/us_altio_gmp4j_BigInteger.h"

#ifdef __cplusplus
extern "C" {
#endif

static jfieldID native_ptr;

JNIEXPORT void JNICALL Java_us_altio_gmp4j_BigInteger_natInitializeLibrary
(JNIEnv *env, jclass this)
{
	mp_set_memory_functions(NULL, NULL, NULL);
	native_ptr = (*env)->GetFieldID(env, this, "native_ptr", "Lus/altio/gmp4j/Pointer;");
}

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

JNIEXPORT void JNICALL Java_us_altio_gmp4j_BigInteger_natMpzInitSetSi
(JNIEnv * env, jobject this, jlong val) {
	mpz_ptr new;
	jobject native_ptr_fld;
	new = (mpz_ptr) Common_malloc(env, sizeof(mpz_t));
	mpz_init_set_si(new, val);
	native_ptr_fld = Common_NewRawDataObject(env, new);
	(*env)->SetObjectField(env, this, native_ptr, native_ptr_fld);
}

JNIEXPORT void JNICALL Java_us_altio_gmp4j_BigInteger_natFinalize
(JNIEnv * env, jobject this)
{
	mpz_ptr ref;

	ref=(mpz_ptr) Common_GetRawData(env, (*env)->GetObjectField(env, this,
					native_ptr));

	if (ref != NULL) {
		mpz_clear(ref);
		free(ref);
		ref=NULL;
	}
}

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

JNIEXPORT jstring JNICALL Java_us_altio_gmp4j_BigInteger_natMpzGetStr(
		JNIEnv * env, jobject this, jint radix) {
	mpz_srcptr ref;
	char *cstr;
	jstring result;

	ref = (mpz_srcptr) Common_GetRawData(env, (*env)->GetObjectField(env, this,
			native_ptr));
	cstr = mpz_get_str(NULL, (int) radix, ref);
	result = (*env)->NewStringUTF(env, cstr);
	free(cstr);
	return (result);
}


JNIEXPORT jlong JNICALL Java_us_altio_gmp4j_BigInteger_natGetSi(JNIEnv * env,
		jobject this, jobject that) {
	return (mpz_get_si((mpz_ptr) Common_GetRawData(env, that)));
}

JNIEXPORT jobject JNICALL Java_us_altio_gmp4j_BigInteger_natMpzPowUi(
		JNIEnv *env, jobject this, jobject that, jlong v) {
	mpz_ptr new;
	jobject native_ptr_fld;
	new = (mpz_ptr) Common_malloc(env, sizeof(mpz_t));
	mpz_init(new);
	mpz_pow_ui(new, (mpz_ptr) Common_GetRawData(env, that),
			(unsigned int)v);
	native_ptr_fld = Common_NewRawDataObject(env, new);
	return native_ptr_fld;
}


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
	bigIntegerClass = (*env)->FindClass(env, "Lus/altio/gmp4j/Pointer;");
	if (bigIntegerClass == NULL) {
		return NULL;
	}
	result = (*env)->NewObjectArray(env, 2, bigIntegerClass, NULL);
	if (result == NULL) {
		return NULL;
	}
	(*env)->SetObjectArrayElement(env, result, 0, native_ptr_fldq);
	(*env)->SetObjectArrayElement(env, result, 1, native_ptr_fldr);
	return result;
}

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

JNIEXPORT jobject JNICALL
Java_us_altio_gmp4j_BigInteger_natMpzPowm(JNIEnv * env, jobject this,
		jobject that1, jobject that2, jobject that3) {
	mpz_ptr new;
	jobject native_ptr_fld;
	new = (mpz_ptr) Common_malloc(env, sizeof(mpz_t));
	mpz_init(new);
	mpz_powm(new, (mpz_ptr) Common_GetRawData(env, that1),
			(mpz_ptr) Common_GetRawData(env, that2), (mpz_ptr) Common_GetRawData(env,
					that3));
	native_ptr_fld = Common_NewRawDataObject(env, new);
	return native_ptr_fld;
}

JNIEXPORT jobject JNICALL Java_us_altio_gmp4j_BigInteger_natMpzInvert(
		JNIEnv * env, jobject this, jobject that1, jobject that2) {
	mpz_ptr new;
	jobject native_ptr_fld;
	new = (mpz_ptr) Common_malloc(env, sizeof(mpz_t));
	mpz_init(new);
	mpz_invert(new, (mpz_ptr) Common_GetRawData(env, that1),
			(mpz_ptr) Common_GetRawData(env, that2));
	native_ptr_fld = Common_NewRawDataObject(env, new);
	return native_ptr_fld;
}

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
	bigIntegerClass = (*env)->FindClass(env, "Lus/altio/gmp4j/Pointer;");
	if (bigIntegerClass == NULL) {
		return NULL;
	}
	result = (*env)->NewObjectArray(env, 2, bigIntegerClass, NULL);
	if (result == NULL) {
		return NULL;
	}
	(*env)->SetObjectArrayElement(env, result, 0, native_ptr_fldq);
	(*env)->SetObjectArrayElement(env, result, 1, native_ptr_fldr);
	return result;
}

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

JNIEXPORT jboolean JNICALL Java_us_altio_gmp4j_BigInteger_natMpzPerfectSquare(
		JNIEnv *env, jobject this, jobject that) {
	int result;
	result = mpz_perfect_square_p((mpz_ptr) Common_GetRawData(env, that));
	if (result != 0)
		return (jboolean) 1;
	else
		return (jboolean) 0;
}

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
	bigIntegerClass = (*env)->FindClass(env, "Lus/altio/gmp4j/Pointer;");
	if (bigIntegerClass == NULL) {
		return NULL;
	}
	result = (*env)->NewObjectArray(env, 2, bigIntegerClass, NULL);
	if (result == NULL) {
		return NULL;
	}
	(*env)->SetObjectArrayElement(env, result, 0, native_ptr_fldq);
	(*env)->SetObjectArrayElement(env, result, 1, native_ptr_fldr);
	return result;

}

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
	bigIntegerClass = (*env)->FindClass(env, "Lus/altio/gmp4j/Pointer;");
	if (bigIntegerClass == NULL) {
		return NULL;
	}
	result = (*env)->NewObjectArray(env, 2, bigIntegerClass, NULL);
	if (result == NULL) {
		return NULL;
	}
	(*env)->SetObjectArrayElement(env, result, 0, native_ptr_r1);
	(*env)->SetObjectArrayElement(env, result, 1, native_ptr_r2);
	return result;
}

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

JNIEXPORT jint JNICALL Java_us_altio_gmp4j_BigInteger_natMpzTstBit(JNIEnv *env,
		jobject this, jobject that, jlong n) {
	int result;
	result = mpz_tstbit((mpz_ptr) Common_GetRawData(env, that), (unsigned long) n);
	return ((jint)result);
}

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

JNIEXPORT jlong JNICALL Java_us_altio_gmp4j_BigInteger_natMpzSizeInBase2(
		JNIEnv * env, jobject this, jobject that) {
	return (signed long) mpz_sizeinbase((mpz_ptr) Common_GetRawData(env, that), 2);
}

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
Return 2 if n is definitely prime, return 1 if n is probably prime (without being certain), or return 0 if n is definitely composite.
*/
JNIEXPORT jint JNICALL Java_us_altio_gmp4j_BigInteger_natMpzProbabPrime
  (JNIEnv * env, jobject this, jobject that, jint certainty)
{
	int result;
	result = mpz_probab_prime_p((mpz_ptr) Common_GetRawData(env, that),certainty);
	return ((jint)result);
}

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

JNIEXPORT jlong JNICALL Java_us_altio_gmp4j_BigInteger_natMpzScan1
  (JNIEnv * env, jobject this, jobject that)
{
	return ((jlong)mpz_scan1((mpz_ptr) Common_GetRawData(env, that),0));
}

JNIEXPORT jlong JNICALL Java_us_altio_gmp4j_BigInteger_natMpzPopcount
  (JNIEnv * env, jobject this, jobject that)
{
	return ((jlong)mpz_popcount((mpz_ptr) Common_GetRawData(env, that)));
}

JNIEXPORT jint JNICALL Java_us_altio_gmp4j_BigInteger_natMpzSgn
  (JNIEnv * env, jobject this, jobject that)
{
	return ((jint)mpz_sgn((mpz_ptr)Common_GetRawData(env, that)));
}

JNIEXPORT void JNICALL Java_us_altio_gmp4j_BigInteger_natMyFreePrngState
  (JNIEnv * env, jclass this, jobject prngState)
{
	gmp_randclear(Common_GetRawData(env, prngState));
}

#ifdef __cplusplus
}
#endif

