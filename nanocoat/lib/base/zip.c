/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "sjme/zip.h"
#include "sjme/debug.h"

/** Magic number for the central directory header. */
#define SJME_ZIP_CDIR_MAGIC INT32_C(0x02014B50)

/** Magic number for the end central directory record. */
#define SJME_ZIP_ECDIR_MAGIC INT32_C(0x06054B50)

/** Minimum length of the end central directory record. */
#define SJME_ZIP_ECDIR_MIN_LENGTH 22

/** Offset to the central directory size. */
#define SJME_ZIP_ECDIR_CDIR_LEN_OFFSET 12

/** Maximum length of the end central directory record. */
#define SJME_ZIP_ECDIR_MAX_LENGTH (SJME_ZIP_ECDIR_MIN_LENGTH + 65535)

/** The offset to the comment length. */
#define SJME_ZIP_ECDIR_OFF_COMMENT_LEN (SJME_ZIP_ECDIR_MIN_LENGTH - 2)

static sjme_errorCode sjme_zip_close(
	sjme_attrInNotNull sjme_closeable closeable)
{
	sjme_errorCode error;
	sjme_zip zip;
	
	zip = (sjme_zip)closeable;
	if (zip == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Debug. */
#if defined(SJME_CONFIG_DEBUG)
	sjme_message("Zip close %p", zip);
#endif
	
	/* Lock the zip. */
	if (sjme_error_is(error = sjme_thread_spinLockGrab(&zip->lock)))
		return sjme_error_default(error);
	
	/* Close the seekable. */
	if (sjme_error_is(error = sjme_closeable_close(
		SJME_AS_CLOSEABLE(zip->seekable))))
		goto fail_seekableClose;
		
	/* Un-ref the seekable we just closed. */
	if (sjme_error_is(error = sjme_alloc_weakUnRef(zip->seekable)))
		goto fail_seekableUnref;
		
	/* Release the lock. */
	if (sjme_error_is(error = sjme_thread_spinLockRelease(&zip->lock,
		NULL)))
		return sjme_error_default(error);
	
	/* Success! */
	return SJME_ERROR_NONE;

fail_seekableUnref:
fail_seekableClose:
	/* Release lock before failing. */
	if (sjme_error_is(sjme_thread_spinLockRelease(&zip->lock,
		NULL)))
		return sjme_error_default(error);
	
	return sjme_error_default(error);
}

static sjme_errorCode sjme_zip_findCentralDir(
	sjme_attrInNotNull sjme_seekable seekable,
	sjme_attrOutNotNull sjme_jint* outCDirPos,
	sjme_attrOutNotNull sjme_jint* outEndCDirPos)
{
	sjme_errorCode error;
	sjme_jint seekLen, endCDirAt, stopAt, cDirAt;
	sjme_jint magic;
	sjme_jint cDirLen; 
	sjme_jchar commentLen;
	
	if (seekable == NULL || outCDirPos == NULL || outEndCDirPos == NULL)
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
	
	/* Constantly scan for the end of central directory record. */
	endCDirAt = seekLen - SJME_ZIP_ECDIR_MIN_LENGTH;
	for (; endCDirAt >= stopAt; endCDirAt--)
	{
		/* Read in magic number. */
		magic = -1;
		if (sjme_error_is(error = sjme_seekable_readLittle(seekable, 4,
			&magic, endCDirAt, sizeof(magic))))
			return sjme_error_default(error);
		
		/* Is this the one? */
		if (magic == SJME_ZIP_ECDIR_MAGIC)
		{
			/* Read in the comment length. */
			commentLen = 65535;
			if (sjme_error_is(error = sjme_seekable_readLittle(seekable, 2,
				&commentLen,
				endCDirAt + SJME_ZIP_ECDIR_OFF_COMMENT_LEN,
				sizeof(commentLen))))
				return sjme_error_default(error);
			
			/* Cannot exceed the file size. */
			if (endCDirAt + SJME_ZIP_ECDIR_MIN_LENGTH + commentLen > seekLen)
				continue;
			
			/* Read in length of the central directory. */
			cDirLen = -1;
			if (sjme_error_is(error = sjme_seekable_readLittle(seekable, 4,
				&cDirLen, endCDirAt + SJME_ZIP_ECDIR_CDIR_LEN_OFFSET, 4)) ||
				cDirLen < 0)
				return sjme_error_default(error);
			
			/* Impossible position? */
			cDirAt = endCDirAt - cDirLen;
			if (cDirAt < 0 || cDirAt >= endCDirAt)
				continue;
			
			/* Read in central directory start magic. */
			magic = -1;
			if (sjme_error_is(error = sjme_seekable_readLittle(seekable, 4,
				&magic, cDirAt, sizeof(magic))))
				return sjme_error_default(error);
			
			/* Not the magic we are looking for? */
			if (magic != SJME_ZIP_CDIR_MAGIC)
				continue;
			
			/* Should be here! */
			*outCDirPos = cDirAt;
			*outEndCDirPos = endCDirAt;
			return SJME_ERROR_NONE;
		}
	}
	
	/* If this was reached, this is not a Zip! */
	return SJME_ERROR_NOT_ZIP;
}

sjme_errorCode sjme_zip_entryRead(
	sjme_attrInNotNull const sjme_zip_entry* inEntry,
	sjme_attrOutNotNull sjme_stream_input* outStream)
{
	if (inEntry == NULL || outStream == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Not an actually valid entry? */
	if (inEntry->zip == NULL)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	sjme_todo("Implement this?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_zip_locateEntry(
	sjme_attrInNotNull sjme_zip inZip,
	sjme_attrOutNotNull sjme_zip_entry* outEntry,
	sjme_attrInNotNull sjme_lpcstr entryName)
{
	sjme_errorCode error;
	sjme_jint dirBase, magic;
	sjme_seekable seekable;
	sjme_zip_entry result;
	
	if (inZip == NULL || outEntry == NULL || entryName == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Grab the lock. */
	if (sjme_error_is(error = sjme_thread_spinLockGrab(&inZip->lock)))
		return sjme_error_default(error);
	
	/* Clear result. */
	memset(&result, 0, sizeof(result));
	
	/* Go through and scan for the entry. */
	seekable = inZip->seekable;
	for (dirBase = inZip->centralDirPos;;)
	{
		/* Read in magic number. */
		magic = -1;
		if (sjme_error_is(error = sjme_seekable_readLittle(seekable, 4,
			&magic, dirBase, sizeof(magic))) || magic == -1)
			return sjme_error_default(error);
		
		/* End of directory hit? */
		if (magic == SJME_ZIP_ECDIR_MAGIC)
		{
			error = SJME_ERROR_FILE_NOT_FOUND;
			goto fail_notFound;
		}
		
		/* Read something else? */
		else if (magic != SJME_ZIP_CDIR_MAGIC)
		{
			error = SJME_ERROR_CORRUPT_ZIP;
			goto fail_corrupt;
		}
		
		sjme_todo("Impl?");
		return sjme_error_notImplemented(0);
	}
	
	/* Release the lock. */
	if (sjme_error_is(error = sjme_thread_spinLockRelease(&inZip->lock,
		NULL)))
		return sjme_error_default(error);
	
	/* Success! */
	memmove(outEntry, &result, sizeof(*outEntry));
	return SJME_ERROR_NONE;

fail_corrupt:
fail_notFound:
	/* Release lock before failing. */
	if (sjme_error_is(sjme_thread_spinLockRelease(&inZip->lock,
		NULL)))
		return sjme_error_default(error);
	
	return sjme_error_default(error);
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
	
	/* Un-reference the original memory area as we are on top of it. */
	if (sjme_error_is(error = sjme_alloc_weakUnRef(seekable)))
		return sjme_error_default(error);
	
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
	sjme_jint centralDirPos, endCentralDirPos;
	sjme_zip result;
	
	if (inPool == NULL || outZip == NULL || inSeekable == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Locate the central directory within the Zip. */
	centralDirPos = -1;
	endCentralDirPos = -1;
	if (sjme_error_is(error = sjme_zip_findCentralDir(inSeekable,
		&centralDirPos, &endCentralDirPos)) ||
		centralDirPos < 0 || endCentralDirPos < 0)
		return sjme_error_default(error);
	
	/* Allocate Zip state structure. */
	result = NULL;
	if (sjme_error_is(error = sjme_alloc_weakNew(inPool,
		sizeof(*result),
		sjme_closeable_autoEnqueue, NULL,
		(void**)&result, NULL)))
		return sjme_error_default(error);
	
	/* Store info. */
	result->closeable.closeHandler = sjme_zip_close;
	result->inPool = inPool;
	result->centralDirPos = centralDirPos;
	result->endCentralDirPos = endCentralDirPos;
	result->seekable = inSeekable;
	
	/* Count the seekable up, since we are using it. */
	if (sjme_error_is(error = sjme_alloc_weakRef(result, NULL)))
		return sjme_error_default(error);
	
	/* Success! */
	*outZip = result;
	return SJME_ERROR_NONE;
}
