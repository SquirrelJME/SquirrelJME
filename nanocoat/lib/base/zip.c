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

/** Magic number for the central directory. */
#define SJME_ZIP_ECDIR_MAGIC INT32_C(0x06054B50)

/** Minimum length of the end central directory record. */
#define SJME_ZIP_ECDIR_MIN_LENGTH 22

/** Maximum length of the end central directory record. */
#define SJME_ZIP_ECDIR_MAX_LENGTH (SJME_ZIP_ECDIR_MIN_LENGTH + 65535)

/** The offset to the comment length. */
#define SJME_ZIP_ECDIR_OFF_COMMENT_LEN (SJME_ZIP_ECDIR_MIN_LENGTH - 2)

static sjme_errorCode sjme_zip_findCentralDir(
	sjme_attrInNotNull sjme_seekable seekable,
	sjme_attrOutNotNull sjme_jint* outPos)
{
	sjme_errorCode error;
	sjme_jint seekLen, scanAt, stopAt;
	sjme_jint magic;
	sjme_jchar commentLen;
	
	if (seekable == NULL || outPos == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Need to know the length of the seekable first. */
	seekLen = -1;
	if (sjme_error_is(error = sjme_seekable_size(seekable,
		&seekLen)) || seekLen < 0)
		return sjme_error_default(error);
	
	/* Too short to even be a ZIP? */
	if (seekLen < SJME_ZIP_ECDIR_MIN_LENGTH)
		return SJME_ERROR_TOO_SHORT;
		
	/* Stop here. */
	stopAt = seekLen - SJME_ZIP_ECDIR_MAX_LENGTH;
	if (stopAt < 0)
		stopAt = 0;
	
	/* Constantly scan for the central directory. */
	scanAt = seekLen - SJME_ZIP_ECDIR_MIN_LENGTH;
	for (; scanAt >= stopAt; scanAt--)
	{
		/* Read in magic number. */
		magic = -1;
		if (sjme_error_is(error = sjme_seekable_readLittle(seekable, 4,
			&magic, scanAt, sizeof(magic))))
			return sjme_error_default(error);
		
		/* Is this the one? */
		if (magic == SJME_ZIP_ECDIR_MAGIC)
		{
			/* Read in the comment length. */
			commentLen = 65535;
			if (sjme_error_is(error = sjme_seekable_readLittle(seekable, 2,
				&commentLen,
				scanAt + SJME_ZIP_ECDIR_OFF_COMMENT_LEN,
				sizeof(commentLen))))
				return sjme_error_default(error);
			
			/* Cannot exceed the file size. */
			if (scanAt + SJME_ZIP_ECDIR_MIN_LENGTH + commentLen > seekLen)
				continue;
			
			/* Should be here! */
			*outPos = scanAt;
			return SJME_ERROR_NONE;
		}
	}
	
	/* If this was reached, this is not a Zip! */
	return SJME_ERROR_NOT_ZIP;
}

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
	sjme_errorCode error;
	sjme_jint centralDirPos;
	
	if (inPool == NULL || outZip == NULL || inSeekable == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Locate the central directory within the Zip. */
	centralDirPos = -1;
	if (sjme_error_is(error = sjme_zip_findCentralDir(inSeekable,
		&centralDirPos)) || centralDirPos < 0)
		return sjme_error_default(error);
	
	sjme_todo("Implement this?");
	return sjme_error_notImplemented(0);
}
