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
 * Standard UNIX Header.
 *
 * @since 2016/02/26
 */

/** Header guard. */
#ifndef SJME_hGSYS_UNIXH
#define SJME_hGSYS_UNIXH

/** Anti-C++. */
#ifdef _cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_cXSYS_UNIXH
extern "C"
{
#endif /** #ifdef SJME_CXX_IS_EXTERNED */
#endif /** #ifdef __cplusplus */

/** Specific System Only. */
#if SJME_TARGET == SJME_TARGET_UNIX || defined(SJME_STANDARD_UNIX)

/****************************************************************************/

/** Enable standard C. */
#ifndef SJME_STANDARD_C
	#define SJME_STANDARD_C
#endif

/** Force UNIX. */
#ifndef SJME_STANDARD_UNIX
	#define SJME_STANDARD_UNIX
#endif

/** Force standard C inclusion. */
#include "sys_stdc.h"

/****************************************************************************/

/** End. */
#endif

/** Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_cXSYS_UNIXH
}
#undef SJME_cXSYS_UNIXH
#undef SJME_CXX_IS_EXTERNED
#endif /** #ifdef SJME_cXSYS_UNIXH */
#endif /** #ifdef __cplusplus */

/** Header guard. */
#endif /* #ifndef SJME_hGSYS_UNIXH */

