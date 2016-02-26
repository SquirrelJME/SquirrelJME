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
 * Standard C Header.
 *
 * @since 2016/02/26
 */

/** Header guard. */
#ifndef SJME_hGSYS_STDCH
#define SJME_hGSYS_STDCH

/** Anti-C++. */
#ifdef _cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_cXSYS_STDCH
extern "C"
{
#endif /** #ifdef SJME_CXX_IS_EXTERNED */
#endif /** #ifdef __cplusplus */

/** Configuration Header. */
#include "config.h"

/** Specific System Only. */
#if SJME_TARGET == SJME_TARGET_CSTANDARD

/****************************************************************************/

/****************************************************************************/

/** End. */
#endif

/** Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_cXSYS_STDCH
}
#undef SJME_cXSYS_STDCH
#undef SJME_CXX_IS_EXTERNED
#endif /** #ifdef SJME_cXSYS_STDCH */
#endif /** #ifdef __cplusplus */

/** Header guard. */
#endif /* #ifndef SJME_hGSYS_STDCH */

