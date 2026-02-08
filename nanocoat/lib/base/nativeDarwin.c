/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/config.h"
#include "sjme/intern/nal.h"

#if (SJME_CONFIG_NAL_EXEC_PATH == SJME_CONFIG_NAL_IMPLEMENT_MACOS)
	#include <mach-o/dyld.h>
#endif

#pragma region(execPath)
#if (SJME_CONFIG_NAL_EXEC_PATH == SJME_CONFIG_NAL_IMPLEMENT_MACOS)

sjme_errorCode sjme_nal_default_execPath(
	sjme_attrOutNotNullBuf(outLen) sjme_attrOutModify sjme_lpstr out,
	sjme_attrInPositiveNonZero sjme_jint outLen)
{
	uint32_t bufLen;

	if (out == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (outLen <= 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;

	/* The output path is only written when the buffer is large enough. */
	bufLen = outLen;
	if (_NSGetExecutablePath(out, &bufLen) < 0)
		return SJME_ERROR_PATH_TOO_LONG;

	/* Success! */
	return SJME_ERROR_NONE;
}

#endif
#pragma endregion(execPath)
