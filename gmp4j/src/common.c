#include <stdlib.h>
#include <stdio.h>
#include "common.h"

/*
 * Define bitness of underlying platform: supported either 4 or 8
 */
static int SIZEOF_VOID_P() { void* p; return sizeof(p);}

/*
 * Cached Pointer class info.
 */
// class reference to either Pointer32 or Pointer64
static jclass rawDataClass = NULL;
// field reference to pointer
static jfieldID rawData_fid = NULL;
// method reference to constructor
static jmethodID rawData_mid = NULL;

/*
 * Global initialization
 */
JNIEXPORT jint JNICALL JNI_OnLoad (JavaVM *vm, void *reserved)
{
	JNIEnv *env;
	void *envp;

	if ((*vm)->GetEnv (vm, &envp, JNI_VERSION_1_4) != JNI_OK)
	{
		return JNI_VERSION_1_4;
	}
	env = (JNIEnv *) envp;
	/*
	* Initialize fields, methods, class references
	*/
	if (SIZEOF_VOID_P() == 8)
	{
		rawDataClass = (*env)->FindClass (env, "us/altio/gmp4j/Pointer64");
		if (rawDataClass != NULL)
			rawDataClass = (*env)->NewGlobalRef (env, rawDataClass);

		if (rawDataClass != NULL)
		{
		rawData_fid = (*env)->GetFieldID (env, rawDataClass, "data", "J");
		rawData_mid = (*env)->GetMethodID (env, rawDataClass, "<init>", "(J)V");
		}
	}
	else if (SIZEOF_VOID_P() == 4)
	{
		rawDataClass = (*env)->FindClass (env, "us/altio/gmp4j/Pointer32");
		if (rawDataClass != NULL)
		rawDataClass = (*env)->NewGlobalRef (env, rawDataClass);

		if (rawDataClass != NULL)
		{
			rawData_fid = (*env)->GetFieldID (env, rawDataClass, "data", "I");
			rawData_mid = (*env)->GetMethodID (env, rawDataClass, "<init>", "(I)V");
		}
	}
	return JNI_VERSION_1_4;
}


JNIEXPORT void JNICALL Common_ThrowException (JNIEnv * env, const char *className, const char *errMsg)
{
	jclass excClass;
	if ((*env)->ExceptionOccurred (env))
	{
		(*env)->ExceptionClear (env);
	}
	excClass = (*env)->FindClass (env, className);
	if (excClass == NULL)
	{
		jclass errExcClass;
		errExcClass =(*env)->FindClass (env, "java/lang/ClassNotFoundException");
		if (errExcClass == NULL)
		{
			errExcClass = (*env)->FindClass (env, "java/lang/InternalError");
			if (errExcClass == NULL)
			{
				// despaired, theres nothing we can do.
				return;
			}
		}
		(*env)->ThrowNew (env, errExcClass, className);
	}
	(*env)->ThrowNew (env, excClass, errMsg);
}
/*
 * malloc with np checking
 */
JNIEXPORT void * JNICALL Common_malloc (JNIEnv * env, size_t size)
{
	void *mem = malloc (size);
	if (mem == NULL)
	{
		Common_ThrowException (env, "java/lang/OutOfMemoryError", "malloc() failed.");
		return NULL;
	}
	return mem;
}

/*
 * Free string
 */
JNIEXPORT void JNICALL Common_free_cstring (JNIEnv * env, jstring s, const char *cstr)
{
	(*env)->ReleaseStringUTFChars (env, s, cstr);
}

/*
 * realloc with np checking
 */
JNIEXPORT void * JNICALL Common_realloc (JNIEnv * env, void *ptr, size_t size)
{
	void *orig_ptr = ptr;
	ptr = realloc (ptr, size);
	if (ptr == 0)
	{
		free (orig_ptr);
		Common_ThrowException (env, "java/lang/OutOfMemoryError", "realloc() failed.");
	return NULL;
	}
	return (ptr);
}
/*
 * free with np checking
 */
JNIEXPORT void JNICALL Common_free (JNIEnv * env, void *p)
{
	if (p != NULL)
	{
		free (p);
	}
}

/*
 * FindClass with exception handling
 */
JNIEXPORT jclass JNICALL Common_FindClass (JNIEnv * env, const char *className)
{
	jclass retval = (*env)->FindClass (env, className);
	if (retval == NULL)
	{
		Common_ThrowException (env, "java/lang/ClassNotFoundException", className);
	}
	return retval;
}


/*
 * build a Pointer object.
 */
JNIEXPORT jobject JNICALL Common_NewRawDataObject (JNIEnv * env, void *data)
{
	if (rawDataClass == NULL || rawData_mid == NULL)
	{
		Common_ThrowException (env, "java/lang/InternalError", "Pointer class was not properly initialized");
		return NULL;
	}
	if (SIZEOF_VOID_P() == 8) {
		return (*env)->NewObject (env, rawDataClass, rawData_mid, (jlong) data);
	}
	else
	{
		return (*env)->NewObject (env, rawDataClass, rawData_mid, (jint) data);
	}
}

/*
 * return native_ptr from BI
 */
JNIEXPORT void * JNICALL Common_GetRawData (JNIEnv * env, jobject rawdata)
{
	if (rawData_fid == NULL)
	{
		Common_ThrowException (env, "java/lang/InternalError", "Pointer class was not properly initialized");
		return NULL;
	}

	if (SIZEOF_VOID_P() == 8)
	{
		return (void *) (*env)->GetLongField (env, rawdata, rawData_fid);
	}
	else
	{
		return (void *) (*env)->GetIntField (env, rawdata, rawData_fid);
	}
}
