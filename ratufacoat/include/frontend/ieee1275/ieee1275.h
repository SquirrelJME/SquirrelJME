/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * IEEE1275 Support Header, defines the shared standard implementation.
 * 
 * @since 2022/12/26
 */

#ifndef SQUIRRELJME_IEEE1275_H
#define SQUIRRELJME_IEEE1275_H

#include "sjmejni/sjmejni.h"

/* PowerPC platform?. */
#if defined(SJME_HAS_POWERPC)
	#include "frontend/ieee1275/powerpc/ieee1275platform.h"
#endif

/* SPARC platform? */
#if defined(SJME_HAS_SPARC)
	#include "frontend/ieee1275/sparc/ieee1275platform.h"
#endif

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_IEEE1275_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/** The number of valid IEEE1275 entry point arguments. */
#define SJME_IEEE1275_NUM_ARGS 16

/** P Handle, typically represents a device node. */
typedef sjme_jupointer sjme_ieee1275PHandle;

/** I Handle. */
typedef sjme_jpointer sjme_ieee1275IHandle;

/** Invalid PHandle. */
#define SJME_IEEE1275_INVALID_PHANDLE ((sjme_ieee1275PHandle)UINT32_C(-1))

/** Invalid IHandle. */
#define SJME_IEEE1275_INVALID_IHANDLE ((sjme_ieee1275IHandle)INT32_C(0))

/** No property. */
#define SJME_IEEE1275_NO_PROPERTY ((sjme_jint)INT32_C(-1))

/**
 * Arguments to pass to IEEE1275.
 *
 * @since 2022/12/26
 */
typedef struct sjme_ieee1275Args
{
	/** Service to call. */
	const char* service;

	/** Number of arguments. */
	sjme_jint numArgs;

	/** Return value. */
	sjme_jint retVal;

	/** Arguments to pass. */
	sjme_jupointer args[SJME_IEEE1275_NUM_ARGS];
} sjme_ieee1275Args;

/**
 * Entry function for callback into an IEEE1275 function.
 *
 * @param args Pointer to arguments.
 * @return The result of the call.
 * @since 2022/12/26
 */
typedef sjme_jint (*sjme_ieee1275EntryFuncDef)(const sjme_ieee1275Args* args);

/**
 * Performs a basic IEEE1275 call to the given entry function.
 *
 * @param entryFunc The entry function to call.
 * @param args The arguments to the call.
 * @param numRetVals The number of return values used.
 * @since 2022/12/26
 */
sjme_jupointer sjme_ieee1275BaseCall(sjme_ieee1275EntryFuncDef entryFunc,
	sjme_ieee1275Args* const args, sjme_jint* numRetVals);

/**
 * Performs a basic IEEE1275 call using the default entry function.
 *
 * @param args The arguments to the call.
 * @param numRetVals The number of return values used.
 * @since 2022/12/26
 */
sjme_jupointer sjme_ieee1275Call(sjme_ieee1275Args* const args,
	sjme_jint* numRetVals);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_IEEE1275_H
}
		#undef SJME_CXX_SQUIRRELJME_IEEE1275_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_IEEE1275_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_IEEE1275_H */
