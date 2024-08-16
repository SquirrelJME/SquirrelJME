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

static sjme_errorCode sjme_rom_zipSuiteInit(
	sjme_attrInNotNull sjme_rom_suite inSuite)
{
	if (inSuite == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

static sjme_errorCode sjme_rom_zipSuiteLibraryId(
	sjme_attrInNotNull sjme_rom_suite inSuite,
	sjme_attrInNotNull sjme_rom_library inLibrary,
	sjme_attrOutNotNull sjme_jint* outId)
{
	if (inSuite == NULL || inLibrary == NULL || outId == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

static sjme_errorCode sjme_rom_zipSuiteListLibraries(
	sjme_attrInNotNull sjme_rom_suite inSuite,
	sjme_attrOutNotNull sjme_list_sjme_rom_library** outLibraries)
{
	if (inSuite == NULL || outLibraries == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

static sjme_errorCode sjme_rom_zipSuiteLoadLibrary()
{
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

/** Functions for Zip based suites. */
static sjme_rom_suiteFunctions sjme_rom_zipSuiteFunctions =
{
	.init = sjme_rom_zipSuiteInit,
	.libraryId = sjme_rom_zipSuiteLibraryId,
	.list = sjme_rom_zipSuiteListLibraries,
	.loadLibrary = sjme_rom_zipSuiteLoadLibrary,
};

sjme_errorCode sjme_rom_suiteFromZipSeekable(
	sjme_attrInNotNull sjme_alloc_pool* pool,
	sjme_attrOutNotNull sjme_rom_suite* outSuite,
	sjme_attrInNotNull sjme_seekable seekable)
{
	sjme_errorCode error;
	sjme_zip zip;
	sjme_rom_suite result;
	
	if (pool == NULL || outSuite == NULL || seekable == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Try opening as a Zip file. */
	zip = NULL;
	if (sjme_error_is(error = sjme_zip_openSeekable(pool, &zip,
		seekable)) || zip == NULL)
		return sjme_error_default(error);
	
	/* Setup new suite. */
	result = NULL;
	if (sjme_error_is(error = sjme_rom_suiteNew(pool,
		&result,
		&sjme_rom_zipSuiteFunctions, NULL)) ||
		result == NULL)
		goto fail_suiteNew;
	
	/* Count up Zip. */
	if (sjme_error_is(error = sjme_alloc_weakRef(zip, NULL)))
		goto fail_refUp;
	
	/* Set handle. */
	result->handle = zip;
	
	/* Success! */
	*outSuite = result;
	return SJME_ERROR_NONE;
	
fail_refUp:
fail_suiteNew:
	/* Close the zip before failing. */
	sjme_closeable_close(SJME_AS_CLOSEABLE(zip));
	
	return sjme_error_default(error);
}
