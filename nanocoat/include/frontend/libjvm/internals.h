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
