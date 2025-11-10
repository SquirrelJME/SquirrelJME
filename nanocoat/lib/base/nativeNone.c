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

#pragma region(getenv)
#if (SJME_CONFIG_NAL_GETENV == SJME_CONFIG_NAL_IMPLEMENT_NONE)

sjme_errorCode sjme_nal_default_getEnv(
	sjme_attrInNotNull sjme_attrOutNotNullBuf(len) sjme_lpstr buf,
	sjme_attrInPositiveNonZero sjme_jint bufLen,
	sjme_attrInNotNull sjme_lpcstr env)
{
	if (buf == NULL || env == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (bufLen <= 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;

	return SJME_ERROR_NOT_IMPLEMENTED;
}

#endif
#pragma endregion(getenv)

#pragma region(nanotime)
#if (SJME_CONFIG_NAL_NANOTIME == SJME_CONFIG_NAL_IMPLEMENT_NONE)

sjme_errorCode sjme_nal_default_nanoTime(
	sjme_attrOutNotNull sjme_jlong* result)
{
	if (result == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	return SJME_ERROR_NOT_IMPLEMENTED;
}

#endif
#pragma endregion(nanotime)

#pragma region(seekable)
#if (SJME_CONFIG_NAL_SEEKABLE == SJME_CONFIG_NAL_IMPLEMENT_NONE)

sjme_errorCode sjme_nal_default_fileOpen(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInNotNull sjme_lpcstr inPath,
	sjme_attrOutNotNull sjme_seekable* outSeekable,
	sjme_attrInValue sjme_nal_openMode openMode)
{
	if (allocPool == NULL || inPath == NULL || outSeekable == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (openMode < SJME_NAL_OPEN_READ ||
		openMode > SJME_NAL_OPEN_WRITE_TRUNCATE)
		return SJME_ERROR_INVALID_ARGUMENT;

	return SJME_ERROR_NOT_IMPLEMENTED;
}

#endif
#pragma endregion(seekable)

#pragma region(pipe)
#if (SJME_CONFIG_NAL_PIPE == SJME_CONFIG_NAL_IMPLEMENT_NONE)

sjme_errorCode sjme_nal_default_stdErr(
	sjme_attrInNotNullBuf(len) sjme_cpointer buf,
	sjme_attrInPositive sjme_jint off,
	sjme_attrInPositiveNonZero sjme_jint len)
{
	if (buf == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (off < 0 || len < 0 || (off + len) < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;

	return SJME_ERROR_NOT_IMPLEMENTED;
}

sjme_errorCode sjme_nal_default_stdErrFlush(void)
{
	return SJME_ERROR_NOT_IMPLEMENTED;
}

sjme_errorCode sjme_nal_default_stdOutFlush(void)
{

	return SJME_ERROR_NOT_IMPLEMENTED;
}

sjme_errorCode sjme_nal_default_stdOut(
	sjme_attrInNotNullBuf(len) sjme_cpointer buf,
	sjme_attrInPositive sjme_jint off,
	sjme_attrInPositiveNonZero sjme_jint len)
{
	if (buf == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (off < 0 || len < 0 || (off + len) < 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;

	return SJME_ERROR_NOT_IMPLEMENTED;
}

#endif
#pragma endregion(pipe)
