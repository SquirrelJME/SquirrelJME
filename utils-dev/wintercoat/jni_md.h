/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: WinterCoat
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// --------------------------------------------------------------------------*/

/**
 * Java Native Machine definitions.
 *
 * @since 2016/10/19
 */

/** Header guard. */
#ifndef SJME_hGJNI_MDH
#define SJME_hGJNI_MDH

/** Anti-C++. */
#ifdef _cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_cXJNI_MDH
extern "C"
{
#endif /** #ifdef SJME_CXX_IS_EXTERNED */
#endif /** #ifdef __cplusplus */

/****************************************************************************/

#include <stdint.h>

/** Byte. */
typedef int8_t jbyte;

/** Short. */
typedef int16_t jshort;

/** Integer. */
typedef int32_t jint;

/** Long. */
typedef int64_t jlong;

/** Float. */
typedef float jfloat;

/** Double. */
typedef double jdouble;

/** Character. */
typedef uint16_t jchar;

/** Not used on POSIX. */
#define JNICALL
#define JNIEXPORT
#define JNIIMPORT

/****************************************************************************/

/** Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_cXJNI_MDH
}
#undef SJME_cXJNI_MDH
#undef SJME_CXX_IS_EXTERNED
#endif /** #ifdef SJME_cXJNI_MDH */
#endif /** #ifdef __cplusplus */

/** Header guard. */
#endif /* #ifndef SJME_hGJNI_MDH */

