/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "debug.h"
#include "format/sqc.h"
#include "memory.h"
#include "utf.h"

/** The magic number for individual JAR libraries. */
#define JAR_MAGIC_NUMBER UINT32_C(0x00456570)

/** Index to the number of resources that exist within a JAR. */
#define SJME_JAR_COUNT_TOC_INDEX SJME_JINT_C(1)

/** The table of contents index within the JARs. */
#define SJME_JAR_OFFSET_TOC_INDEX SJME_JINT_C(2)

/** The table of contents size. */
#define SJME_JAR_SIZE_TOC_INDEX SJME_JINT_C(3)

/** The index specifying the name of the JAR. */
#define SJME_JAR_OFFSET_NAME_INDEX SJME_JINT_C(5)

/** Flags for the index within the TOC. */
#define SJME_JAR_TOC_INT_FLAGS_INDEX SJME_JINT_C(0)

/** Offset to the JAR resource data. */
#define SJME_JAR_TOC_OFFSET_DATA_INDEX SJME_JINT_C(4)

/** The size of the JAR resource data. */
#define SJME_JAR_TOC_SIZE_DATA_INDEX SJME_JINT_C(5)

/** Flag for compressed entries. */
#define SJME_JAR_TOC_FLAG_COMPRESSED_FLAG SJME_JINT_C(32)

/**
 * Detects library files.
 * 
 * @param data ROM data. 
 * @param size ROM size.
 * @param error Error output.
 * @return If detected or not.
 * @since 2021/09/12
 */
static sjme_jboolean sjme_sqcLibraryDetect(const void* data, sjme_jint size,
	sjme_error* error)
{
	return sjme_detectMagicNumber(data, size,
		JAR_MAGIC_NUMBER, error);
}

/**
 * Destroys a library instance of a SQC.
 * 
 * @param instance The target instance to destroy.
 * @param error The error state.
 * @return If destruction was successful.
 * @since 2021/11/07
 */
static sjme_jboolean sjme_sqcLibraryDestroy(void* instance,
	sjme_error* error)
{
	sjme_libraryInstance* libraryInstance = instance;
	sjme_sqcLibraryState* sqcLibraryState = libraryInstance->state;
	
	/* Destroy the base SQC state. */
	if (!sjme_sqcDestroy(&sqcLibraryState->sqcState, error))
		return sjme_false;
	
	/* Finish destroying the library. */
	if (!sjme_free(libraryInstance->state, error))
		return sjme_false;
	
	/* Destruction happened. */
	return sjme_true;
}

/**
 * When an entry is closed, this will un-count the library as well.
 * 
 * @param counter The counter to collect.
 * @param error The error state. 
 * @return If collection was a success.
 * @since 2021/12/6
 */
static sjme_jboolean sjme_sqcLibraryEntryChunkCollect(sjme_counter* counter,
	sjme_error* error)
{
	sjme_libraryInstance* libInstance;
	
	if (counter == NULL)
		return sjme_setErrorF(error, SJME_ERROR_NULLARGS, 0);

	/* Count down the instances of the library being used. */ 
	libInstance = counter->dataPointer;
	return sjme_counterDown(&libInstance->counter,
		NULL, error);
}

/**
 * Opens a single entry within the SQC as a chunk.
 * 
 * @param libInstance The instance of the library to get the entry from.
 * @param outChunk The output memory chunk where the data is located.
 * @param index The index of the entry to load.
 * @param error Any error state that occurs.
 * @return If opening the entry was successful.
 * @since 2021/12/16
 */
