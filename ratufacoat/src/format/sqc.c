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
#include "memchunk.h"

/** The magic number for pack libraries. */
#define PACK_MAGIC_NUMBER UINT32_C(0x58455223)

/** The magic number for individual JAR libraries. */
#define JAR_MAGIC_NUMBER UINT32_C(0x00456570)

/** The class version offset. */
#define SQC_CLASS_VERSION_OFFSET SJME_JINT_C(4)

/** The offset for the number of properties. */
#define SQC_NUM_PROPERTIES_OFFSET SJME_JINT_C(6)

/** The base offset for properties. */
#define SQC_BASE_PROPERTY_OFFSET SJME_JINT_C(8)

/** The size of each property. */
#define SQC_BASE_PROPERTY_BYTES SJME_JINT_C(4)

/* --------------------------------- COMMON ------------------------------- */

/** The offset for the TOC count. */
#define SQC_TOC_COUNT_OFFSET SJME_JINT_C(0)

/** The offset for the TOC span. */
#define SQC_TOC_SPAN_OFFSET SJME_JINT_C(2)

/** The base offset for TOC entries. */
#define SQC_TOC_ENTRY_BASE_OFFSET SJME_JINT_C(4)

/** The size of a single entry within the TOC. */
#define SQC_TOC_ENTRY_SIZE SJME_JINT_C(4)

/**
 * Destroys the SQC state and anything related to it.
 * 
 * @param sqcInstancePtr The instance to destroy. 
 * @param error Any resultant error.
 * @return If destruction was successful.
 * @since 2021/11/07
 */
static sjme_jboolean sjme_sqcDestroy(sjme_sqcState* sqcInstancePtr,
	sjme_error* error)
{
	if (sqcInstancePtr == NULL)
	{
		sjme_setError(error, SJME_ERROR_NULLARGS, 0);
		
		return sjme_false;
	}
	
	/* Finished destruction. */
	return sjme_true;
}

/**
 * Initializes the SQC instance.
 * 
 * @param formatInstance The format instance.
 * @param sqcState The state pointer for the output.
 * @param error The error state.
 * @return If initializes was successful.
 * @since 2021/11/07 
 */
static sjme_jboolean sjme_sqcInit(sjme_formatInstance* formatInstance,
	sjme_sqcState* sqcState, sjme_error* error)
{
	sjme_jshort classVersion, numProperties;
	
	/* Check. */
	if (sqcState == NULL)
	{
		sjme_setError(error, SJME_ERROR_NULLARGS, 0);
		return sjme_false;
	}
	
	/* Read version info. */
	if (!sjme_chunkReadBigShort(&formatInstance->chunk,
		SQC_CLASS_VERSION_OFFSET, &classVersion, error))
		return sjme_false;
	
	/* Only a specific version is valid for now. */
	if (classVersion != SQC_CLASS_VERSION_20201129)
	{
		sjme_setError(error, SJME_ERROR_INVALID_CLASS_VERSION,
			classVersion);
		return sjme_false;
	}
	
	/* Read in the property count. */
	if (!sjme_chunkReadBigShort(&formatInstance->chunk,
		SQC_NUM_PROPERTIES_OFFSET, &numProperties, error))
		return sjme_false;
	
	/* Load state with SQC properties. */
	sqcState->chunk = &formatInstance->chunk;
	sqcState->classVersion = classVersion;
	sqcState->numProperties = ((sjme_jint)numProperties) &
		SJME_JINT_C(0xFFFF);
	
	/* Everything is okay. */
	return sjme_true;
}

/**
 * Gets a property from the SQC.
 * 
 * @param sqcState The SQC to read from.
 * @param index The property index to read from.
 * @param out The read value.
 * @param error The error state.
 * @return If the read was successful.
 * @since 2021/10/29
 */
static sjme_jboolean sjme_sqcGetProperty(const sjme_sqcState* sqcState,
	sjme_jint index, sjme_jint* out, sjme_error* error)
{
	if (sqcState == NULL || out == NULL)
	{
		sjme_setError(error, SJME_ERROR_NULLARGS, 0);
		
		return sjme_false;
	}
	
	/* Is the read in bounds? */
	if (index < 0 || index >= sqcState->numProperties)
	{
		sjme_setError(error, SJME_ERROR_OUT_OF_BOUNDS, index);
		
		return sjme_false;
	}
	
	/* Read in the property. */
	return sjme_chunkReadBigInt(sqcState->chunk,
		SQC_BASE_PROPERTY_OFFSET + (index * SQC_BASE_PROPERTY_BYTES),
		out, error);
}

