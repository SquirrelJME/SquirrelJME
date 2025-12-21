/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/config.h"
#include "sjme/util.h"
#include "sjme/intern/nal.h"

#if (SJME_CONFIG_NAL_EXEC_PATH == SJME_CONFIG_NAL_IMPLEMENT_BSD)
	#include <unistd.h>
#endif

#pragma region(execPath)
#if (SJME_CONFIG_NAL_EXEC_PATH == SJME_CONFIG_NAL_IMPLEMENT_BSD)

sjme_errorCode sjme_nal_default_execPath(
	sjme_attrOutNotNullBuf(outLen) sjme_attrOutModify sjme_lpstr out,
	sjme_attrInPositiveNonZero sjme_jint outLen)
{
	sjme_lpstr temp;
	sjme_jint tempLen, procLen;

	if (out == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (outLen <= 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;

	/* Setup buffer that is slightly larger, to detect small buffers. */
	tempLen = outLen + 16;
	temp = sjme_alloca(sizeof(*temp) * tempLen);
	if (temp == NULL)
		return sjme_error_outOfMemory(NULL, temp);

	/* Clear. */
	memset(temp, 0, sizeof(*temp) * tempLen);

	/* Unfortunately the only way to tell if a path is too long is by */
	/* requesting more than what the user requested. */
	procLen = readlink("/proc/curproc/file", temp, tempLen);
	if (procLen > 0 && procLen < outLen)
		strncpy(out, temp, sjme_min(procLen, outLen));

	/* Cleanup. */
	sjme_alloca_free(temp);

	/* If the path is too long, then fail. */
	if (procLen < 0 || procLen >= outLen)
		return SJME_ERROR_PATH_TOO_LONG;
	return SJME_ERROR_NONE;
}

#endif
#pragma endregion(execPath)
