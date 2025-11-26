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

#define TAC_BUF_SIZE 2048

static const sjme_cchar tacBuf[TAC_BUF_SIZE];

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
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInNotNull sjme_lpcstr inPath,
	sjme_attrOutNotNull sjme_seekable* outSeekable,
	sjme_attrInValue sjme_nal_openMode openMode)
{
	if (allocPool == NULL || inPath == NULL || outSeekable == NULL)
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

	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
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

sjme_errorCode sjme_nal_test_stdErr(
	sjme_attrInNotNullBuf(len) sjme_cpointer buf,
	sjme_attrInPositive sjme_jint off,
	sjme_attrInPositiveNonZero sjme_jint len)
{
	sjme_jint i;
	sjme_cchar c;
	
	if (buf == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (off < 0 || len < 0 || ((sjme_intPointer)buf + len) < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;

	fwrite(SJME_POINTER_OFFSET(buf, off), 1, len, stdout);
	fflush(stdout);
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nal_test_stdOut(
	sjme_attrInNotNullBuf(len) sjme_cpointer buf,
	sjme_attrInPositive sjme_jint off,
	sjme_attrInPositiveNonZero sjme_jint len)
{
	return sjme_nal_test_stdErr(buf, off, len);
}

const sjme_nal sjme_nal_test =
{
	.currentTimeMillis = sjme_nal_test_currentTimeMillis,
	.fileOpen = sjme_nal_test_fileOpen,
	.getEnv = sjme_nal_test_getEnv,
	.nanoTime = sjme_nal_test_nanoTime,
	{
		{
		},
		{
			.out = sjme_nal_test_stdOut,
		},
		{
			.out = sjme_nal_test_stdErr,
		},
	}
};
