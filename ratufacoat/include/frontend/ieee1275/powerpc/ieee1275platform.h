/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * PowerPC platform support.
 * 
 * @since 2022/12/26
 */

#ifndef SQUIRRELJME_IEEE1275PLATFORM_H
#define SQUIRRELJME_IEEE1275PLATFORM_H

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_IEEE1275PLATFORM_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

#if 0
/** Disable interrupts for entry calls. */
#define SJME_IEEE1275_DISABLE_INTS_FOR_CALLS
#endif

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_IEEE1275PLATFORM_H
}
		#undef SJME_CXX_SQUIRRELJME_IEEE1275PLATFORM_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_IEEE1275PLATFORM_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_IEEE1275PLATFORM_H */
