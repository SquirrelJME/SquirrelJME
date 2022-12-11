/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Java JNI compatible header.
 * 
 * @since 2022/12/11
 */

#ifndef SQUIRRELJME_JNI_H
#define SQUIRRELJME_JNI_H

#include "sjmejni/sjmejni.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_JNI_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/** Boolean. */
typedef sjme_jboolean jboolean;

/** Byte. */
typedef sjme_jbyte jbyte;

/** Character. */
typedef sjme_jchar jchar;

/** Short. */
typedef sjme_jshort jshort;

/** Integer. */
typedef sjme_jint jint;

/** Long. */
typedef sjme_jlong jlong;

/** Float. */
typedef sjme_jfloat jfloat;

/** Double. */
typedef sjme_jdouble jdouble;

/** Size type. */
typedef jint jsize;

/** False. */
#define JNI_FALSE SJME_FALSE

/** True. */
#define JNI_TRUE SJME_TRUE

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_JNI_H
}
		#undef SJME_CXX_SQUIRRELJME_JNI_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_JNI_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_JNI_H */
