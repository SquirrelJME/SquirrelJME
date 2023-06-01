/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Input/Output for IEEE1275.
 * 
 * @since 2022/12/26
 */

#ifndef SQUIRRELJME_IO_H
#define SQUIRRELJME_IO_H

#include "frontend/ieee1275/ieee1275.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_IO_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Writes to the given device.
 *
 * @param iHandle The handle to write to.
 * @param src The source buffer to write.
 * @param srcLen The length of the data to write.
 * @return Will return the value returned by the target write, or @c -1 if
 * there is no write capability.
 * @since 2022/12/26
 */
sjme_jint sjme_ieee1275Write(const sjme_ieee1275IHandle iHandle,
	const void* const src, const size_t srcLen);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_IO_H
}
		#undef SJME_CXX_SQUIRRELJME_IO_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_IO_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_IO_H */
