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

/** The class version defined on 2020/11/29. */
#define SQC_CLASS_VERSION_20201129 INT16_C(1)

/** The class version offset. */
#define SQC_CLASS_VERSION_OFFSET INT32_C(4)

/** The offset for the number of properties. */
#define SQC_NUM_PROPERTIES_OFFSET INT32_C(6)

/* --------------------------------- COMMON ------------------------------- */

/**
 * State for the SQC.
 * 
 * @since 2021/10/17
 */
typedef struct sjme_sqcState
{
	/** The class version. */
	sjme_jshort classVersion;
	
	/** The number of properties in the SQC. */
	sjme_jint numProperties;
} sjme_sqcState;

/* ---------------------------------- PACK -------------------------------- */

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

static sjme_jboolean sjme_initSqcPackInstance(sjme_packInstance* instance,
	sjme_error* error)
{
	sjme_sqcState* state;
	sjme_jshort classVersion, numProperties;
	
	/* Check. */
	if (instance == NULL)
	{
		sjme_setError(error, SJME_ERROR_NULLARGS, 0);
		return sjme_false;
	}
	
	/* Read version info. */
	if (!sjme_chunkReadBigShort(&instance->chunk, SQC_CLASS_VERSION_OFFSET,
		&classVersion, error))
		return sjme_false;
	
	/* Only a specific version is valid for now. */
	if (classVersion != SQC_CLASS_VERSION_20201129)
	{
		sjme_setError(error, SJME_ERROR_INVALID_CLASS_VERSION,
			classVersion);
		return sjme_false;
	}
	
	/* Read in the property count. */
	if (!sjme_chunkReadBigShort(&instance->chunk, SQC_NUM_PROPERTIES_OFFSET,
		&numProperties, error))
		return sjme_false;
	
	/* Allocate state. */
	state = sjme_malloc(sizeof(*state), error);
	if (state == NULL)
		return sjme_false;
	
	/* Load state with SQC properties. */
	state->classVersion = classVersion;
	state->numProperties = ((sjme_jint)numProperties) & SJME_JINT_C(0xFFFF);
	
	/* Everything is okay. */
	return sjme_true;
}

const sjme_packDriver sjme_packSqcDriver =
{
	.detect = sjme_detectSqcPack,
	.initInstance = sjme_initSqcPackInstance,
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

const sjme_libraryDriver sjme_librarySqcDriver =
{
	.detect = sjme_detectSqcLib,
};
