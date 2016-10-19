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
 * WinterCoat includes.
 *
 * @since 2016/10/19
 */

/** Header guard. */
#ifndef SJME_hGWINTERCOATH
#define SJME_hGWINTERCOATH

/** Anti-C++. */
#ifdef _cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_cXWINTERCOATH
extern "C"
{
#endif /** #ifdef SJME_CXX_IS_EXTERNED */
#endif /** #ifdef __cplusplus */

/****************************************************************************/

#include "jni.h"
#include "jvm.h"

/**
 * Prints the specified location and aborts.
 *
 * @param pfile The location of the TODO.
 * @param pline The line of the TODO.
 * @param pfunc The function of the TODO.
 * @since 2016/10/19
 */
#define WC_TODO() WC_TODO_real(__FILE__, __LINE__, __func__)
void WC_TODO_real(const char* const pin, int pline, const char* const pfunc);

/**
 * Checks for the specified condition and if it fails, an abort occurs.
 *
 * @param pfile The location of the TODO.
 * @param pline The line of the TODO.
 * @param pfunc The function of the TODO.
 * @param pcode The error code.
 * @param pcond The condition.
 * @since 2016/10/19
 */
#define WC_ASSERT(code, i) \
	WC_ASSERT_real(__FILE__, __LINE__, __func__, (code), (i))
void WC_ASSERT_real(const char* const pin, int pline, const char* const pfunc,
	const char* const pcode, int pcond);

/****************************************************************************/

/** Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_cXWINTERCOATH
}
#undef SJME_cXWINTERCOATH
#undef SJME_CXX_IS_EXTERNED
#endif /** #ifdef SJME_cXWINTERCOATH */
#endif /** #ifdef __cplusplus */

/** Header guard. */
#endif /* #ifndef SJME_hGWINTERCOATH */

