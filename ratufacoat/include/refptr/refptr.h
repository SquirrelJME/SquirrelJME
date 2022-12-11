/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Reference pointer implementation, using compiler specific magic to implement
 * this with some required words.
 * 
 * @since 2022/12/11
 */

#ifndef SQUIRRELJME_REFPTR_H
#define SQUIRRELJME_REFPTR_H

#include "sjmerc.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_REFPTR_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

#if defined(_MSC_VER)
	/* Microsoft Visual Studio implementation. */

	/** Start of function for reference pointers */
	#define SJME_REFPTR_BEGIN

	/** End of function for reference pointers. */
	#define SJME_REFPTR_END

	/** Reference pointer variables. */
	#define SJME_REFPTR_VARS(...) \
		struct sjme_refptr_vars__ { __VA_ARGS__ }
#else
	/* Not supported so needs to be added. */
	#error No RefPtr Implementation!
#endif

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_REFPTR_H
}
		#undef SJME_CXX_SQUIRRELJME_REFPTR_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_REFPTR_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_REFPTR_H */
