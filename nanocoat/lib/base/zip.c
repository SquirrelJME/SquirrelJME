/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/zip.h"
#include "sjme/debug.h"

sjme_errorCode sjme_zip_entryRead(
	sjme_attrInNotNull sjme_zip_entry inEntry,
	sjme_attrOutNotNull sjme_stream_input* outStream)
{
	if (inEntry == NULL || outStream == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Implement this?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_zip_locateEntry(
	sjme_attrInNotNull sjme_zip inZip,
	sjme_attrOutNotNull sjme_zip_entry* outEntry,
	sjme_attrInNotNull sjme_lpcstr entryName)
{
	if (inZip == NULL || outEntry == NULL || entryName == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Implement this?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_zip_openMemory(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_zip* outZip,
	sjme_attrInNotNull sjme_pointer rawData,
	sjme_attrInPositive sjme_jint rawSize)
{
	sjme_errorCode error;
	sjme_seekable seekable;
	sjme_zip result;
	
	if (inPool == NULL || outZip == NULL || rawData == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Open seekable wrapping memory. */
	seekable = NULL;
	if (sjme_error_is(error = sjme_seekable_openMemory(inPool,
		&seekable, rawData, rawSize)) ||
		seekable == NULL)
		return sjme_error_default(error);
	
	/* Open Zip from this seekable now. */
	result = NULL;
	if (sjme_error_is(sjme_zip_openSeekable(inPool,
		&result, seekable)) || result == NULL)
	{
		/* Destroy seekable before failing. */
		sjme_alloc_free(seekable);
		
		return sjme_error_default(error);
	}
	
	/* Success! */
	*outZip = result;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_zip_openSeekable(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrOutNotNull sjme_zip* outZip,
	sjme_attrInNotNull sjme_seekable inSeekable)
{
	if (inPool == NULL || outZip == NULL || inSeekable == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Implement this?");
	return sjme_error_notImplemented(0);
}
