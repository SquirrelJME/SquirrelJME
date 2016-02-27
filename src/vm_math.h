/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// --------------------------------------------------------------------------*/

/**
 * Java Virtual Machine Numerical Math.
 *
 * @since 2016/02/26
 */

/** Header guard. */
#ifndef SJME_hGVM_MATHH
#define SJME_hGVM_MATHH

/** Anti-C++. */
#ifdef _cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_cXVM_MATHH
extern "C"
{
#endif /** #ifdef SJME_CXX_IS_EXTERNED */
#endif /** #ifdef __cplusplus */

/* Configuration. */
#include "config.h"

/****************************************************************************/

/** Java boolean. */
typedef struct jboolean
{
	uint8_t v;
} jboolean;

/** Java byte. */
typedef struct jbyte
{
	int8_t v;
} jbyte;

/** Java short. */
typedef struct jshort
{
	int16_t v;
} jshort;

/** Java char. */
typedef struct jchar
{
	uint16_t v;
} jchar;

/** Java int. */
typedef struct jint
{
	int32_t v;
} jint;

/** Java long. */
typedef struct jlong
{
	/* 64-bit math might not be supported by the compiler. */
	#if !defined(UINT64_MAX)
		int32_t v[2];
	
	/* Should be supported. */
	#else
		int64_t v;
	#endif
} jlong;

/** Java float (32-bit). */
typedef struct jfloat
{
	jint v;
} jfloat;

/** Java double (64-bit). */
typedef struct jdouble
{
	jlong v;
} jdouble;

/** Long endianess order. */
#if SJME_ENDIAN == SJME_ENDIAN_BIG
	#define SJME_JLONG_HI 0
	#define SJME_JLONG_LO 1
#elif SJME_ENDIAN == SJME_ENDIAN_LITTLE
	#define SJME_JLONG_HI 1
	#define SJME_JLONG_LO 0
#else
	#error Unknown endianess for jlong.
#endif

/** Double uses the same endiannes as long. */
#define SJME_JDOUBLE_HI SJME_JLONG_HI
#define SJME_JDOUBLE_LO SJME_JDOUBLE_LO

/****************************************************************************/

/** Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_cXVM_MATHH
}
#undef SJME_cXVM_MATHH
#undef SJME_CXX_IS_EXTERNED
#endif /** #ifdef SJME_cXVM_MATHH */
#endif /** #ifdef __cplusplus */

/** Header guard. */
#endif /* #ifndef SJME_hGVM_MATHH */

