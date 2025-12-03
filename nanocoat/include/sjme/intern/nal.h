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

/** Use Linux implementation. */
#define SJME_CONFIG_NAL_IMPLEMENT_LINUX 4

/** Use Older POSIX implementation. */
#define SJME_CONFIG_NAL_IMPLEMENT_POSIX_OLD 5

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

#if defined(SJME_CONFIG_HAS_OS_WINDOWS) || \
	defined(SJME_CONFIG_HAS_OS_WINDOWS_CE)
	/** Use WinSock. */
	#define SJME_CONFIG_NAL_TCP_UDP SJME_CONFIG_NAL_IMPLEMENT_WIN32
#elif !defined(SJME_CONFIG_HAS_NO_SYS_SOCKET_H) || \
	defined(SJME_CONFIG_HAS_SYS_SOCKET_H)
	#if SJME_CONFIG_POSIX_VERSION_LEAST(SJME_CONFIG_POSIX_VERSION_2001)
		/** Use POSIX 2001 networking. */
		#define SJME_CONFIG_NAL_TCP_UDP SJME_CONFIG_NAL_IMPLEMENT_POSIX
	#else
		/** Use Old POSIX networking. */
		#define SJME_CONFIG_NAL_TCP_UDP SJME_CONFIG_NAL_IMPLEMENT_POSIX_OLD
	#endif
#endif

#if !defined(SJME_CONFIG_NAL_THREAD_SLEEP)
	#if defined(SJME_CONFIG_HAS_OS_POSIX)
		/** Use POSIX implementation of thread sleep. */
		#define SJME_CONFIG_NAL_THREAD_SLEEP SJME_CONFIG_NAL_IMPLEMENT_POSIX
	#elif defined(SJME_CONFIG_HAS_OS_WINDOWS)
		/** Use Windows implementation of thread sleep. */
		#define SJME_CONFIG_NAL_THREAD_SLEEP SJME_CONFIG_NAL_IMPLEMENT_WIN32
	#endif
#endif

#if !defined(SJME_CONFIG_NAL_THREAD_YIELD)
	#if defined(SJME_CONFIG_HAS_OS_LINUX)
		/** Use Linux implementation of thread yield. */
		#define SJME_CONFIG_NAL_THREAD_YIELD SJME_CONFIG_NAL_IMPLEMENT_LINUX
	#elif defined(SJME_CONFIG_HAS_OS_WINDOWS)
		/** Use Windows implementation of thread yield. */
		#define SJME_CONFIG_NAL_THREAD_YIELD SJME_CONFIG_NAL_IMPLEMENT_WIN32
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

#if !defined(SJME_CONFIG_NAL_TCP_UDP)
	/** Not implemented. */
	#define SJME_CONFIG_NAL_TCP_UDP SJME_CONFIG_NAL_IMPLEMENT_NONE
#endif

#if !defined(SJME_CONFIG_NAL_THREAD_SLEEP)
	/** Not implemented. */
	#define SJME_CONFIG_NAL_THREAD_SLEEP SJME_CONFIG_NAL_IMPLEMENT_NONE
#endif

#if !defined(SJME_CONFIG_NAL_THREAD_YIELD)
	/** Not implemented. */
	#define SJME_CONFIG_NAL_THREAD_YIELD SJME_CONFIG_NAL_IMPLEMENT_NONE
#endif

#pragma endregion(none)

#if (SJME_CONFIG_NAL_GETENV == SJME_CONFIG_NAL_IMPLEMENT_WIN32) || \
	(SJME_CONFIG_NAL_NANOTIME == SJME_CONFIG_NAL_IMPLEMENT_WIN32) || \
	(SJME_CONFIG_NAL_PIPE == SJME_CONFIG_NAL_IMPLEMENT_WIN32) || \
	(SJME_CONFIG_NAL_SEEKABLE == SJME_CONFIG_NAL_IMPLEMENT_WIN32) || \
	(SJME_CONFIG_NAL_TCP_UDP == SJME_CONFIG_NAL_IMPLEMENT_WIN32) || \
	(SJME_CONFIG_NAL_THREAD_SLEEP == SJME_CONFIG_NAL_IMPLEMENT_WIN32) || \
	(SJME_CONFIG_NAL_THREAD_YIELD == SJME_CONFIG_NAL_IMPLEMENT_WIN32)
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