sjme_jboolean sjme_sqcInitToc(const sjme_sqcState* sqcState,
	sjme_sqcToc* outToc, sjme_jint pdxCount, sjme_jint pdxOffset,
	sjme_jint pdxSize, sjme_error* error)
{
	sjme_jint sqcTocCount;
	sjme_jint sqcTocOffset;
	sjme_jint sqcTocSize;
	sjme_jshort tocTocCount;
	sjme_jshort tocTocSpan;
	
	if (sqcState == NULL || outToc == NULL)
	{
		sjme_setError(error, SJME_ERROR_NULLARGS, 0);
		
		return sjme_false;
	}
	
	/* Get the properties to determine where our actual TOC exists. */
	sqcTocCount = sqcTocOffset = sqcTocSize = -1;
	if (!sjme_sqcGetProperty(sqcState, pdxCount,
			&sqcTocCount, error) ||
		!sjme_sqcGetProperty(sqcState, pdxOffset,
			&sqcTocOffset, error) ||
		!sjme_sqcGetProperty(sqcState, pdxSize,
			&sqcTocSize, error))
	{
		sjme_setError(error, SJME_ERROR_INVALID_PACK_FILE, 1);
		
		return sjme_false;
	}
	
	/* Get the chunk region where the TOC exists. */
	if (!sjme_chunkSubChunk(sqcState->chunk, &outToc->chunk,
		sqcTocOffset, sqcTocSize, error))
	{
		sjme_setError(error, SJME_ERROR_INVALID_PACK_FILE, 1);
		
		return sjme_false;
	}
	
	/* Read the actual count and span the TOC gives us. */
	if (!sjme_chunkReadBigShort(&outToc->chunk, SQC_TOC_COUNT_OFFSET,
			&tocTocCount, error) ||
		!sjme_chunkReadBigShort(&outToc->chunk, SQC_TOC_SPAN_OFFSET,
			&tocTocSpan, error))
	{
		sjme_setError(error, SJME_ERROR_CORRUPT_TOC, 0);
		
		return sjme_false;
	}
	
	/* These values are unsigned. */
	outToc->numEntries = tocTocCount & SJME_JINT_C(0xFFFF);
	outToc->span = tocTocSpan & SJME_JINT_C(0xFFFF);
	
	/* Has the TOC been somehow corrupted? It's qualities are different? */
	if (outToc->numEntries != sqcTocCount ||
		sqcTocSize != (SQC_TOC_ENTRY_BASE_OFFSET +
			(SQC_TOC_ENTRY_SIZE * outToc->numEntries * outToc->span)) ||
		((sqcTocSize % 4) != 0))
	{
		sjme_setError(error, SJME_ERROR_CORRUPT_TOC, 1);
		
		return sjme_false;
	}
	
	/* Everything is okay! */
	return sjme_true;
}

sjme_jboolean sjme_sqcTocGet(const sjme_sqcToc* sqcToc, sjme_jint* outValue,
	sjme_jint rowIndex, sjme_jint itemInSpan, sjme_error* error)
{
	if (sqcToc == NULL || outValue == NULL)
	{
		sjme_setError(error, SJME_ERROR_NULLARGS, 0);
		
		return sjme_false;
	}
	
	/* Reading an invalid item? */
	if (rowIndex < 0 || itemInSpan < 0 ||
		rowIndex >= sqcToc->numEntries || itemInSpan >= sqcToc->span)
	{
		sjme_setError(error, SJME_ERROR_OUT_OF_BOUNDS, rowIndex);
		
		return sjme_false;
	}
	
	/* Read the value from the TOC, which are in row major order. */
	return sjme_chunkReadBigInt(&sqcToc->chunk,
		SQC_TOC_ENTRY_BASE_OFFSET +
			(rowIndex * (SQC_TOC_ENTRY_SIZE * sqcToc->span)) +
			(SQC_TOC_ENTRY_SIZE * itemInSpan),
		outValue, error);
}

