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
static sjme_jboolean sjme_sqcGetProperty(sjme_sqcState* sqcState,
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

/* ---------------------------------- PACK -------------------------------- */

/** The index to the table of contents count. */
#define SJME_PACK_COUNT_TOC_INDEX SJME_JINT_C(1)

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
	return sjme_detectMagicNumber(data, size, PACK_MAGIC_NUMBER, error);
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
	if (!sjme_sqcInit(&packInstance->format, &sqcPackState->sqcState,
		error))
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
	if (instance == NULL || outChunk == NULL)
	{
		sjme_setError(error, SJME_ERROR_NULLARGS, 0);
		
		return sjme_false;
	}
	
	/* Out of bounds? */
	if (index < 0 || index > instance->numLibraries)
	{
		sjme_setError(error, SJME_ERROR_INVALIDARG, index);
		
		return sjme_false;
	}
	
	sjme_todo("Locate chunk??");
}

/**
 * Queries the number of libraries which are in the SQC Pack file.
 * 
 * @param instance The instance to query. 
 * @param error The error state.
 * @return The number of queried libraries or {@code -1} on failure.
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
};

/* -------------------------------- LIBRARY ------------------------------- */

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
	return sjme_detectMagicNumber(data, size, JAR_MAGIC_NUMBER, error);
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
	
	return sjme_true;
}

const sjme_libraryDriver sjme_librarySqcDriver =
{
	.detect = sjme_sqcLibraryDetect,
	.init = sjme_sqcLibraryInit,
	.destroy = sjme_sqcLibraryDestroy,
};
