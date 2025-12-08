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
	sjme_attrInNotNull sjme_lpcstr dylibName,
	sjme_attrOutNotNull sjme_dylib* outLib)
{
	if (nal == NULL || dylibName == NULL || outLib == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

static sjme_errorCode sjme_dylib_openExtraScritchAudio(
	sjme_attrInNullable const sjme_nal* nal,
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
			return sjme_dylib_openExtraScritchUI(nal,
				family, subComponent, outLib);

		case SJME_DYLIB_EXTRA_FAMILY_SCRITCHAUDIO:
			return sjme_dylib_openExtraScritchAudio(nal,
				family, subComponent, outLib);
		
		default:
			return sjme_error_notImplemented(0);
	}
}
