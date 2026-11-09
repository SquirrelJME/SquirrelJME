/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Internals to libjvm.
 * 
 * @since 2025/06/25
 */

#ifndef SJME_C_INTERNALS_H
#define SJME_C_INTERNALS_H

#include "sjme/error.h"
#include "frontend/libjvm/commonJniJvm.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_INTERNALS_H
extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/** Accesses the JVM in the reserved space. */
#define SJME_JNI_JVM_JVM(vm) ((vm)->reserved0)

/** Accesses the JNI Environment in the reserved space. */
#define SJME_JNI_JVM_ENV(vm) ((vm)->reserved1)

/** Accesses the NanoCoat VM Task in the reserved space. */
#define SJME_JNI_JVM_TASK(vm) ((vm)->reserved2)
	
/** Accesses the JVM in the reserved space. */
#define SJME_JNI_ENV_JVM(env) ((env)->reserved1)

/** Accesses the JNI Environment in the reserved space. */
#define SJME_JNI_ENV_ENV(env) ((env)->reserved0)

/** Accesses the NanoCoat VM Task in the reserved space. */
#define SJME_JNI_ENV_TASK(env) ((env)->reserved2)
	
/** JNI Library function. */
#define SJME_JNI_LIB_FUNCTION
	
jint JNICALL sjme_jni_JvmDestroyJavaVM(
	sjme_attrInNotNull JavaVM* jvm);
	
#pragma region(notImplemented)
	
/** Not implemented (void). */
#define SJME_LIBJVM_NOT_IMPLEMENTED_VOID \
	sjme_todo("Impl?")
	
/** Not implemented (null). */
#define SJME_LIBJVM_NOT_IMPLEMENTED_NULL \
	SJME_LIBJVM_NOT_IMPLEMENTED_VOID; \
	return NULL /* NOLINT */
	
/** Not implemented (number). */
#define SJME_LIBJVM_NOT_IMPLEMENTED_NUMBER \
	SJME_LIBJVM_NOT_IMPLEMENTED_VOID; \
	return 0 /* NOLINT */
	
/** Not implemented (typed). */
#define SJME_LIBJVM_NOT_IMPLEMENTED_TYPED(t) \
	t todoIgnored; /* NOLINT */ \
	SJME_LIBJVM_NOT_IMPLEMENTED_VOID; /* NOLINT */ \
	memset(&todoIgnored, 0, sizeof(todoIgnored)); /* NOLINT */ \
	return todoIgnored /* NOLINT */
	
/** Not implemented (jlong). */
#define SJME_LIBJVM_NOT_IMPLEMENTED_JLONG \
	SJME_LIBJVM_NOT_IMPLEMENTED_TYPED(jlong)
	
/** Not implemented (jfloat). */
#define SJME_LIBJVM_NOT_IMPLEMENTED_JFLOAT \
	SJME_LIBJVM_NOT_IMPLEMENTED_TYPED(jfloat)
	
/** Not implemented (jdouble). */
#define SJME_LIBJVM_NOT_IMPLEMENTED_JDOUBLE \
	SJME_LIBJVM_NOT_IMPLEMENTED_TYPED(jdouble)

#pragma endregion(notImplemented)
	
/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_INTERNALS_H
}
#undef SJME_CXX_INTERNALS_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_INTERNALS_H */
#endif /* #ifdef __cplusplus */

#endif /* SJME_C_INTERNALS_H */