/* ---------------------------------- PACK -------------------------------- */

/** The index to the table of contents count. */
#define SJME_PACK_COUNT_TOC_INDEX SJME_JINT_C(1)

/** The index where the TOC offset is located. */
#define SJME_PACK_OFFSET_TOC_INDEX SJME_JINT_C(2)

/** The index which indicates the size of the TOC. */
#define SJME_PACK_SIZE_TOC_INDEX SJME_JINT_C(3)

/** The index of the offset to the JAR data. */
#define SJME_TOC_PACK_OFFSET_DATA_INDEX SJME_JINT_C(4)

/** The index of the size of the JAR data. */
#define SJME_TOC_PACK_SIZE_DATA_INDEX SJME_JINT_C(5)

/**
 * Detects pack files.
 * 
 * @param data ROM data. 
 * @param size ROM size.
 * @param error Error output.
 * @return If detected or not.
 * @since 2021/09/12
 */
static sjme_jboolean sjme_sqcPackDetect(const void* data, sjme_jint size,
	sjme_error* error)
{
	return sjme_detectMagicNumber(data, size, PACK_MAGIC_NUMBER,
		error);
}

/**
 * Destroys a pack library instance.
 * 
 * @param instance The instance to destroy.
 * @param error The error state.
 * @return If destruction was successful.
 * @since 2021/11/07 
 */
static sjme_jboolean sjme_sqcPackDestroy(void* instance,
	sjme_error* error)
{
	sjme_packInstance* packInstance = instance;
	sjme_sqcPackState* sqcPackState = packInstance->state;
	
	/* Destroy the base SQC state. */
	if (!sjme_sqcDestroy(&sqcPackState->sqcState, error))
		return sjme_false;
	
	/* Finish destroying the pack. */
	if (!sjme_free(packInstance->state, error))
		return sjme_false;
	
	/* Destruction happened. */
	return sjme_true;
}

/**
 * Initializes the SQC pack instance.
 * 
 * @param instance The instance to initialize.
 * @param error The error state.
 * @return If initialization was successful.
 * @since 2021/11/07
 */
static sjme_jboolean sjme_sqcPackInit(void* instance,
	sjme_error* error)
{
	sjme_packInstance* packInstance = instance;
	sjme_sqcPackState* sqcPackState;
	
	/* Allocate state storage. */
	sqcPackState = sjme_malloc(sizeof(*sqcPackState), error);
	if (sqcPackState == NULL)
		return sjme_false;
	
	/* Set instance used. */
	packInstance->state = sqcPackState;
	
	/* Use common initialization sequence. */
	if (!sjme_sqcInit(&packInstance->format,
		&sqcPackState->sqcState, error))
		return sjme_false;
	
	/* Initialize the table of contents for the various libraries. */
	if (!sjme_sqcInitToc(&sqcPackState->sqcState,
		&sqcPackState->libToc, SJME_PACK_COUNT_TOC_INDEX,
		SJME_PACK_OFFSET_TOC_INDEX,
		SJME_PACK_SIZE_TOC_INDEX, error))
		return sjme_false;
	
	return sjme_true;
}

/**
 * Locates the SQC chunk within a pack.
 * 
 * @param instance The instance to load from.
 * @param outChunk The resultant chunk.
 * @param index The index of the chunk to lookup.
 * @param error The error state.
 * @return If the chunk was successfully located.
 * @since 2021/11/07
 */
