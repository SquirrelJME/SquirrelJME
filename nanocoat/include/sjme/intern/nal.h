/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Native NAL functions.
 * 
 * @since 2025/11/10
 */

#ifndef SJME_C_SQUIRRELJME_NAL_H
#define SJME_C_SQUIRRELJME_NAL_H

#include "sjme/config.h"
#include "sjme/error.h"
#include "sjme/native.h"
#include "sjme/stream.h"
#include "sjme/intern/nalSelect.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_NAL_H

extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

sjme_errorCode sjme_nal_default_execPath(
	sjme_attrOutNotNullBuf(outLen) sjme_attrOutModify sjme_lpstr out,
	sjme_attrInPositiveNonZero sjme_jint outLen);

sjme_errorCode sjme_nal_default_fileOpen(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInNotNull sjme_lpcstr inPath,
	sjme_attrOutNotNull sjme_seekable* outSeekable,
	sjme_attrInValue sjme_nal_openMode openMode);

sjme_errorCode sjme_nal_default_getEnv(
	sjme_attrInNotNull sjme_attrOutNotNullBuf(len) sjme_lpstr buf,
	sjme_attrInPositiveNonZero sjme_jint bufLen,
	sjme_attrInNotNull sjme_lpcstr env);
	
sjme_errorCode sjme_nal_default_nanoTime(
	sjme_attrOutNotNull sjme_jlong* result);
	
sjme_errorCode sjme_nal_default_pathStyle(
	sjme_attrOutNotNull const sjme_path_style** outStyle);

sjme_errorCode sjme_nal_default_tcpUdp(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrOutNullable sjme_stream_input* netIn,
	sjme_attrOutNullable sjme_stream_output* netOut,
	sjme_attrInValue sjme_jboolean isUdp,
	sjme_attrInValue sjme_jboolean listening,
	sjme_attrInNullable sjme_lpcstr address,
	sjme_attrInRange(0, 65535) sjme_jint port);

sjme_errorCode sjme_nal_default_stdErr(
	sjme_attrInNotNullBuf(len) sjme_cpointer buf,
	sjme_attrInPositive sjme_jint off,
	sjme_attrInPositiveNonZero sjme_jint len);

sjme_errorCode sjme_nal_default_stdErrFlush(void);

sjme_errorCode sjme_nal_default_stdOutFlush(void);

sjme_errorCode sjme_nal_default_stdOut(
	sjme_attrInNotNullBuf(len) sjme_cpointer buf,
	sjme_attrInPositive sjme_jint off,
	sjme_attrInPositiveNonZero sjme_jint len);
	
sjme_errorCode sjme_nal_default_threadSleep(
	sjme_attrInPositive sjme_jint millis,
	sjme_attrInPositive sjme_jint nanos);
	
sjme_errorCode sjme_nal_default_threadYield(void);

sjme_errorCode sjme_nal_default_userHome(
	sjme_attrOutNotNullBuf(outLen) sjme_attrOutModify sjme_lpstr out,
	sjme_attrInPositiveNonZero sjme_jint outLen);

sjme_errorCode sjme_nal_default_userName(
	sjme_attrOutNotNullBuf(outLen) sjme_attrOutModify sjme_lpstr out,
	sjme_attrInPositiveNonZero sjme_jint outLen);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_NAL_H
}
#undef SJME_CXX_SQUIRRELJME_NAL_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_NAL_H */
#endif /* #ifdef __cplusplus */

#endif /* SJME_C_SQUIRRELJME_NAL_H */
