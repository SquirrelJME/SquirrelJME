/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/util.h"
#include "sjme/intern/nal.h"

#pragma region(execPath)
#if (SJME_CONFIG_NAL_EXEC_PATH == SJME_CONFIG_NAL_IMPLEMENT_SOLARIS)

sjme_errorCode sjme_nal_default_execPath(
	sjme_attrOutNotNullBuf(outLen) sjme_attrOutModify sjme_lpstr out,
	sjme_attrInPositiveNonZero sjme_jint outLen)
{
	sjme_lpcstr execPath;
	sjme_jint execLen;

	if (out == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (outLen <= 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;

	/* Get the path and the length. */
	execPath = getexecname();
	if (execPath == NULL)
		return SJME_ERROR_NATIVE_ERROR;

	/* Cannot fit this path? */
	execLen = strlen(execPath);
	if (execLen > outLen)
		return SJME_ERROR_PATH_TOO_LONG;

	/* Copy it out. */
	strncpy(out, execPath, sjme_min(execLen, outLen))

	/* Success! */
	return SJME_ERROR_NONE;
}

#endif
#pragma endregion(execPath)
