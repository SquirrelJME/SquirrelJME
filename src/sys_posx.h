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
 * POSIX Header.
 *
 * @since 2016/02/26
 */

/** Header guard. */
#ifndef SJME_hGSYS_POSXH
#define SJME_hGSYS_POSXH

/** Anti-C++. */
#ifdef _cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_cXSYS_POSXH
extern "C"
{
#endif /** #ifdef SJME_CXX_IS_EXTERNED */
#endif /** #ifdef __cplusplus */

/* Configuration. */
#include "config.h"

/** Specific System Only. */
#if SJME_TARGET == SJME_TARGET_POSIX || defined(SJME_STANDARD_POSIX)

/****************************************************************************/

/** Force POSIX to exist. */
#ifndef SJME_STANDARD_POSIX
	#define SJME_STANDARD_POSIX
#endif

/** Force UNIX. */
#ifndef SJME_STANDARD_UNIX
	#define SJME_STANDARD_UNIX
#endif

/** Force UNIX inclusion. */
#include "sys_unix.h"

/****************************************************************************/

/** End. */
#endif

/** Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_cXSYS_POSXH
}
#undef SJME_cXSYS_POSXH
#undef SJME_CXX_IS_EXTERNED
#endif /** #ifdef SJME_cXSYS_POSXH */
#endif /** #ifdef __cplusplus */

/** Header guard. */
#endif /* #ifndef SJME_hGSYS_POSXH */

