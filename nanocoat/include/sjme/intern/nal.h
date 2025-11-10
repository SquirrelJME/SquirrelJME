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

/** Not implemented. */
#define SJME_CONFIG_NAL_IMPLEMENT_NONE 0

/** Use NAL Standard C implementation. */
#define SJME_CONFIG_NAL_IMPLEMENT_STDC 1

/** Use NAL POSIX implementation. */
#define SJME_CONFIG_NAL_IMPLEMENT_POSIX 2

/** Use Windows 32-bit implementation. */
#define SJME_CONFIG_NAL_IMPLEMENT_WIN32 3

#if !defined(SJME_CONFIG_NAL_GETENV)
	#if defined(SJME_CONFIG_HAS_C89)
		/** Use Standard C getenv implementation. */
		#define SJME_CONFIG_NAL_GETENV SJME_CONFIG_NAL_IMPLEMENT_STDC
	#endif
#endif

#if !defined(SJME_CONFIG_NAL_NANOTIME)
	#if defined(SJME_CONFIG_HAS_OS_LINUX) || defined(SJME_CONFIG_HAS_OS_BSD)
		/** Use POSIX nanotime implementation. */
		#define SJME_CONFIG_NAL_NANOTIME SJME_CONFIG_NAL_IMPLEMENT_POSIX
	#elif defined(SJME_CONFIG_HAS_OS_WINDOWS)
		/** Use Windows nanotime implementation. */
		#define SJME_CONFIG_NAL_NANOTIME SJME_CONFIG_NAL_IMPLEMENT_WIN32
	#endif
#endif

#if !defined(SJME_CONFIG_NAL_PIPE)
	#if !defined(SJME_CONFIG_HAS_NO_STDIO)
		/** Use Standard C file IO for pipes. */
		#define SJME_CONFIG_NAL_PIPE SJME_CONFIG_NAL_IMPLEMENT_STDC
	#endif
#endif

#if !defined(SJME_CONFIG_NAL_SEEKABLE)
	#if !defined(SJME_CONFIG_HAS_NO_STDIO)
		/** Use Standard C file IO for seekables. */
		#define SJME_CONFIG_NAL_SEEKABLE SJME_CONFIG_NAL_IMPLEMENT_STDC
	#endif
#endif

#pragma region(none)

#if !defined(SJME_CONFIG_NAL_GETENV)
	/** Not implemented. */
	#define SJME_CONFIG_NAL_GETENV SJME_CONFIG_NAL_IMPLEMENT_NONE
#endif

#if !defined(SJME_CONFIG_NAL_NANOTIME)
	/** Not implemented. */
	#define SJME_CONFIG_NAL_NANOTIME SJME_CONFIG_NAL_IMPLEMENT_NONE
#endif

#if !defined(SJME_CONFIG_NAL_PIPE)
	/** Not implemented. */
	#define SJME_CONFIG_NAL_PIPE SJME_CONFIG_NAL_IMPLEMENT_NONE
#endif

#if !defined(SJME_CONFIG_NAL_SEEKABLE)
	/** Not implemented. */
	#define SJME_CONFIG_NAL_SEEKABLE SJME_CONFIG_NAL_IMPLEMENT_NONE
#endif

#pragma endregion(none)

#if (SJME_CONFIG_NAL_GETENV == SJME_CONFIG_NAL_IMPLEMENT_WIN32) || \
	(SJME_CONFIG_NAL_NANOTIME == SJME_CONFIG_NAL_IMPLEMENT_WIN32) || \
	(SJME_CONFIG_NAL_SEEKABLE == SJME_CONFIG_NAL_IMPLEMENT_WIN32) || \
	(SJME_CONFIG_NAL_PIPE == SJME_CONFIG_NAL_IMPLEMENT_WIN32)
	/** Has any Windows 32-bit implementation. */
	#define SJME_CONFIG_NAL_HAS_ANY_WIN32
#endif
	
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
