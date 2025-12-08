/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/dylibExtra.h"
#include "sjme/path.h"

static sjme_errorCode sjme_dylib_attemptOpen(
	sjme_attrInNullable const sjme_nal* nal,
	sjme_attrInNullable sjme_lpcstr libRoot,
	sjme_attrInNotNull sjme_lpcstr dylibName,
	sjme_attrOutNotNull sjme_dylib* outLib)
{
	if (nal == NULL || dylibName == NULL || outLib == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

static sjme_errorCode sjme_dylib_attemptOpenDelve(
	sjme_attrInNullable const sjme_nal* nal,
	sjme_attrInNullable sjme_lpcstr libRoot,
	sjme_attrInNotNull sjme_lpcstr dylibName,
	sjme_attrOutNotNull sjme_dylib* outLib)
{
	sjme_dylib result;
	sjme_errorCode error, derived;
	sjme_cchar libJvmDir[SJME_MAX_PATH];
	
	if (nal == NULL || dylibName == NULL || outLib == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Default to some undefined error. */
	error = SJME_ERROR_UNKNOWN;
	derived = SJME_ERROR_UNKNOWN;

	/* No known library yet. */
	result = NULL;

	/* Prefer specified root first. */
	if (result == NULL || libRoot != NULL)
		if (sjme_error_is(error = sjme_dylib_attemptOpen(nal, libRoot,
			dylibName, &result)) || result == NULL)
			derived = sjme_error_defaultOr(error, derived);

	/* Look in the environment variable for SquirrelJME. */
	if (result == NULL)
	{
		/* Grab from the environment. */
		memset(libJvmDir, 0, sizeof(libJvmDir));
		if (sjme_error_is(error = nal->getEnv(libJvmDir,
			SJME_MAX_PATH - 1, "SQUIRRELJME_LIB_JVM")))
		{
			if (error != SJME_ERROR_NO_SUCH_ELEMENT)
				derived = sjme_error_defaultOr(error, derived);
		}
	}
	
	
	/* Was an actual library found? */
	if (result != NULL)
	{
		*outLib = result;
		return SJME_ERROR_NONE;
	}

	/* Everything failed. */
	return sjme_error_default(derived);
}

static sjme_errorCode sjme_dylib_openExtraScritchAudio(
	sjme_attrInNullable const sjme_nal* nal,
	sjme_attrInNullable sjme_lpcstr libRoot,
	sjme_attrInRange(0, SJME_DYLIB_NUM_EXTRA_FAMILY)
		sjme_dylib_extraFamily family,
	sjme_attrInNullable sjme_lpcstr subComponent,
	sjme_attrOutNotNull sjme_dylib* outLib)
{
	if (nal == NULL || outLib == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

static sjme_errorCode sjme_dylib_openExtraScritchUI(
	sjme_attrInNullable const sjme_nal* nal,
	sjme_attrInNullable sjme_lpcstr libRoot,
	sjme_attrInRange(0, SJME_DYLIB_NUM_EXTRA_FAMILY)
		sjme_dylib_extraFamily family,
	sjme_attrInNullable sjme_lpcstr subComponent,
	sjme_attrOutNotNull sjme_dylib* outLib)
{
	if (nal == NULL || outLib == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_dylib_openExtra(
	sjme_attrInNullable const sjme_nal* nal,
	sjme_attrInNullable sjme_lpcstr libRoot,
	sjme_attrInRange(0, SJME_DYLIB_NUM_EXTRA_FAMILY)
		sjme_dylib_extraFamily family,
	sjme_attrInNullable sjme_lpcstr subComponent,
	sjme_attrOutNotNull sjme_dylib* outLib)
{
	if (outLib == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (family <= 0 || family >= SJME_DYLIB_NUM_EXTRA_FAMILY)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Fallback to default NAL? */
	if (nal == NULL)
		nal = &sjme_nal_default;

	/* Which family type to load? */
	switch (family)
	{
		case SJME_DYLIB_EXTRA_FAMILY_SCRITCHUI:
			return sjme_dylib_openExtraScritchUI(nal, libRoot,
				family, subComponent, outLib);

		case SJME_DYLIB_EXTRA_FAMILY_SCRITCHAUDIO:
			return sjme_dylib_openExtraScritchAudio(nal, libRoot,
				family, subComponent, outLib);
		
		default:
			return sjme_error_notImplemented(0);
	}
}