sjme_jboolean sjme_sqcPackLocateChunk(sjme_packInstance* instance,
	sjme_memChunk* outChunk, sjme_jint index, sjme_error* error)
{
	sjme_sqcPackState* pack;
	sjme_jint jarOffset;
	sjme_jint jarSize;
	
	if (instance == NULL || outChunk == NULL)
	{
		sjme_setError(error, SJME_ERROR_NULLARGS, 0);
		
		return sjme_false;
	}
	
	/* We need this one. */
	pack = instance->state;
	
	/* Out of bounds? */
	if (index < 0 || index >= instance->numLibraries)
	{
		sjme_setError(error, SJME_ERROR_INVALIDARG, index);
		
		return sjme_false;
	}
	
	/* Read the position of the JAR. */
	jarOffset = jarSize = -1;
	if (!sjme_sqcTocGet(&pack->libToc, &jarOffset, index,
			SJME_TOC_PACK_OFFSET_DATA_INDEX, error) ||
		!sjme_sqcTocGet(&pack->libToc, &jarSize, index,
			SJME_TOC_PACK_SIZE_DATA_INDEX, error))
	{
		sjme_setError(error, SJME_ERROR_CORRUPT_PACK_ENTRY, 1);
		
		return sjme_false;
	}
	
	/* Is this an impossibly placed JAR? */
	if (jarOffset < 0 || jarSize < 0)
	{
		sjme_setError(error, SJME_ERROR_CORRUPT_PACK_ENTRY, 2);
		
		return sjme_false;
	}
	
	/* Get the chunk where the entry belongs. */
	return sjme_chunkSubChunk(pack->sqcState.chunk, outChunk, jarOffset,
		jarSize, error);
}

/**
 * Queries the number of libraries which are in the SQC Pack file.
 * 
 * @param instance The instance to query. 
 * @param error The error state.
 * @return The number of queried libraries or @c -1 on failure.
 * @since 2021/11/07
 */
static sjme_jint sjme_sqcPackQueryNumLibraries(sjme_packInstance* instance,
	sjme_error* error)
{
	sjme_sqcPackState* sqcPackState;
	sjme_jint value = -1;
	
	if (instance == NULL)
	{
		sjme_setError(error, SJME_ERROR_NULLARGS, 0);
		
		return -1;
	}
	
	/* Get the pack state. */
	sqcPackState = instance->state;
	
	/* Read the property. */
	if (!sjme_sqcGetProperty(&sqcPackState->sqcState,
		SJME_PACK_COUNT_TOC_INDEX, &value, error) || value < 0)
	{
		sjme_setError(error, SJME_ERROR_INVALID_NUM_LIBRARIES, value);
		
		return -1;
	}
	
	/* Use the resultant value. */
	return value;
}

const sjme_packDriver sjme_packSqcDriver =
{
	.detect = sjme_sqcPackDetect,
	.init = sjme_sqcPackInit,
	.destroy = sjme_sqcPackDestroy,
	.queryNumLibraries = sjme_sqcPackQueryNumLibraries,
	.locateChunk = sjme_sqcPackLocateChunk,
	
	/* There is no need to close or free any RAM resources because all
	 * libraries are within the SQC chunk in memory. */
	.libraryMarkClosed = NULL,
};

/* -------------------------------- LIBRARY ------------------------------- */

/** Index to the number of resources that exist within a JAR. */
#define SJME_JAR_COUNT_TOC_INDEX SJME_JINT_C(1)

/** The table of contents index within the JARs. */
#define SJME_JAR_OFFSET_TOC_INDEX SJME_JINT_C(2)

/** The table of contents size. */
#define SJME_JAR_SIZE_TOC_INDEX SJME_JINT_C(3)

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
	{
		sjme_setError(error, SJME_ERROR_NULLARGS, 0);
		
		return sjme_false;
	}

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
	{
		sjme_setError(error, SJME_ERROR_NULLARGS, 0);
		
		return sjme_false;
	}
	
	if (index < 0 || index >= libInstance->numEntries)
	{
		sjme_setError(error, SJME_ERROR_OUT_OF_BOUNDS, index);
		
		return sjme_false;
	}
	
	sqcLibraryState = libInstance->state;
	
	/* Try to get the flags. */
	entryFlags = 0;
	if (!sjme_sqcTocGet(&sqcLibraryState->entryToc,
			&entryFlags, index,
			SJME_JAR_TOC_INT_FLAGS_INDEX, error))
	{
		sjme_setError(error, SJME_ERROR_INVALID_JAR_FILE, index);
		
		return sjme_false;
	}
	
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
	{
		sjme_setError(error, SJME_ERROR_INVALID_JAR_FILE, index);
		
		return sjme_false;
	}
	
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
	{
		sjme_setError(error, SJME_ERROR_NO_MEMORY, index);
		
		return sjme_false;
	}
	
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
