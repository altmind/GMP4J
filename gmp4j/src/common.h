#ifndef __Common_H__
#define __Common_H__

#include <stddef.h>
#include <jni.h>
//#include <config.h>

/* Helper macros for going between pointers and jlongs.  */
#define JLONG_TO_PTR(T,P) ((T *)(long)P)
#define PTR_TO_JLONG(P) ((jlong)(long)P)

JNIEXPORT jclass JNICALL Common_FindClass (JNIEnv * env, const char *className);
JNIEXPORT void JNICALL Common_ThrowException (JNIEnv * env, const char *className, const char *errMsg);
JNIEXPORT void *JNICALL Common_malloc (JNIEnv * env, size_t size);
JNIEXPORT void *JNICALL Common_realloc (JNIEnv * env, void *ptr, size_t size);
JNIEXPORT void JNICALL Common_free (JNIEnv * env, void *p);
JNIEXPORT void JNICALL Common_free_cstring (JNIEnv * env, jstring s, const char *cstr);
JNIEXPORT jobject JNICALL Common_NewRawDataObject (JNIEnv * env, void *data);
JNIEXPORT void * JNICALL Common_GetRawData (JNIEnv * env, jobject rawdata);

#define Common_RETHROW_EXCEPTION(env) if((*(env))->ExceptionOccurred((env)) != NULL) return NULL;

#ifdef DEBUG
#define DBG(x) fprintf(stderr, "%s", (x));
#else
#define DBG(x)
#endif

/* Some O/S's don't declare 'environ' */
#if HAVE_CRT_EXTERNS_H
#include <crt_externs.h>
#define environ (*_NSGetEnviron())
#else
extern char **environ;
#endif /* HAVE_CRT_EXTERNS_H */

#endif
