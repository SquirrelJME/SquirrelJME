/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/nvm/romMeepSwm.h"
#include "sjme/nvm/cleanup.h"

static sjme_errorCode sjme_nvm_rom_swmLoadSingle(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInNotNull sjme_nvm_rom_suite inSuite,
	sjme_attrInNotNull sjme_nvm_rom_library inLibrary,
	sjme_attrInNotNull sjme_nvm_rom_swmLibrary* inOutDepend)
{
	if (allocPool == NULL || inSuite == NULL || inLibrary == NULL ||
		inOutDepend == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_nvm_rom_swmLoad(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInNotNull sjme_nvm_rom_suite inSuite,
	sjme_attrOutNotNull sjme_nvm_rom_swmManager* outSwmManager)
{
	sjme_errorCode error;
	sjme_nvm_rom_swmManager result;
	sjme_list_sjme_nvm_rom_library* libraries;
	sjme_nvm_rom_library library;
	sjme_list(sjme_nvm_rom_swmLibrary)* suiteDepends;
	sjme_nvm_rom_swmLibrary* suiteDependency;
	sjme_jint i, n;

	if (allocPool == NULL || inSuite == NULL || outSwmManager == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Allocate the result as it gets added into. */
	result = NULL;
	if (sjme_error_is(error = sjme_nvm_alloc(allocPool,
		sizeof(*result), SJME_NVM_STRUCT_ROM_MEEP_SWM,
		SJME_AS_NVM_COMMONP(&result))) ||
		result == NULL)
		goto fail_allocResult;

	/* We need the library list to process everything. */
	libraries = NULL;
	if (sjme_error_is(error = sjme_nvm_rom_suiteLibraries(inSuite,
		&libraries)) || libraries == NULL)
		goto fail_suiteLibs;

	/* Allocate list of suite information. */
	suiteDepends = NULL;
	if (sjme_error_is(error = sjme_list_alloc(allocPool,
		libraries->length, &suiteDepends, sjme_nvm_rom_swmLibrary, 0)) ||
		suiteDepends == NULL)
		goto fail_allocDepends;
	result->libraries = suiteDepends;

	/* Go through and process each library. */
	for (n = libraries->length, i = 0; i < n; i++)
	{
		/* Ignore blank library slots. */
		library = libraries->elements[i];
		if (library == NULL)
			continue;

		/* Set library here. */
		suiteDependency = &suiteDepends->elements[i];
		suiteDependency->library = library;

		/* Parse this library's dependency information. */
		if (sjme_error_is(error = sjme_nvm_rom_swmLoadSingle(allocPool,
			inSuite, library, suiteDependency)))
			goto fail_parseDepends;
	}

	/* Return the resultant dependency information. */
	*outSwmManager = result;
	return SJME_ERROR_NONE;

fail_parseDepends:
fail_suiteLibs:
fail_allocDepends:
fail_allocResult:
	if (result != NULL)
		sjme_closeable_close(SJME_AS_CLOSEABLE(result));
	return sjme_error_default(error);
}

sjme_errorCode sjme_nvm_rom_swmResolve(
	sjme_attrInNotNull sjme_nvm_rom_swmManager swmManager,
	sjme_attrInNotNull sjme_nvm_rom_library inLibrary,
	sjme_attrOutNotNull sjme_lpstr* outMainClass,
	sjme_attrOutNotNull sjme_list(sjme_lpstr)** outMainArgs,
	sjme_attrOutNotNull sjme_list(sjme_jint)** outById,
	sjme_attrOutNotNull sjme_list(sjme_lpstr)** outByName)
{
	if (swmManager == NULL || inLibrary == NULL || outMainClass == NULL ||
		outMainArgs == NULL || outById == NULL || outByName == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}
