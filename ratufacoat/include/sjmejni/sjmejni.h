/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * SquirrelJME defined JNI interface.
 * 
 * @since 2022/12/11
 */

#ifndef SQUIRRELJME_SJMEJNI_H
#define SQUIRRELJME_SJMEJNI_H

#include "ccfeatures.h"

#if defined(SJME_HAS_STDINT_H)
	#include <stdint.h>
#endif

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_SJME_JNI_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

#if defined(SJME_HAS_STDINT_H)
	/** Boolean. */
	typedef uint8_t sjme_jboolean;

	/** Byte. */
	typedef int8_t sjme_jbyte;

	/** Character. */
	typedef uint16_t sjme_jchar;

	/** Short. */
	typedef int16_t sjme_jshort;

	/** Integer. */
	typedef int32_t sjme_jint;

	/** Long. */
	typedef int64_t sjme_jlong;

	/** Float. */
	typedef float sjme_jfloat;

	/** Double. */
	typedef double sjme_jdouble;

#elif defined(SJME_FEATURE_MSVC)
	/** Boolean. */
	typedef unsigned __int8 sjme_jboolean;

	/** Byte. */
	typedef signed __int8 sjme_jbyte;

	/** Character. */
	typedef unsigned __int16 sjme_jchar;

	/** Short. */
	typedef signed __int16 sjme_jshort;

	/** Integer. */
	typedef signed __int32 sjme_jint;

	/** Long. */
	typedef signed __int64 sjme_jlong;

	/** Float. */
	typedef float sjme_jfloat;

	/** Double. */
	typedef double sjme_jdouble;
#else
	#error No standard types are known.
#endif

/** Size type. */
typedef sjme_jint sjme_jsize;

/** False. */
#define SJME_FALSE 0

/** True. */
#define SJME_TRUE 1

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_SJME_JNI_H
}
		#undef SJME_CXX_SQUIRRELJME_SJME_JNI_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_SJME_JNI_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_SJMEJNI_H */
