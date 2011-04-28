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
 * JNI OnLoad constructor.
 */
JNIEXPORT jint JNICALL
JNI_OnLoad (JavaVM *vm, void *reserved)
{
  JNIEnv *env;
  void *envp;

  if ((*vm)->GetEnv (vm, &envp, JNI_VERSION_1_4) != JNI_OK)
    {
      return JNI_VERSION_1_4;
    }
  env = (JNIEnv *) envp;
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

  if (SIZEOF_VOID_P() == 4)
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

  //#error "Pointer size is not supported."
  //#endif /* SIZEOF_VOID_P == 4 */
  //#endif /* SIZEOF_VOID_P == 8 */

  return JNI_VERSION_1_4;
}


JNIEXPORT void JNICALL
Common_ThrowException (JNIEnv * env, const char *className, const char *errMsg)
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
      errExcClass =
	(*env)->FindClass (env, "java/lang/ClassNotFoundException");
      if (errExcClass == NULL)
	{
	  errExcClass = (*env)->FindClass (env, "java/lang/InternalError");
	  if (errExcClass == NULL)
	    {
	      fprintf (stderr, "Common: Utterly failed to throw exeption ");
	      fprintf (stderr, "%s", className);
	      fprintf (stderr, " with message ");
	      fprintf (stderr, "%s", errMsg);
	      return;
	    }
	}
      (*env)->ThrowNew (env, errExcClass, className);
    }
  (*env)->ThrowNew (env, excClass, errMsg);
}

JNIEXPORT void *JNICALL
Common_malloc (JNIEnv * env, size_t size)
{
  void *mem = malloc (size);
  if (mem == NULL)
    {
      Common_ThrowException (env, "java/lang/OutOfMemoryError",
			  "malloc() failed.");
      return NULL;
    }
  return mem;
}

JNIEXPORT void *JNICALL
Common_realloc (JNIEnv * env, void *ptr, size_t size)
{
  void *orig_ptr = ptr;
  ptr = realloc (ptr, size);
  if (ptr == 0)
    {
      free (orig_ptr);
      Common_ThrowException (env, "java/lang/OutOfMemoryError",
			  "malloc() failed.");
      return NULL;
    }
  return (ptr);
}

JNIEXPORT void JNICALL
Common_free (JNIEnv * env, void *p)
{
  if (p != NULL)
    {
      free (p);
    }
}

JNIEXPORT const char *JNICALL
Common_jstring_to_cstring (JNIEnv * env, jstring s)
{
  const char *cstr;
  if (s == NULL)
    {
      Common_ThrowException (env, "java/lang/NullPointerException",
			  "Null string");
      return NULL;
    }
  cstr = (const char *) (*env)->GetStringUTFChars (env, s, NULL);
  if (cstr == NULL)
    {
      Common_ThrowException (env, "java/lang/InternalError",
			  "GetStringUTFChars() failed.");
      return NULL;
    }
  return cstr;
}

JNIEXPORT void JNICALL
Common_free_cstring (JNIEnv * env, jstring s, const char *cstr)
{
  (*env)->ReleaseStringUTFChars (env, s, cstr);
}

JNIEXPORT jint JNICALL
Common_MonitorEnter (JNIEnv * env, jobject o)
{
  jint retval = (*env)->MonitorEnter (env, o);
  if (retval != 0)
    {
      Common_ThrowException (env, "java/lang/InternalError",
			  "MonitorEnter() failed.");
    }
  return retval;
}

JNIEXPORT jint JNICALL
Common_MonitorExit (JNIEnv * env, jobject o)
{
  jint retval = (*env)->MonitorExit (env, o);
  if (retval != 0)
    {
      Common_ThrowException (env, "java/lang/InternalError",
			  "MonitorExit() failed.");
    }
  return retval;
}

JNIEXPORT jclass JNICALL
Common_FindClass (JNIEnv * env, const char *className)
{
  jclass retval = (*env)->FindClass (env, className);
  if (retval == NULL)
    {
      Common_ThrowException (env, "java/lang/ClassNotFoundException", className);
    }
  return retval;
}


/*
 * Build a Pointer object.
 */

JNIEXPORT jobject JNICALL
Common_NewRawDataObject (JNIEnv * env, void *data)
{
  if (rawDataClass == NULL || rawData_mid == NULL)
    {
      Common_ThrowException (env, "java/lang/InternalError",
                          "Pointer class was not properly initialized");
      return NULL;
    }

  if (SIZEOF_VOID_P() == 8) {
    return (*env)->NewObject (env, rawDataClass, rawData_mid, (jlong) data);
  }
  else{
    return (*env)->NewObject (env, rawDataClass, rawData_mid, (jint) data);
  }
}

JNIEXPORT void * JNICALL
Common_GetRawData (JNIEnv * env, jobject rawdata)
{
  if (rawData_fid == NULL)
    {
      Common_ThrowException (env, "java/lang/InternalError",
                          "Pointer class was not properly initialized");
      return NULL;
    }

  if (SIZEOF_VOID_P() == 8)
    {
      return (void *) (*env)->GetLongField (env, rawdata, rawData_fid);
    }
  else{
    return (void *) (*env)->GetIntField (env, rawdata, rawData_fid);
  }
}
