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

#include "atomic.h"
#include "sjmerc.h"
#include "error.h"
#include "format/detect.h"
#include "format/format.h"
#include "format/library.h"
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
	
	/** Initialization function. */
	sjme_formatInitFunction init;
	
	/** Destroy function. */
	sjme_formatDestroyFunction destroy;
	
	/**
	 * Queries the number of libraries in the pack file.
	 * 
	 * @param instance The instance to query.
	 * @param error The error state.
	 * @return The number of libraries, will be a negative value if it could
	 * not be assessed.
	 * @since 2021/11/07
	 */
	sjme_jint (*queryNumLibraries)(sjme_packInstance* instance,
		sjme_error* error);
	
	/**
	 * Attempts to locate a chunk for the given library, so that it may be
	 * read accordingly.
	 * 
	 * @param instance The pack instance we are loading for.
	 * @param chunk The chunk to be read.
	 * @param index The index to locate.
	 * @param error The error state.
	 * @return If the chunk was located.
	 * @since 2021/11/07
	 */
	sjme_jboolean (*locateChunk)(sjme_packInstance* instance,
		sjme_memChunk* chunk, sjme_jint index, sjme_error* error);
} sjme_packDriver;

/**
 * Instance of a pack, is internally kept state.
 * 
 * @since 2021/09/19
 */ 
struct sjme_packInstance
{
	/** The format instance. */
	sjme_formatInstance format;
	
	/** The driver used for this instance. */
	const sjme_packDriver* driver;
	
	/** Instance state for the current driver. */
	void* state;
	
	/** The number of available libraries. */
	sjme_jint numLibraries;
	
	/** The set of cached libraries in the pack. */
	sjme_atomicPointer* libraries;
	
	/** Counter for the pack instance. */
	sjme_counter counter;
};

/**
 * Closes the given pack instance.
 * 
 * @param instance The instance to close.
 * @param error The error state.
 * @return If closing was a success or not.
 * @since 2021/10/23
 */
sjme_jboolean sjme_packClose(sjme_packInstance* instance,
	sjme_error* error);

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

/**
 * Opens a library within a pack file.
 * 
 * @param packInstance The instance of the pack file.
 * @param outLibrary The output instance of the given library.
 * @param index The index of the library to open.
 * @param error The error state.
 * @return If opening the library was successful.
 * @since 2021/11/07
 */
sjme_jboolean sjme_packOpenLibrary(sjme_packInstance* packInstance,
	sjme_libraryInstance** outLibrary, sjme_jint index, sjme_error* error);

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
