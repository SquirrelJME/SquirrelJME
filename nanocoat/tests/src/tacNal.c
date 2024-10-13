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

#include "test.h"

sjme_errorCode sjme_nal_test_currentTimeMillis(
	sjme_attrOutNotNull sjme_jlong* result)
{
	static sjme_jlong time;

	if (result == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Increment higher time up. */
	time.part.hi++;
	
	/* Copy over. */
	memmove(result, &time, sizeof(time));
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nal_test_fileOpen(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrInNotNull sjme_lpcstr inPath,
	sjme_attrOutNotNull sjme_seekable* outSeekable)
{
	if (inPool == NULL || inPath == NULL || outSeekable == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Filesystem not supported. */
	return SJME_ERROR_FILE_NOT_FOUND;
}

sjme_errorCode sjme_nal_test_getEnv(
	sjme_attrInNotNull sjme_attrOutNotNullBuf(len) sjme_lpstr buf,
	sjme_attrInPositiveNonZero sjme_jint bufLen,
	sjme_attrInNotNull sjme_lpcstr env)
{
	if (buf == NULL || env == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
}

sjme_errorCode sjme_nal_test_nanoTime(
	sjme_attrOutNotNull sjme_jlong* result)
{
	static sjme_jint lowTime;
	sjme_errorCode error;
	sjme_jlong millis;
	
	if (result == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Get millisecond time. */
	if (sjme_error_is(error = sjme_nal_test_currentTimeMillis(
		&millis)))
		return sjme_error_default(error);
	
	/* Copy over time. */
	result->part.hi = millis.part.hi;
	result->part.lo = ++lowTime;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nal_test_stdErrF(
	sjme_attrInNotNull sjme_lpcstr format,
	...)
{
#define BUFSIZE 512
	sjme_errorCode error;
	char buf[BUFSIZE];
	va_list list;
	
	if (format == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Start argument parsing. */
	va_start(list, format);
	error = SJME_ERROR_NONE;
	if (vsnprintf(buf, BUFSIZE, format, list) < 0)
		error = SJME_ERROR_IO_EXCEPTION;
	
	/* End argument parsing. */
	va_end(list);

	/* Emit. */
	sjme_message("E> %s", buf);
	
	/* Success? */
	return error;
#undef BUFSIZE
}

sjme_errorCode sjme_nal_test_stdOutF(
	sjme_attrInNotNull sjme_lpcstr format,
	...)
{
#define BUFSIZE 512
	sjme_errorCode error;
	char buf[BUFSIZE];
	va_list list;
	
	if (format == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
		
	/* Start argument parsing. */
	va_start(list, format);
	error = SJME_ERROR_NONE;
	if (vsnprintf(buf, BUFSIZE, format, list) < 0)
		error = SJME_ERROR_IO_EXCEPTION;
	
	/* End argument parsing. */
	va_end(list);

	/* Emit. */
	sjme_message("O> %s", buf);
	
	/* Success? */
	return error;
#undef BUFSIZE
}

const sjme_nal sjme_nal_test =
{
	.currentTimeMillis = sjme_nal_test_currentTimeMillis,
	.fileOpen = sjme_nal_test_fileOpen,
	.getEnv = sjme_nal_test_getEnv,
	.nanoTime = sjme_nal_test_nanoTime,
	.stdErrF = sjme_nal_test_stdErrF,
	.stdOutF = sjme_nal_test_stdOutF,
};
