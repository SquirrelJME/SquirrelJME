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
#include "sjme/util.h"

/** ScritchAudio library order. */
static const sjme_lpcstr sjme_dylib_extraAudio[] =
{
#if defined(SJME_CONFIG_HAS_OS_POSIX)
	"audio-oss",
#endif
	
#if defined(SJME_CONFIG_HAS_OS_WINDOWS) || \
	defined(SJME_CONFIG_HAS_OS_WINDOWS_CE)
	"audio-winmm",
#endif
	
	NULL,
};

/** ScritchUI library order. */
static const sjme_lpcstr sjme_dylib_extraUi[] =
{
	"ui-gtk2",
	
#if defined(SJME_CONFIG_HAS_OS_WINDOWS) || \
	defined(SJME_CONFIG_HAS_OS_WINDOWS_CE)
	"ui-win32",
#endif
	
	NULL,
};

static sjme_errorCode sjme_dylib_openExtraScritchAny(
	sjme_attrInNullable const sjme_nal* nal,
	sjme_attrInNullable sjme_lpcstr subComponent,
	sjme_attrOutNotNull sjme_dylib* outLib,
	sjme_attrInNotNull const sjme_lpcstr* order)
{
#define TEMP_SIZE 128
	sjme_errorCode error;
	sjme_jint i, dir;
	sjme_lpcstr orderComponent;
	sjme_path tryPath;
	sjme_cchar tempName[TEMP_SIZE];
	sjme_dylib result;
	
	if (nal == NULL || outLib == NULL || order == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Go through all components, use the passed component, if any. */
	result = NULL;
	for (dir = 0; dir < 2; dir++)
		for (i = (subComponent != NULL ? -1 : 0);; i++)
		{
			/* Stop when all possible components have been checked. */
			orderComponent = (i < 0 ? subComponent : order[i]);
			if (orderComponent == NULL)
				break;

			/* Determine base path to use. */
			memset(&tryPath, 0, sizeof(tryPath));
			if (sjme_error_is(error = sjme_path_default(nal,
				&tryPath, (dir == 0 ? SJME_NVM_DEFAULT_DIRECTORY_NATIVES :
					SJME_NVM_DEFAULT_DIRECTORY_EXEC), -1)))
				return sjme_error_default(error);

			/* Determine dynamic library name. */
			memset(tempName, 0, sizeof(tempName));
			if (sjme_error_is(error = sjme_dylib_name(
				"squirreljme-scritch", orderComponent,
				tempName, TEMP_SIZE - 1)))
				return sjme_error_default(error);
			tempName[TEMP_SIZE - 1] = '\0';

			/* Resolve from this path. */
			if (sjme_error_is(error = sjme_path_resolveS(&tryPath, tempName)))
				return sjme_error_default(error);

			/* Attempt loading the library. */
			if (sjme_error_is(error = sjme_dylib_open(tryPath.chars,
				&result)) || result == NULL)
			{
				if (error != SJME_ERROR_COULD_NOT_LOAD_LIBRARY)
					return sjme_error_default(error);

				/* Try again. */
				continue;
			}

			/* A library was found, so stop. */
			break;
		}

	/* Was a library found? */
	if (result != NULL)
	{
		*outLib = result;
		return SJME_ERROR_NONE;
	}

	/* No library was found. */
	return SJME_ERROR_COULD_NOT_LOAD_LIBRARY;
#undef TEMP_SIZE
}

sjme_errorCode sjme_dylib_openExtra(
	sjme_attrInNullable const sjme_nal* nal,
	sjme_attrInRange(0, SJME_DYLIB_NUM_EXTRA_FAMILY)
		sjme_dylib_extraFamily family,
	sjme_attrInNullable sjme_lpcstr subComponent,
	sjme_attrOutNotNull sjme_dylib* outLib)
{
	sjme_jint i, n;
	sjme_cchar at;
	
	if (outLib == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (family <= 0 || family >= SJME_DYLIB_NUM_EXTRA_FAMILY)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Fallback to default NAL? */
	if (nal == NULL)
		nal = &sjme_nal_default;

	/* If a subcomponent is passed, make sure it does not have a wonky */
	/* set of characters. */
	if (subComponent != NULL)
		for (n = sjme_util_sizeToInt(strlen(subComponent)), i = 0; i < n; i++)
		{
			at = subComponent[i];
			if (at == '\\' || at == '/' || at == ':' || at <= ' ')
				return SJME_ERROR_SECURITY_EXCEPTION;
		}

	/* Which family type to load? */
	switch (family)
	{
		case SJME_DYLIB_EXTRA_FAMILY_SCRITCHUI:
			return sjme_dylib_openExtraScritchAny(nal,
				subComponent, outLib,
				&sjme_dylib_extraUi[0]);

		case SJME_DYLIB_EXTRA_FAMILY_SCRITCHAUDIO:
			return sjme_dylib_openExtraScritchAny(nal,
				subComponent, outLib,
				&sjme_dylib_extraAudio[0]);
		
		default:
			return sjme_error_notImplemented(0);
	}
}