static sjme_jboolean sjme_sqcLibraryEntryChunk(
	sjme_libraryInstance* libInstance, sjme_countableMemChunk** outChunk,
	sjme_jint index, sjme_error* error)
{
	sjme_jint entryFlags;
	sjme_jint entryOffset;
	sjme_jint entrySize;
	sjme_sqcLibraryState* sqcLibraryState;
	sjme_memChunk entryChunk;
	sjme_countableMemChunk* result;
	
	if (libInstance == NULL || outChunk == NULL)
		return sjme_setErrorF(error, SJME_ERROR_NULLARGS, 0);
	
	if (index < 0 || index >= libInstance->numEntries)
		return sjme_setErrorF(error, SJME_ERROR_OUT_OF_BOUNDS, index);
	
	sqcLibraryState = libInstance->state;
	
	/* Try to get the flags. */
	entryFlags = 0;
	if (!sjme_sqcTocGet(&sqcLibraryState->entryToc,
			&entryFlags, index,
			SJME_JAR_TOC_INT_FLAGS_INDEX, error))
		return sjme_setErrorF(error, SJME_ERROR_INVALID_JAR_FILE, index);
	
	/* If this is compressed we need to treat it as a stream. */
	if ((entryFlags & SJME_JAR_TOC_FLAG_COMPRESSED_FLAG) != 0)
	{
		sjme_todo("Implement compressed SQC JAR entries");
		
		return sjme_false;
	}
	
	/* Read the entry position. */
	entryOffset = -1;
	entrySize = -1;
	if (!sjme_sqcTocGet(&sqcLibraryState->entryToc,
			&entryOffset, index,
			SJME_JAR_TOC_OFFSET_DATA_INDEX, error) ||
		!sjme_sqcTocGet(&sqcLibraryState->entryToc,
			&entrySize, index,
			SJME_JAR_TOC_SIZE_DATA_INDEX, error))
		return sjme_setErrorF(error, SJME_ERROR_INVALID_JAR_FILE, index);
	
	/* Split off the chunk position. */
	memset(&entryChunk, 0, sizeof(entryChunk));
	if (!sjme_chunkSubChunk(sqcLibraryState->sqcState.chunk,
		&entryChunk, entryOffset, entrySize, error))
	{
		sjme_setError(error, SJME_ERROR_INVALID_JAR_FILE,
			sjme_getError(error, index));
		
		return sjme_false;
	}
	
	/* Allocate result. */
	result = sjme_malloc(sizeof(*result), error);
	if (result == NULL)
		return sjme_setErrorF(error, SJME_ERROR_NO_MEMORY, index);
	
	/* Setup count and chunk details and counter. */
	result->chunk = entryChunk;
	sjme_counterInit(&result->count,
		sjme_sqcLibraryEntryChunkCollect, libInstance,
		index, error);
	
	/* Count the library up as it is being used more. */
	sjme_counterUp(&libInstance->counter, error);
	
	/* Use the calculated chunk. */
	*outChunk = result;
	return sjme_true;
}

/**
 * Initializes the state for an SQC Library.
 * 
 * @param instance The instance to initialize.
 * @param error The error state.
 * @return If initialization was successful.
 * @since 2021/11/07
 */
static sjme_jboolean sjme_sqcLibraryInit(void* instance,
	sjme_error* error)
{
	sjme_libraryInstance* libraryInstance = instance;
	sjme_sqcLibraryState* sqcLibraryState;
	sjme_jint numEntries;
	
	/* Allocate state storage. */
	sqcLibraryState = sjme_malloc(sizeof(*sqcLibraryState), error);
	if (sqcLibraryState == NULL)
		return sjme_false;
		
	/* Set instance used. */
	libraryInstance->state = sqcLibraryState;
	
	/* Use common initialization sequence. */
	if (!sjme_sqcInit(&libraryInstance->format,
		&sqcLibraryState->sqcState, error))
		return sjme_false;
	
	/* Determine the number of entries within the JAR. */
	numEntries = -1;
	if (!sjme_sqcGetProperty(&sqcLibraryState->sqcState,
		SJME_JAR_COUNT_TOC_INDEX, &numEntries, error) ||
		numEntries < 0)
	{
		sjme_setError(error, SJME_ERROR_INVALID_JAR_FILE, numEntries);
		
		return sjme_false;
	}
	
	/* Setup JAR properties. */
	libraryInstance->numEntries = numEntries;
	
	/* Load the library name accordingly. */
	if (!sjme_sqcGetPropertyPtr(&sqcLibraryState->sqcState,
		SJME_JAR_OFFSET_NAME_INDEX,
		(void**)&libraryInstance->name, error))
		return sjme_setErrorF(error, SJME_ERROR_INVALID_JAR_FILE, 0);
	
	/* Load TOC for the library. */
	if (!sjme_sqcInitToc(&sqcLibraryState->sqcState,
		&sqcLibraryState->entryToc, SJME_JAR_COUNT_TOC_INDEX,
		SJME_JAR_OFFSET_TOC_INDEX,
		SJME_JAR_SIZE_TOC_INDEX, error))
		return sjme_false;
	
	/* Success! */
	return sjme_true;
}

const sjme_libraryDriver sjme_librarySqcDriver =
{
	.detect = sjme_sqcLibraryDetect,
	.init = sjme_sqcLibraryInit,
	.destroy = sjme_sqcLibraryDestroy,
	.entryChunk = sjme_sqcLibraryEntryChunk,
};
