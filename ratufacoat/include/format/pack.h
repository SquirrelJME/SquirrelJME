/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Support for multiple libraries at once.
 * 
 * @since 2021/09/12
 */

#ifndef SQUIRRELJME_PACK_H
#define SQUIRRELJME_PACK_H

#include "sjmerc.h"
#include "error.h"
#include "format/detect.h"
#include "memchunk.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_PACK_H
extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

typedef struct sjme_packInstance sjme_packInstance;

/**
 * This represents a library driver that is available for usage.
 * 
 * @since 2021/09/12
 */
typedef struct sjme_packDriver
{
	/** Is this for the given libraries driver? */
	sjme_formatDetectFunction detect;
	
	/**
	 * Initializes the given instance.
	 * 
	 * @param instance The instance pointer. 
	 * @param error The error value.
	 * @return If this was successful or not.
	 * @since 2021/09/22
	 */
	sjme_jboolean (*initInstance)(sjme_packInstance* instance,
		sjme_error* error); 
} sjme_packDriver;

/**
 * Instance of a libraries, is internally kept state.
 * 
 * @since 2021/09/19
 */ 
struct sjme_packInstance
{
	/** The driver used to interact with the library. */
	const sjme_packDriver* driver;
	
	/** Memory chunk. */
	sjme_memChunk chunk;
	
	/** Instance state for the current driver. */
	void* state;
};

/**
 * Opens the given pack and makes an instance of it.
 * 
 * @param outInstance The output instance for returning.
 * @param data The data block.
 * @param size The size of the data block.
 * @param error The error state on open.
 * @return If this was successful or not.
 * @since 2021/09/19
 */
sjme_jboolean sjme_packOpen(sjme_packInstance** outInstance, const void* data,
	sjme_jint size, sjme_error* error);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_PACK_H
}
#undef SJME_CXX_SQUIRRELJME_PACK_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_PACK_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_PACK_H */
