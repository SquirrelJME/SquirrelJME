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
#include "sjme/path.h"


#pragma region(execPath)
#if (SJME_CONFIG_NAL_EXEC_PATH == SJME_CONFIG_NAL_IMPLEMENT_NONE)

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
	
	/* Not available here. */
	return SJME_ERROR_NOT_IMPLEMENTED;
}

#endif
#pragma endregion(execPath)

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

#pragma region(pathStyle)
#if (SJME_CONFIG_NAL_PATH_STYLE == SJME_CONFIG_NAL_IMPLEMENT_NONE)

sjme_errorCode sjme_nal_default_pathStyle(
	sjme_attrOutNotNull const sjme_path_style** outStyle)
{
	if (outStyle == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

#if defined(SJME_CONFIG_HAS_OS_PC_DOS)
	*outStyle = &sjme_path_styles[SJME_PATH_STYLE_DOS];
#else
	*outStyle = &sjme_path_styles[SJME_PATH_STYLE_NONE];
#endif
	return SJME_ERROR_NONE;
}

#endif
#pragma endregion(pathStyle)

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

#pragma region(tcpUdp)
#if (SJME_CONFIG_NAL_TCP_UDP == SJME_CONFIG_NAL_IMPLEMENT_NONE)

sjme_errorCode sjme_nal_default_tcpUdp(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrOutNullable sjme_stream_input* netIn,
	sjme_attrOutNullable sjme_stream_output* netOut,
	sjme_attrInValue sjme_jboolean isUdp,
	sjme_attrInValue sjme_jboolean listening,
	sjme_attrInNullable sjme_lpcstr address,
	sjme_attrInRange(0, 65535) sjme_jint port)
{
	if (allocPool == NULL || (netIn == NULL && netOut == NULL) ||
		(!listening && address == NULL))
		return SJME_ERROR_NULL_ARGUMENTS;

	if (port < 1 || port > 65535)
		return SJME_ERROR_INVALID_ARGUMENT;

	return sjme_error_notImplemented(0);
}

#endif
#pragma endregion(tcpUdp)

#pragma region(threadSleep)
#if (SJME_CONFIG_NAL_THREAD_SLEEP == SJME_CONFIG_NAL_IMPLEMENT_NONE)

sjme_errorCode sjme_nal_default_threadSleep(
	sjme_attrInPositive sjme_jint millis,
	sjme_attrInPositive sjme_jint nanos)
{
	return SJME_ERROR_NONE;
}

#endif
#pragma endregion(threadSleep)

#pragma region(threadYield)
#if (SJME_CONFIG_NAL_THREAD_YIELD == SJME_CONFIG_NAL_IMPLEMENT_NONE)
	
sjme_errorCode sjme_nal_default_threadYield(void)
{
	return SJME_ERROR_NONE;
}

#endif
#pragma endregion(threadYield)

#pragma region(userHome)
#if (SJME_CONFIG_NAL_USER_HOME == SJME_CONFIG_NAL_IMPLEMENT_NONE)

sjme_errorCode sjme_nal_default_userHome(
	sjme_attrOutNotNullBuf(outLen) sjme_attrOutModify sjme_lpstr out,
	sjme_attrInPositiveNonZero sjme_jint outLen)
{
	if (out == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (outLen <= 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;

	/* Not supported. */
	return SJME_ERROR_NOT_IMPLEMENTED;
}

#endif
#pragma endregion(userHome)

#pragma region(userName)
#if (SJME_CONFIG_NAL_USER_NAME == SJME_CONFIG_NAL_IMPLEMENT_NONE)

sjme_errorCode sjme_nal_default_userName(
	sjme_attrOutNotNullBuf(outLen) sjme_attrOutModify sjme_lpstr out,
	sjme_attrInPositiveNonZero sjme_jint outLen)
{
	if (out == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (outLen <= 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;

	/* Not user login. */
	return SJME_ERROR_NO_USER_LOGIN;
}

#endif
#pragma endregion(userName)
