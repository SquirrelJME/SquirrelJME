/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Native Shelf Abstraction (NAL).
 * 
 * @since 2023/07/29
 */

#ifndef SQUIRRELJME_NATIVE_H
#define SQUIRRELJME_NATIVE_H

#include "sjme/stdTypes.h"
#include "sjme/error.h"
#include "sjme/seekable.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_NATIVE_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Returns the current time in milliseconds as per the Java
 * method @c System::currentTimeMillis() .
 * 
 * @param result The resultant time.
 * @return any resultant error code.
 * @since 2023/05/23
 */
typedef sjme_errorCode (*sjme_nal_currentTimeMillisFunc)(
	sjme_attrOutNotNull sjme_jlong* result)
	sjme_attrCheckReturn;

/**
 * Opens the given file natively.
 * 
 * @param inPool The pool for allocations.
 * @param inPath The path to open.
 * @param outSeekable The seekable to open within.
 * @return Any resultant error, if any.
 * @since 2024/08/11
 */
typedef sjme_errorCode (*sjme_nal_fileOpenFunc)(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrInNotNull sjme_lpcstr inPath,
	sjme_attrOutNotNull sjme_seekable* outSeekable);

/**
 * Reads from the system environment a variable.
 * 
 * @param buf The output buffer.
 * @param bufLen The length of the buffer to store within.
 * @param env The environment variable to lookup.
 * @return Any resultant error code. Will return @c SJME_ERROR_NO_SUCH_ELEMENT
 * if there is no environment variable with the given name.
 * @since 2023/08/05
 */
typedef sjme_errorCode (*sjme_nal_getEnvFunc)(
	sjme_attrInNotNull sjme_attrOutNotNullBuf(len) sjme_lpstr buf,
	sjme_attrInPositiveNonZero sjme_jint bufLen,
	sjme_attrInNotNull sjme_lpcstr env)
	sjme_attrCheckReturn; 

/**
 * Returns the current nanosecond monotonic class as per the Java
 * method @c System::nanoTime() .
 * 
 * @param result The resultant time.
 * @return Any resultant error code.
 * @since 2023/05/23
 */
typedef sjme_errorCode (*sjme_nal_nanoTimeFunc)(
	sjme_attrOutNotNull sjme_jlong* result)
	sjme_attrCheckReturn;

/**
 * Formatted text to standard stream.
 * 
 * @param format The format string.
 * @param ... Format arguments.
 * @return Any resultant error code.
 * @since 2024/08/08
 */
typedef sjme_errorCode (*sjme_nal_stdFFunc)(
	sjme_attrInNotNull sjme_lpcstr format,
	...);

/**
 * Native Abstraction Layer functions.
 * 
 * @since 2023/07/29
 */
typedef struct sjme_nal
{
	/** Current time in milliseconds. */
	sjme_nal_currentTimeMillisFunc currentTimeMillis;
	
	/** Opens a given native file. */
	sjme_nal_fileOpenFunc fileOpen;
	
	/** Get environment variable. */
	sjme_nal_getEnvFunc getEnv;
	
	/** Get the current monotonic nanosecond time. */
	sjme_nal_nanoTimeFunc nanoTime;
	
	/** Formatted output to standard error. */
	sjme_nal_stdFFunc stdErrF;
	
	/** Formatted output to standard output. */
	sjme_nal_stdFFunc stdOutF;
} sjme_nal;

/** Default native abstraction layer. */
extern const sjme_nal sjme_nal_default;

#if !defined(SJME_CONFIG_MISSING_ERRNO)

/**
 * Maps @c errno to a SquirrelJME error.
 * 
 * @param errNum The error number. 
 * @return The resultant error.
 * @since 2024/08/11
 */
sjme_errorCode sjme_nal_errno(sjme_jint errNum);

#else

/**
 * Maps @c errno to a SquirrelJME error.
 * 
 * @param errNum The error number. 
 * @return The resultant error.
 * @since 2024/08/11
 */
#define sjme_nal_errno(ignored) ((sjme_errorCode)(SJME_ERROR_NATIVE_ERROR))

#endif

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_NATIVE_H
}
		#undef SJME_CXX_SQUIRRELJME_NATIVE_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_NATIVE_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_NATIVE_H */
