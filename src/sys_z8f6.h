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
 * Zilog Z8F64200100KITG Board.
 *
 * @since 2016/02/26
 */

/** Header guard. */
#ifndef SJME_hGSYS_Z8F6H
#define SJME_hGSYS_Z8F6H

/** Anti-C++. */
#ifdef _cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_cXSYS_Z8F6H
extern "C"
{
#endif /** #ifdef SJME_CXX_IS_EXTERNED */
#endif /** #ifdef __cplusplus */

/** Configuration Header. */
#include "config.h"

/** Specific System Only. */
#if SJME_TARGET == SJME_TARGET_Z8F642001

/****************************************************************************/

/** eZ8 specific header. */
#include "ez8.h"

/** Standard headers. */
#include "stdarg.h"
#include "stddef.h"
#include "limits.h"
#include "float.h"
#include "stdlib.h"
#include "setjmp.h"

/** Where code is placed. */
#define sjme_code rom

/** Where normal memory is used. */
#define sjme_mem far

/** Strings in the code area. */
#define sjme_codestr(v) R##v

/** Strings in the memory area. */
#define sjme_memstr(v) F##v

/** Do not modify variable. */
#define sjme_const const

/****************************************************************************/

/** End. */
#endif

/** Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_cXSYS_Z8F6H
}
#undef SJME_cXSYS_Z8F6H
#undef SJME_CXX_IS_EXTERNED
#endif /** #ifdef SJME_cXSYS_Z8F6H */
#endif /** #ifdef __cplusplus */

/** Header guard. */
#endif /* #ifndef SJME_hGSYS_Z8F6H */

