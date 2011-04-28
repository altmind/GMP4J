#ifndef __Common_H__
#define __Common_H__

#include <stddef.h>
#include <jni.h>
//#include <config.h>

//int size_void_p() { void* p; return sizeof(p);}

//static int SIZEOF_VOID_P = sizeof(void* p);
   
/* #if SIZEOF_VOID_P == 4 */
/* typedef jint jpointer; */
/* #elif SIZEOF_VOID_P == 8 */
/* typedef jlong jpointer; */
/* #else */
/* #error "Unknown pointer size" */
/* #endif */

/* Helper macros for going between pointers and jlongs.  */
#define JLONG_TO_PTR(T,P) ((T *)(long)P)
#define PTR_TO_JLONG(P) ((jlong)(long)P)

JNIEXPORT jclass JNICALL Common_FindClass (JNIEnv * env, const char *className);
JNIEXPORT void JNICALL Common_ThrowException (JNIEnv * env,
					   const char *className,
					   const char *errMsg);
JNIEXPORT void *JNICALL Common_malloc (JNIEnv * env, size_t size);
JNIEXPORT void *JNICALL Common_realloc (JNIEnv * env, void *ptr, size_t size);
JNIEXPORT void JNICALL Common_free (JNIEnv * env, void *p);
JNIEXPORT const char *JNICALL Common_jstring_to_cstring (JNIEnv * env,
						      jstring s);
JNIEXPORT void JNICALL Common_free_cstring (JNIEnv * env, jstring s,
					 const char *cstr);
JNIEXPORT jint JNICALL Common_MonitorEnter (JNIEnv * env, jobject o);
JNIEXPORT jint JNICALL Common_MonitorExit (JNIEnv * env, jobject o);

JNIEXPORT jobject JNICALL Common_NewRawDataObject (JNIEnv * env, void *data);
JNIEXPORT void * JNICALL Common_GetRawData (JNIEnv * env, jobject rawdata);

#define Common_RETHROW_EXCEPTION(env) if((*(env))->ExceptionOccurred((env)) != NULL) return NULL;

/* Simple debug macro */
#ifdef DEBUG
#define DBG(x) fprintf(stderr, "%s", (x));
#else
#define DBG(x)
#endif

/* Some O/S's don't declare 'environ' */
#if HAVE_CRT_EXTERNS_H
/* Darwin does not have a variable named environ
   but has a function which you can get the environ
   variable with.  */
#include <crt_externs.h>
#define environ (*_NSGetEnviron())
#else
extern char **environ;
#endif /* HAVE_CRT_EXTERNS_H */

#endif
