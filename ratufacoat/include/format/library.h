/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Library (JAR) support.
 * 
 * @since 2021/09/12
 */

#ifndef SQUIRRELJME_LIBRARY_H
#define SQUIRRELJME_LIBRARY_H

#include "sjmerc.h"
#include "counter.h"
#include "error.h"
#include "format/def.h"
#include "format/detect.h"
#include "format/format.h"
#include "memchunk.h"
#include "stream.h"
#include "utf.h"

/*--------------------------------------------------------------------------*/

/**
 * Opens an entry within the library as a memory chunk.
 * 
 * @param libInstance The instance of the library to get the entry from.
 * @param outChunk The output memory chunk where the data is located.
 * @param index The index of the entry to load.
 * @param error Any error state that occurs.
 * @return If opening the entry was successful.
 * @since 2021/12/16
 */
typedef sjme_jboolean (*sjme_libraryEntryChunkFunction)(
	sjme_libraryInstance* libInstance, sjme_countableMemChunk** outChunk,
	sjme_jint index, sjme_error* error);
	
/**
 * Opens an entry within the library as a data stream.
 * 
 * @param libInstance The instance of the library to get the entry from.
 * @param outStream The output stream that is used to access the data.
 * @param index The index of the entry to load.
 * @param error Any error state that occurs.
 * @return If opening the entry was successful.
 * @since 2021/12/16
 */
typedef sjme_jboolean (*sjme_libraryEntryStreamFunction)(
	sjme_libraryInstance* libInstance, sjme_dataStream** outStream,
	sjme_jint index, sjme_error* error);

/**
 * This represents a library driver that is available for usage.
 * 
 * @since 2021/09/12
 */
typedef struct sjme_libraryDriver
{
	/** Is this for the given library driver? */
	sjme_formatDetectFunction detect;
	
	/** Initialization function. */
	sjme_formatInitFunction init;
	
	/** Destroy function. */
	sjme_formatDestroyFunction destroy;
	
	/** Function for opening entries as chunks. */
	sjme_libraryEntryChunkFunction entryChunk;
	
	/** Function for opening entries as streams. */
	sjme_libraryEntryStreamFunction entryStream;
} sjme_libraryDriver;

/**
 * Instance of a library which represents a single JAR or set of classes.
 * 
 * @since 2021/09/19
 */ 
struct sjme_libraryInstance
{
	/** The format instance. */
	sjme_formatInstance format;
	
	/** The driver used for the library. */
	const sjme_libraryDriver* driver;
	
	/** Instance state for the current driver. */
	void* state;
	
	/** Counter for the library instance. */
	sjme_counter counter;
	
	/** The pointer to the pack this is within, will be @c NULL if not. */
	void* packOwner;
	
	/** The index within the pack file, will be @c -1 if not in a pack. */
	sjme_jint packIndex;
	
	/** The number of entries which are in this library. */
	sjme_jint numEntries;
	
	/** The name of this library. */
	sjme_utfString* name;
};

/**
 * Closes the given library instance.
 * 
 * @param instance The instance of the library to close. 
 * @param error The error state if not closed.
 * @return If the library was properly closed.
 * @since 2021/10/31
 */
sjme_jboolean sjme_libraryClose(sjme_libraryInstance* instance,
	sjme_error* error);

/**
 * Opens an entry within the library as a memory chunk.
 * 
 * @param libInstance The instance of the library to get the entry from.
 * @param outChunk The output memory chunk where the data is located.
 * @param index The index of the entry to load.
 * @param error Any error state that occurs.
 * @return If opening the entry was successful.
 * @since 2021/11/13
 */
sjme_jboolean sjme_libraryEntryChunk(sjme_libraryInstance* libInstance,
	sjme_countableMemChunk** outChunk, sjme_jint index, sjme_error* error);

/**
 * Opens an entry within the library as a data stream.
 * 
 * @param libInstance The instance of the library to get the entry from.
 * @param outStream The output stream that is used to access the data.
 * @param index The index of the entry to load.
 * @param error Any error state that occurs.
 * @return If opening the entry was successful.
 * @since 2021/11/13
 */
sjme_jboolean sjme_libraryEntryStream(sjme_libraryInstance* libInstance,
	sjme_dataStream** outStream, sjme_jint index, sjme_error* error);

/**
 * Opens the given library and makes an instance of it.
 * 
 * @param outInstance The output instance for returning.
 * @param data The data block.
 * @param size The size of the data block.
 * @param error The error state on open.
 * @return If this was successful or not.
 * @since 2021/09/19
 */
sjme_jboolean sjme_libraryOpen(sjme_libraryInstance** outInstance,
	const void* data, sjme_jint size, sjme_error* error);

/*--------------------------------------------------------------------------*/

#endif /* SQUIRRELJME_LIBRARY_H */
