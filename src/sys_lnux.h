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
 * Linux header.
 *
 * @since 2016/02/26
 */

/** Header guard. */
#ifndef SJME_hGSYS_LNUXH
#define SJME_hGSYS_LNUXH

/** Anti-C++. */
#ifdef _cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_cXSYS_LNUXH
extern "C"
{
#endif /** #ifdef SJME_CXX_IS_EXTERNED */
#endif /** #ifdef __cplusplus */

/** Configuration Header. */
#include "config.h"

/** Specific System Only. */
#if SJME_TARGET == SJME_TARGET_LINUX

/****************************************************************************/

/** Force POSIX. */
#ifndef SJME_STANDARD_POSIX
	#define SJME_STANDARD_POSIX
#endif

/** Force POSIX inclusion. */
#include "sys_posx.h"

/****************************************************************************/

/** End. */
#endif

/** Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_cXSYS_LNUXH
}
#undef SJME_cXSYS_LNUXH
#undef SJME_CXX_IS_EXTERNED
#endif /** #ifdef SJME_cXSYS_LNUXH */
#endif /** #ifdef __cplusplus */

/** Header guard. */
#endif /* #ifndef SJME_hGSYS_LNUXH */

