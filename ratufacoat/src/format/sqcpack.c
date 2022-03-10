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

/** The magic number for pack libraries. */
#define PACK_MAGIC_NUMBER UINT32_C(0x58455223)

/** The index to the table of contents count. */
#define SJME_PACK_COUNT_TOC_INDEX SJME_JINT_C(1)

/** The index where the TOC offset is located. */
#define SJME_PACK_OFFSET_TOC_INDEX SJME_JINT_C(2)

/** The index which indicates the size of the TOC. */
#define SJME_PACK_SIZE_TOC_INDEX SJME_JINT_C(3)

/** The index which indicates what the main launcher class is. */
#define SJME_PACK_STRING_LAUNCHER_MAIN_CLASS_INDEX SJME_JINT_C(8)

/** The index which indicates the arguments to initialize the launcher. */
#define SJME_PACK_STRINGS_LAUNCHER_ARGS_INDEX SJME_JINT_C(9)

/** The index which indicates the number of launcher args that exist. */
#define SJME_PACK_COUNT_LAUNCHER_ARGS_INDEX SJME_JINT_C(10)

/** Index where the pack flags exist. */
#define SJME_PACK_FLAGS_INDEX SJME_JINT_C(16)

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
	return sjme_chunkSubChunk(pack->sqcState.chunk, outChunk,
		jarOffset, jarSize, error);
}

/**
 * Queries the main arguments that are used to start the launcher.
 * 
 * @param instance The pack instance to query the parameter from.
 * @param outArgs The output main arguments.
 * @param error Any resultant error state.
 * @return If the query was successful.
 * @since 2022/03/09
 */
static sjme_jboolean sjme_sqcPackQueryLauncherArgs(sjme_packInstance* instance,
	sjme_mainArgs** outArgs, sjme_error* error)
{
	sjme_sqcPackState* sqcPackState;
	sjme_mainArgs* result;
	sjme_jint count;
	
	if (instance == NULL || outArgs == NULL)
	{
		sjme_setError(error, SJME_ERROR_NULLARGS, 0);
		return sjme_false;
	}
	
	/* Get the pack state. */
	sqcPackState = instance->state;
	
	/* Read count. */
	count = -1;
	if (!sjme_sqcGetProperty(&sqcPackState->sqcState,
		SJME_PACK_COUNT_LAUNCHER_ARGS_INDEX, &count, error) ||
		count < 0)
	{
		sjme_setError(error, SJME_ERROR_INVALID_PACK_FILE, 0);
		return sjme_false;
	}
	
	/* Allocate output. */
	result = sjme_malloc(SJME_SIZEOF_MAIN_ARGS(count), error);
	if (result == NULL)
	{
		sjme_setError(error, SJME_ERROR_NO_MEMORY, 0);
		return sjme_false;
	}
	
	/* Read the strings in. */
	if (!sjme_sqcGetPropertyStrings(&sqcPackState->sqcState,
		SJME_PACK_STRINGS_LAUNCHER_ARGS_INDEX, count,
		&result->args, error))
	{
		sjme_setError(error, SJME_ERROR_INVALID_PACK_FILE, 1);
		return sjme_false;
	}
	
	/* Use what we calculated! */
	*outArgs = result;
	return sjme_true;
}

/**
 * Queries the main class.
 * 
 * @param instance The pack instance to query the parameter from.
 * @param outMainClass The output main class.
 * @param error Any resultant error state.
 * @return If the query was successful.
 * @since 2022/03/09
 */
static sjme_jboolean sjme_sqcPackQueryLauncherClass(
	sjme_packInstance* instance, sjme_utfString** outMainClass,
	sjme_error* error)
{
	sjme_sqcPackState* sqcPackState;
	
	if (instance == NULL || outMainClass == NULL)
	{
		sjme_setError(error, SJME_ERROR_NULLARGS, 0);
		return sjme_false;
	}
	
	/* Get the pack state. */
	sqcPackState = instance->state;
	
	/* Read the property. */
	if (!sjme_sqcGetPropertyPtr(&sqcPackState->sqcState, 
		SJME_PACK_STRING_LAUNCHER_MAIN_CLASS_INDEX,
			(void**)outMainClass, error))
	{
		sjme_setError(error, SJME_ERROR_INVALID_PACK_FILE, 0);
		return sjme_false;
	}
	
	/* Read okay. */
	return sjme_true;
}

/**
 * Queries the class path that is used for the launcher process.
 * 
 * @param instance The pack instance to query the parameter from.
 * @param outClassPath The output class path.
 * @param error Any resultant error state.
 * @return If the query was successful.
 * @since 2022/03/09
 */
static sjme_jboolean sjme_sqcPackQueryLauncherClassPath(
	sjme_packInstance* instance, sjme_classPath** outClassPath,
	sjme_error* error)
{
	if (instance == NULL || outClassPath == NULL)
	{
		sjme_setError(error, SJME_ERROR_NULLARGS, 0);
		return sjme_false;
	}
	
	sjme_todo("Implement this?");
	return sjme_false;
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

/**
 * Queries the flags of the pack.
 * 
 * @param instance The instance to query. 
 * @param error The error state.
 * @return The number of queried libraries or @c 0 on failure.
 * @since 2022/01/08
 */
static sjme_jint sjme_sqcPackQueryPackFlags(sjme_packInstance* instance,
	sjme_error* error)
{
	sjme_sqcPackState* sqcPackState;
	sjme_jint value = 0;
	
	if (instance == NULL)
	{
		sjme_setError(error, SJME_ERROR_NULLARGS, 0);
		
		return -1;
	}
	
	/* Get the pack state. */
	sqcPackState = instance->state;
	
	/* Read the property. */
	if (!sjme_sqcGetProperty(&sqcPackState->sqcState,
		SJME_PACK_FLAGS_INDEX, &value, error) || value == 0)
	{
		sjme_setError(error, SJME_ERROR_INVALID_PACK_FILE, value);
		
		return 0;
	}
	
	/* Use the resultant value. */
	return value;
}

const sjme_packDriver sjme_packSqcDriver =
{
	.detect = sjme_sqcPackDetect,
	.init = sjme_sqcPackInit,
	.destroy = sjme_sqcPackDestroy,
	.queryLauncherArgs = sjme_sqcPackQueryLauncherArgs,
	.queryLauncherClass = sjme_sqcPackQueryLauncherClass,
	.queryLauncherClassPath =
		sjme_sqcPackQueryLauncherClassPath,
	.queryNumLibraries = sjme_sqcPackQueryNumLibraries,
	.queryPackFlags = sjme_sqcPackQueryPackFlags,
	.locateChunk = sjme_sqcPackLocateChunk,
	
	/* There is no need to close or free any RAM resources because all
	 * libraries are within the SQC chunk in memory. */
	.libraryMarkClosed = NULL,
};
