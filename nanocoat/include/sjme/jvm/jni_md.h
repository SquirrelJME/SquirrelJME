/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * JNI Machine Definitions Compatibility header.
 * 
 * @since 2025/10/22
 */

#ifndef SJME_C_SQUIRRELJME_JNI_MD_H
#define SJME_C_SQUIRRELJME_JNI_MD_H

#include "sjme/config.h"
#include "sjme/stdGone.h"
#include "sjme/multithread.h"
#include "sjme/dylib.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_JNI_MD_H

extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/** False value. */
#define JNI_FALSE (0)

/** True value. */
#define JNI_TRUE (1)

/* No error. */
#define JNI_OK (0)

/* Unknown error */
#define JNI_ERR (-1)

/* The current thread is not attached to any JVM. */
#define JNI_EDETACHED (-2)

/* The version is not valid. */
#define JNI_EVERSION (-3)

/* Out of memory. */
#define JNI_ENOMEM (-4)

/* A JVM already exists. */
#define JNI_EEXIST (-5)

/* Invalid arguments. */
#define JNI_EINVAL (-6)

#if defined(SJME_JNI_IMPLEMENTATION)
	/** Exported JNI symbol. */
	#define JNIEXPORT sjme_attrExport

	/** Calling convention used for JNI calls. */
	#define JNICALL sjme_attrThreadCall
#else
	/** Exported JNI symbol. */
	#define JNIEXPORT

	/** Calling convention used for JNI calls. */
	#define JNICALL sjme_attrExportCall sjme_attrThreadCall
#endif

/** Byte type. */
typedef sjme_jbyteNative jbyte;

/** Boolean type. */
typedef sjme_jubyteNative jboolean;

/** Short type. */
typedef sjme_jshortNative jshort;

/** Character type. */
typedef sjme_jushortNative jchar;

/** Integer type. */
typedef sjme_jintNative jint;

/** Long type. */
typedef sjme_jlongNative jlong;

/** Float type. */
typedef sjme_jfloatNative jfloat;

/** Double type. */
typedef sjme_jdoubleNative jdouble;

/** Size type, used for array indexes. */
typedef jint jsize;

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_JNI_MD_H
}
#undef SJME_CXX_SQUIRRELJME_JNI_MD_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_JNI_MD_H */
#endif /* #ifdef __cplusplus */

#endif /* SJME_C_SQUIRRELJME_JNI_MD_H */
