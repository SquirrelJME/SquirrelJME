/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>
#include <stdio.h>

#include "sjme/config.h"
#include "sjme/native.h"
#include "sjme/intern/nal.h"

const sjme_nal sjme_nal_default =
{
	sjme_sm(.currentTimeMillis, NULL),
	sjme_sm(.fileOpen, sjme_nal_default_fileOpen),
	sjme_sm(.getEnv, sjme_nal_default_getEnv),
	sjme_sm(.nanoTime, sjme_nal_default_nanoTime),
	sjme_sm(.tcpUdp, sjme_nal_default_tcpUdp),
	sjme_sm(.threadSleep, sjme_nal_default_threadSleep),
	sjme_sm(.threadYield, sjme_nal_default_threadYield),
	{
		{
			sjme_sm(.close, NULL),
			sjme_sm(.in, NULL),
			sjme_sm(.out, NULL),
			sjme_sm(.flush, NULL),
		},
		{
			sjme_sm(.close, NULL),
			sjme_sm(.in, NULL),
			sjme_sm(.out, sjme_nal_default_stdOut),
			sjme_sm(.flush, sjme_nal_default_stdOutFlush),
		},
		{
			sjme_sm(.close, NULL),
			sjme_sm(.in, NULL),
			sjme_sm(.out, sjme_nal_default_stdErr),
			sjme_sm(.flush, sjme_nal_default_stdErrFlush),
		},
	},
};

#if !defined(SJME_CONFIG_HAS_NO_ERRNO)
sjme_errorCode sjme_nal_errno(sjme_jint errNum)
{
	switch (errNum)
	{
		case EIO:
			return SJME_ERROR_IO_EXCEPTION;
		
		case ENOENT:
			return SJME_ERROR_FILE_NOT_FOUND;

		case ECONNREFUSED:
			return SJME_ERROR_CONNECTION_REFUSED;

		default:
			return SJME_ERROR_UNKNOWN;
	}
}
#endif

sjme_errorCode sjme_nal_stdF(
	sjme_attrInNotNull sjme_nal_stdOFunc outFunc,
	sjme_attrInNotNull sjme_lpcstr format,
	...)
{
	sjme_errorCode error;
#if !defined(SJME_CONFIG_HAS_NO_STDIO)
#define BUF_SIZE 512
	va_list list;
	sjme_cchar buf[BUF_SIZE];
#endif
	
	if (outFunc == NULL || format == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
#if !defined(SJME_CONFIG_HAS_NO_STDIO)
	/* Start argument parsing. */
	va_start(list, format);
	
	/* Print to buffer */
	error = SJME_ERROR_NONE;
	memset(buf, 0, sizeof(buf));
	if (vsnprintf(buf, BUF_SIZE - 1, format, list) < 0)
		error = SJME_ERROR_IO_EXCEPTION;
	buf[BUF_SIZE - 1] = '\0';
		
	/* End argument parsing. */
	va_end(list);
#else
	error = SJME_ERROR_NONE;
#endif

	/* Send to the output. */
	return outFunc(buf, 0, strlen(buf));
#if !defined(SJME_CONFIG_HAS_NO_STDIO)
#undef BUF_SIZE
#endif
}
