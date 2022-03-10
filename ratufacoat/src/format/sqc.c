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
#include "memchunk.h"

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

sjme_jboolean sjme_sqcDestroy(sjme_sqcState* sqcInstancePtr,
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

sjme_jboolean sjme_sqcGetProperty(const sjme_sqcState* sqcState,
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

sjme_jboolean sjme_sqcGetPropertyPtr(const sjme_sqcState* sqcState,
	sjme_jint index, void** out, sjme_error* error)
{
	sjme_jint val;
	
	/* Try to read the internal value first. */
	val = 0;
	if (!sjme_sqcGetProperty(sqcState, index, &val, error))
		return sjme_false;
	
	/* Make sure the value is within the SQC chunk. */
	if (val < 0 || val >= sqcState->chunk->size)
	{
		sjme_setError(error, SJME_ERROR_BADADDRESS, val);
		return sjme_false;
	}
	
	/* Calculate offset. */
	*out = SJME_POINTER_OFFSET_LONG(sqcState->chunk->data, val);
	return sjme_true;
}

sjme_jboolean sjme_sqcInit(sjme_formatInstance* formatInstance,
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
