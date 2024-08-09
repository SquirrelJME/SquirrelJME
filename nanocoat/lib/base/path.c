/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/path.h"
#include "sjme/debug.h"

sjme_errorCode sjme_path_resolveAppend(
	sjme_attrOutNotNull sjme_lpstr outPath,
	sjme_attrInPositiveNonZero sjme_jint outPathLen,
	sjme_attrInNotNull sjme_lpcstr subPath)
{
	if (outPath == NULL || subPath == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (outPathLen <= 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	return sjme_error_notImplemented(0);
}
