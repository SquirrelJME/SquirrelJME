/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * IEEE1275 boot mechanisms.
 * 
 * @since 2022/12/26
 */

#ifndef SQUIRRELJME_BOOT_H
#define SQUIRRELJME_BOOT_H

#include "frontend/ieee1275/ieee1275.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_BOOT_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/** Entry function for IEEE1275. */
extern sjme_ieee1275EntryFuncDef sjme_ieee1275EntryFunc;

/** The entry point arguments. */
extern const char* sjme_ieee1275EntryArgs;

/** The number of entry point arguments. */
extern sjme_juint sjme_ieee1275EntryNumArgs;

/** The PHandle of the chosen device. */
extern sjme_ieee1275PHandle sjme_ieee1275ChosenPHandle;

/**
 * Performs booting of the IEEE1275 System.
 *
 * @since 2022/12/26
 */
void sjme_ieee1275Boot(void);

/**
 * Entry point for the IEEE1275 kernel, specific to a given platform.
 *
 * @since 2022/12/26
 */
void sjme_ieee1275BootArch(void);

/**
 * Exits to IEEE1275 and terminates the kernel.
 *
 * @since 2022/12/26
 */
void sjme_ieee1275Exit(void);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_BOOT_H
}
		#undef SJME_CXX_SQUIRRELJME_BOOT_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_BOOT_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_BOOT_H */
