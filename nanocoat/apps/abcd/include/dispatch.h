/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Dispatch handlers.
 * 
 * @since 2026/02/22
 */

#ifndef SJME_C_SQUIRRELJME_DISPATCH_H
#define SJME_C_SQUIRRELJME_DISPATCH_H

#include "sjme/config.h"
#include "sjme/stdTypes.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_DISPATCH_H
extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Main function for dispatch.
 * 
 * @param argc Main argument count.
 * @param argv Main arguments passed.
 * @since 2026/02/22
 */
typedef int (*sjme_dispatch_mainFunc)(int argc, char** argv);

/**
 * Prints help.
 * 
 * @since 2026/02/22
 */
typedef void (*sjme_dispatch_helpFunc)(void);

/**
 * Dispatch information.
 * 
 * @since 2026/02/22
 */
typedef struct sjme_dispatch_info
{
	/** The command name. */
	sjme_lpcstr name;
	
	/** The main function. */
	sjme_dispatch_mainFunc main;
	
	/** The help printing function. */
	sjme_dispatch_helpFunc help;
} sjme_dispatch_info;
	
/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_DISPATCH_H
}
#undef SJME_CXX_SQUIRRELJME_DISPATCH_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_DISPATCH_H */
#endif /* #ifdef __cplusplus */

#endif /* SJME_C_SQUIRRELJME_DISPATCH_H */
