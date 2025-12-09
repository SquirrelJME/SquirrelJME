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

static sjme_errorCode sjme_dylib_attemptOpen(
	sjme_attrInNullable const sjme_nal* nal,
	sjme_attrInNotNull sjme_lpcstr dylibName,
	sjme_attrOutNotNull sjme_dylib* outLib)
{
	if (nal == NULL || dylibName == NULL || outLib == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

static sjme_errorCode sjme_dylib_openExtraScritchAny(
	sjme_attrInNullable const sjme_nal* nal,
	sjme_attrInRange(0, SJME_DYLIB_NUM_EXTRA_FAMILY)
		sjme_dylib_extraFamily family,
	sjme_attrInNullable sjme_lpcstr subComponent,
	sjme_attrOutNotNull sjme_dylib* outLib,
	sjme_attrInNotNull const sjme_lpcstr* order)
{
	sjme_errorCode error;
	sjme_jint i;
	sjme_lpcstr orderComponent;
	sjme_cchar tryPath[SJME_MAX_PATH];
	sjme_cchar dylibName[SJME_MAX_PATH];
	
	if (nal == NULL || outLib == NULL || order == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Go through all components, use the passed component, if any. */
	for (i = (subComponent != NULL ? -1 : 0);; i++)
	{
		/* Stop when all possible components have been checked. */
		orderComponent = (i < 0 ? subComponent : order[i]);
		if (orderComponent == NULL)
			break;

		/* Determine base path to use. */
		memset(tryPath, 0, sizeof(tryPath));
		if (sjme_error_is(error = sjme_path_default(nal, -1,
			SJME_NVM_DEFAULT_DIRECTORY_NATIVES,
			tryPath, 0, SJME_MAX_PATH - 1)))
			return sjme_error_default(error);

		/* Determine dynamic library name. */
		memset(dylibName, 0, sizeof(dylibName));
		if (sjme_error_is(error = sjme_dylib_name(
			"squirreljme-scritch", orderComponent,
			dylibName, SJME_MAX_PATH - 1)))
			return sjme_error_default(error);
		
		sjme_todo("Impl?");
		return sjme_error_notImplemented(0);
	}
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_dylib_openExtra(
	sjme_attrInNullable const sjme_nal* nal,
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
			return sjme_dylib_openExtraScritchAny(nal,
				family, subComponent, outLib,
				&sjme_dylib_extraUi[0]);

		case SJME_DYLIB_EXTRA_FAMILY_SCRITCHAUDIO:
			return sjme_dylib_openExtraScritchAny(nal,
				family, subComponent, outLib,
				&sjme_dylib_extraAudio[0]);
		
		default:
			return sjme_error_notImplemented(0);
	}
}
