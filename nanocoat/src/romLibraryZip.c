/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "sjme/rom.h"
#include "sjme/alloc.h"
#include "sjme/debug.h"
#include "sjme/payload.h"
#include "sjme/romInternal.h"
#include "sjme/util.h"
#include "sjme/zip.h"
#include "sjme/cleanup.h"

static sjme_errorCode sjme_rom_zipLibraryInit(
	sjme_attrInNotNull sjme_rom_library inLibrary,
	sjme_attrInNullable sjme_pointer data)
{
	if (inLibrary == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Nothing needs to be done. */
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_rom_zipLibraryResourceStream(
	sjme_attrInNotNull sjme_rom_library inLibrary,
	sjme_attrOutNotNull sjme_stream_input* outStream,
	sjme_attrInNotNull sjme_lpcstr resourceName)
{
	sjme_errorCode error;
	sjme_zip zip;
	sjme_zip_entry entry;
	
	if (inLibrary == NULL || outStream == NULL || resourceName == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover zip. */
	zip = inLibrary->handle;
	if (zip == NULL)
		return SJME_ERROR_ILLEGAL_STATE;

	/* Locate the entry. */
	memset(&entry, 0, sizeof(entry));
	if (sjme_error_is(error = sjme_zip_locateEntry(zip,
		&entry, resourceName)))
	{
		/* File not found maps to resource not found. */
		if (error == SJME_ERROR_FILE_NOT_FOUND)
			return SJME_ERROR_RESOURCE_NOT_FOUND;
		
		return sjme_error_default(error);
	}
	
	/* Open input stream over resource. */
	return sjme_zip_entryRead(&entry, outStream);
}

/** Library functions for Zip access. */
static const sjme_rom_libraryFunctions sjme_rom_zipLibraryFunctions =
{
	.init = sjme_rom_zipLibraryInit,
	.resourceStream = sjme_rom_zipLibraryResourceStream,
};

sjme_errorCode sjme_rom_libraryFromZipMemory(
	sjme_attrInNotNull sjme_alloc_pool* pool,
	sjme_attrOutNotNull sjme_rom_library* outLibrary,
	sjme_attrInNotNull sjme_lpcstr libName,
	sjme_attrInNotNull sjme_cpointer base,
	sjme_attrInPositive sjme_jint length)
{
	sjme_seekable seekable;
	sjme_errorCode error;

	if (pool == NULL || outLibrary == NULL || base == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (length < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;

	/* Get source seekable. */
	seekable = NULL;
	if (sjme_error_is(error = sjme_seekable_openMemory(pool,
		&seekable, base, length)) || seekable == NULL)
		return sjme_error_default(error);

	/* This is just an alias for the other function. */
	return sjme_rom_libraryFromZipSeekable(pool, outLibrary,
		libName, seekable);
}

sjme_errorCode sjme_rom_libraryFromZipSeekable(
	sjme_attrInNotNull sjme_alloc_pool* pool,
	sjme_attrOutNotNull sjme_rom_library* outLibrary,
	sjme_attrInNotNull sjme_lpcstr libName,
	sjme_attrInNotNull sjme_seekable seekable)
{
	sjme_errorCode error;
	sjme_zip zip;
	sjme_rom_library result;
	
	if (pool == NULL || outLibrary == NULL || seekable == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Try opening as a Zip file. */
	zip = NULL;
	if (sjme_error_is(error = sjme_zip_openSeekable(pool, &zip,
		seekable)) || zip == NULL)
		return sjme_error_default(error);
	
	/* Setup new library. */
	result = NULL;
	if (sjme_error_is(error = sjme_rom_libraryNew(pool,
		&result, libName, NULL,
		&sjme_rom_zipLibraryFunctions, NULL)) ||
		result == NULL)
		goto fail_libraryNew;
	
	/* Count up Zip. */
	if (sjme_error_is(error = sjme_alloc_weakRef(zip, NULL)))
		goto fail_refUp;
	
	/* Set handle. */
	result->handle = zip;
	
	/* Success! */
	*outLibrary = result;
	return SJME_ERROR_NONE;
	
fail_refUp:
fail_libraryNew:
	/* Close the zip before failing. */
	sjme_closeable_close(SJME_AS_CLOSEABLE(zip));
	
	return sjme_error_default(error);
}
