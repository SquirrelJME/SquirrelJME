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
#include "memio/memchunk.h"
#include "memops.h"

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
		return sjme_setErrorF(error, SJME_ERROR_NULLARGS, 0);
	
	/* Finished destruction. */
	return sjme_true;
}

sjme_jboolean sjme_sqcGetProperty(const sjme_sqcState* sqcState,
	sjme_jint index, sjme_jint* out, sjme_error* error)
{
	if (sqcState == NULL || out == NULL)
		return sjme_setErrorF(error, SJME_ERROR_NULLARGS, 0);
	
	/* Is the read in bounds? */
	if (index < 0 || index >= sqcState->numProperties)
		return sjme_setErrorF(error, SJME_ERROR_OUT_OF_BOUNDS, index);
	
	/* Read in the property. */
	return sjme_chunkReadBigInt(sqcState->chunk,
		SQC_BASE_PROPERTY_OFFSET + (index * SQC_BASE_PROPERTY_BYTES),
		out, error);
}

sjme_jboolean sjme_sqcGetPropertyIntegers(const sjme_sqcState* sqcState,
	sjme_jint index, sjme_jint count, sjme_integerFunction intFunction,
	void* proxy, void* data, sjme_error* error)
{
	sjme_jint* seeker;
	sjme_jint at, bigValue;
	
	if (sqcState == NULL || intFunction == NULL)
		return sjme_setErrorF(error, SJME_ERROR_NULLARGS, 0);
	
	if (count < 0)
		return sjme_setErrorF(error, SJME_ERROR_INVALID_ARGUMENT, count);
	
	/* Get the base pointer where integers are. */
	seeker = NULL;
	if (!sjme_sqcGetPropertyPtr(sqcState, index, (void**)&seeker, error))
		return sjme_setErrorF(error, SJME_ERROR_INVALID_PACK_FILE, 0);
	
	/* Perform function for every integer value. */
	for (at = 0; at < count; at++)
	{
		/* Perform the function call. */
		bigValue = *seeker;
		if (!intFunction(proxy, data, at, sjme_bigInt(bigValue), error))
			return sjme_keepErrorF(error, SJME_ERROR_INVALID_FUNCTIONAL, at);
		
		/* Move the seeker up. */
		seeker = SJME_POINTER_OFFSET_LONG(seeker, sizeof(sjme_jint));
	}
	
	/* There were no failures, so this was a success. */
	return sjme_true;
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
		return sjme_setErrorF(error, SJME_ERROR_BADADDRESS, val);
	
	/* Calculate offset. */
	*out = SJME_POINTER_OFFSET_LONG(sqcState->chunk->data, val);
	return sjme_true;
}

sjme_jboolean sjme_sqcGetPropertyStrings(const sjme_sqcState* sqcState,
	sjme_jint index, sjme_jint count, sjme_utfString* (*outStrings)[],
	sjme_error* error)
{
	sjme_jint at;
	sjme_utfString* seeker;
	
	if (count < 0)
		return sjme_setErrorF(error, SJME_ERROR_INVALID_ARGUMENT, count);
	
	/* Pointless? */
	if (count == 0)
		return sjme_true;
	
	/* Return the base pointer where the property is. */
	seeker = NULL;
	if (!sjme_sqcGetPropertyPtr(sqcState, index, (void**)&seeker, error))
		return sjme_false;
	
	/* Read in all the resultant strings. */
	for (at = 0; at < count; at++)
	{
		/* Set the string to this position. */
		(*outStrings)[at] = seeker;
		
		/* Move the seeker to after this string. */
		seeker = SJME_POINTER_OFFSET_LONG(seeker,
			SJME_SIZEOF_UTF_STRING(sjme_bigShort(seeker->bigLength)));
	}
	
	/* Debugging, for testing purposes. */
#if defined(SJME_DEBUG)
	for (at = 0; at < count; at++)
		sjme_message("Strings[%d/%d]: (%d) %s\n",
			at, count, sjme_bigShort((*outStrings)[at]->bigLength),
			(*outStrings)[at]->chars);
#endif
	
	/* Is okay! */
	return sjme_true;
}

sjme_jboolean sjme_sqcInit(sjme_formatInstance* formatInstance,
	sjme_sqcState* sqcState, sjme_error* error)
{
	sjme_jshort classVersion, numProperties;
	
	/* Check. */
	if (sqcState == NULL)
		return sjme_setErrorF(error, SJME_ERROR_NULLARGS, 0);
	
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
		return sjme_setErrorF(error, SJME_ERROR_NULLARGS, 0);
	
	/* Get the properties to determine where our actual TOC exists. */
	sqcTocCount = sqcTocOffset = sqcTocSize = -1;
	if (!sjme_sqcGetProperty(sqcState, pdxCount,
			&sqcTocCount, error) ||
		!sjme_sqcGetProperty(sqcState, pdxOffset,
			&sqcTocOffset, error) ||
		!sjme_sqcGetProperty(sqcState, pdxSize,
			&sqcTocSize, error))
		return sjme_setErrorF(error, SJME_ERROR_INVALID_PACK_FILE, 1);
	
	/* Get the chunk region where the TOC exists. */
	if (!sjme_chunkSubChunk(sqcState->chunk, &outToc->chunk,
		sqcTocOffset, sqcTocSize, error))
		return sjme_setErrorF(error, SJME_ERROR_INVALID_PACK_FILE, 1);
	
	/* Read the actual count and span the TOC gives us. */
	if (!sjme_chunkReadBigShort(&outToc->chunk, SQC_TOC_COUNT_OFFSET,
			&tocTocCount, error) ||
		!sjme_chunkReadBigShort(&outToc->chunk, SQC_TOC_SPAN_OFFSET,
			&tocTocSpan, error))
		return sjme_setErrorF(error, SJME_ERROR_CORRUPT_TOC, 0);
	
	/* These values are unsigned. */
	outToc->numEntries = tocTocCount & SJME_JINT_C(0xFFFF);
	outToc->span = tocTocSpan & SJME_JINT_C(0xFFFF);
	
	/* Has the TOC been somehow corrupted? It's qualities are different? */
	if (outToc->numEntries != sqcTocCount ||
		sqcTocSize != (SQC_TOC_ENTRY_BASE_OFFSET +
			(SQC_TOC_ENTRY_SIZE * outToc->numEntries * outToc->span)) ||
		((sqcTocSize % 4) != 0))
		return sjme_setErrorF(error, SJME_ERROR_CORRUPT_TOC, 1);
	
	/* Everything is okay! */
	return sjme_true;
}

sjme_jboolean sjme_sqcTocGet(const sjme_sqcToc* sqcToc, sjme_jint* outValue,
	sjme_jint rowIndex, sjme_jint itemInSpan, sjme_error* error)
{
	if (sqcToc == NULL || outValue == NULL)
		return sjme_setErrorF(error, SJME_ERROR_NULLARGS, 0);
	
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
