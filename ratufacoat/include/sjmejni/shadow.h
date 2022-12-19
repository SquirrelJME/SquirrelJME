/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * SquirrelJME JNI Shadow that hides within pointers.
 * 
 * @since 2022/12/18
 */

#ifndef SQUIRRELJME_SHADOW_H
#define SQUIRRELJME_SHADOW_H

#include "sjmejni/sjmejni.h"
#include "error.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_SHADOW_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Shadow for the virtual machine state.
 *
 * @since 2022/12/18
 */
typedef struct sjme_vmStateShadow
{
	/** Pointer to self. */
	struct sjme_vmState* functions;
} sjme_vmStateShadow;

/**
 * Shadow for a single virtual machine thread.
 *
 * @since 2022/12/18
 */
typedef struct sjme_vmThreadShadow
{
	/** Pointer to self. */
	struct sjme_vmThread* functions;

	/** The parent virtual machine of this thread. */
	sjme_vmStateShadow* parentVm;

	/** The last error for this thread, will be set by operations. */
	sjme_error lastError;
} sjme_vmThreadShadow;

/**
 * Returns the virtual machine shadow of the state.
 *
 * @param vm The VM state to shadow.
 * @since 2022/12/18
 */
#define sjme_vmGetStateShadow(vm) ((sjme_vmStateShadow*)(*(vm)))

/**
 * Returns the virtual machine shadow of a thread.
 *
 * @param vm The VM thread to shadow.
 * @since 2022/12/18
 */
#define sjme_vmGetThreadShadow(thread) ((sjme_vmThreadShadow*)(*(thread)))

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_SHADOW_H
}
		#undef SJME_CXX_SQUIRRELJME_SHADOW_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_SHADOW_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_SHADOW_H */
