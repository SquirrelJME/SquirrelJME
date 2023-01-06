/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Unwrapping of variadic arguments for all of the duplicated @c va_list and
 * @c ... method calls.
 * 
 * @since 2023/01/06
 */

#ifndef SQUIRRELJME_VAUNWRAP_H
#define SQUIRRELJME_VAUNWRAP_H

#include "sjmejni/sjmejni.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_VAUNWRAP_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Returns the number of method calls that are used for the call.
 *
 * @param outNum The number of arguments that are used.
 * @param vmThread The virtual machine thread used.
 * @param object The object used for reference
 * @param method The method to get the count of.
 * @param error Any potential error state.
 * @return If the call was successful.
 * @since 2023/01/06
 */
sjme_jboolean sjme_vmNumMethodArgs(sjme_jsize* outNum, sjme_vmThread* vmThread,
	sjme_jobject object, sjme_vmMethod method, sjme_error* error);

/**
 * Unwraps a @c va_list to @c sjme_jvalue* so as to reduce the amount of
 * duplicate code used when calling methods.
 *
 * @param outValues The output where all of the combined values are stored.
 * @param numValues The input size of the values.
 * @param vmThread The current thread this is for.
 * @param object The object being referenced.
 * @param method The method to unwrap for, will be used to set the correct
 * types.
 * @param error Any potential error state.
 * @param args The arguments to unwrap, this should be accessed via the @c ...
 * or a copied @c va_list accordingly.
 * @return If the arguments were unwrapped, otherwise @c sjme_false indicates
 * an error.
 * @since 2023/01/06
 */
sjme_jboolean sjme_vmUnwrapVaArgs(sjme_jvalue* outValues, sjme_jsize numValues,
	sjme_vmThread* vmThread, sjme_jobject object, sjme_vmMethod method,
	sjme_error* error, va_list args);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_VAUNWRAP_H
}
		#undef SJME_CXX_SQUIRRELJME_VAUNWRAP_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_VAUNWRAP_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_VAUNWRAP_H */
