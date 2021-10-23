/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "format/sqc.h"
#include "debug.h"
#include "memory.h"
#include "memchunk.h"

/** The magic number for pack libraries. */
#define PACK_MAGIC_NUMBER UINT32_C(0x58455223)

/** The magic number for individual JAR libraries. */
#define JAR_MAGIC_NUMBER UINT32_C(0x00456570)

/** The class version offset. */
#define SQC_CLASS_VERSION_OFFSET INT32_C(4)

/** The offset for the number of properties. */
#define SQC_NUM_PROPERTIES_OFFSET INT32_C(6)

/* --------------------------------- COMMON ------------------------------- */

static sjme_jboolean sjme_initSqcInstance(sjme_formatInstance* formatInstance,
	void** sqcStatePtr, sjme_error* error)
{
	sjme_sqcState* state;
	sjme_jshort classVersion, numProperties;
	
	/* Check. */
	if (sqcStatePtr == NULL)
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
	
	/* Allocate state. */
	state = sjme_malloc(sizeof(*state), error);
	if (state == NULL)
		return sjme_false;
	
	/* Load state with SQC properties. */
	state->classVersion = classVersion;
	state->numProperties = ((sjme_jint)numProperties) & SJME_JINT_C(0xFFFF);
	
	/* Everything is okay. */
	*sqcStatePtr = state;
	return sjme_true;
}

static sjme_jboolean sjme_sqcGetProperty(sjme_sqcState* sqcState,
	sjme_jint* out, sjme_error* error)
{
	sjme_todo("GetProperty(%p, %p, %p)", sqcState, out, error);
}

/* ---------------------------------- PACK -------------------------------- */

/** The index to the table of contents count. */
#define SJME_PACK_COUNT_TOC_INDEX SJME_JINT(1)

/**
 * Detects pack files.
 * 
 * @param data ROM data. 
 * @param size ROM size.
 * @param error Error output.
 * @return If detected or not.
 * @since 2021/09/12
 */
static sjme_jboolean sjme_detectSqcPack(const void* data, sjme_jint size,
	sjme_error* error)
{
	return sjme_detectMagicNumber(data, size, PACK_MAGIC_NUMBER, error);
}

static sjme_jboolean sjme_initSqcPackInstance(void* instance,
	sjme_error* error)
{
	sjme_packInstance* packInstance = instance;
	
	/* Use common initialization sequence. */
	if (!sjme_initSqcInstance(&packInstance->format, &packInstance->state,
		error))
		return sjme_false;
	
	return sjme_true;
}

static sjme_jint sjme_packQueryNumLibraries(sjme_packInstance* instance,
	sjme_error* error)
{
	sjme_sqcState* sqcState;
	sjme_jint value = -1;
	
	if (instance == NULL)
	{
		sjme_setError(error, SJME_ERROR_NULLARGS, 0);
		
		return sjme_false;
	}
	
	/* Read the property. */
	sqcState = (sjme_sqcState*)instance->state;
	if (!sjme_sqcGetProperty(sqcState, &value, error) || value < 0)
	{
		sjme_setError(error, SJME_ERROR_INVALID_NUM_LIBRARIES, value);
		
		return sjme_false;
	}
	
	/* Use the resultant value. */
	return value;
}

const sjme_packDriver sjme_packSqcDriver =
{
	.detect = sjme_detectSqcPack,
	.initInstance = sjme_initSqcPackInstance,
	.queryNumLibraries = sjme_packQueryNumLibraries,
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
static sjme_jboolean sjme_detectSqcLib(const void* data, sjme_jint size,
	sjme_error* error)
{
	return sjme_detectMagicNumber(data, size, JAR_MAGIC_NUMBER, error);
}

static sjme_jboolean sjme_initSqcLibraryInstance(void* instance,
	sjme_error* error)
{
	sjme_libraryInstance* libraryInstance = instance;
	
	/* Use common initialization sequence. */
	if (!sjme_initSqcInstance(&libraryInstance->format,
		&libraryInstance->state, error))
		return sjme_false;
	
	return sjme_true;
}

const sjme_libraryDriver sjme_librarySqcDriver =
{
	.detect = sjme_detectSqcLib,
	.initInstance = sjme_initSqcLibraryInstance,
};
