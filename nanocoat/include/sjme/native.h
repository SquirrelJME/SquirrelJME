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
 * @file
 * @since 2023/07/29
 */

#ifndef SJME_C_NATIVE_H
#define SJME_C_NATIVE_H

#include "sjme/stdTypes.h"
#include "nvm/mleConst.h"
#include "sjme/error.h"
#include "sjme/seekable.h"
#include "sjme/stream.h"
#include "sjme/nativeTypes.h"

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
 * The native file open mode.
 *
 * @since 2025/07/10
 */
typedef enum sjme_nal_openMode
{
	/** Read. */
	SJME_NAL_OPEN_READ = 1,

	/** Write existing file. */
	SJME_NAL_OPEN_WRITE_EXIST = 2,

	/** Write and truncate file. */
	SJME_NAL_OPEN_WRITE_TRUNCATE = 3,
} sjme_nal_openMode;
	
/**
 * Returns the current time in milliseconds as per the Java
 * method @c System::currentTimeMillis() .
 * 
 * @param result The resultant time.
 * @return any resultant error code.
 * @since 2023/05/23
 */
typedef sjme_errorCode (*sjme_nal_currentTimeMillisFunc)(
	sjme_attrOutNotNull sjme_jlong* result);

/**
 * Opens the given file natively.
 * 
 * @param allocPool The pool for allocations.
 * @param inPath The path to open.
 * @param outSeekable The seekable to open within.
 * @param openMode The open mode for the file.
 * @return Any resultant error, if any.
 * @since 2024/08/11
 */
typedef sjme_errorCode (*sjme_nal_fileOpenFunc)(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInNotNull sjme_lpcstr inPath,
	sjme_attrOutNotNull sjme_seekable* outSeekable,
	sjme_attrInValue sjme_nal_openMode openMode);

/**
 * Reads from the system environment a variable.
 * 
 * @param buf The output buffer.
 * @param bufLen The length of the buffer to store within.
 * @param env The environment variable to lookup.
 * @return Any resultant error code. Will
 * return @link SJME_ERROR_NO_SUCH_ELEMENT @endlink
 * if there is no environment variable with the given name.
 * @since 2023/08/05
 */
typedef sjme_errorCode (*sjme_nal_getEnvFunc)(
	sjme_attrInNotNull sjme_attrOutNotNullBuf(len) sjme_lpstr buf,
	sjme_attrInPositiveNonZero sjme_jint bufLen,
	sjme_attrInNotNull sjme_lpcstr env); 

/**
 * Returns the current nanosecond monotonic class as per the Java
 * method @c System::nanoTime() .
 * 
 * @param result The resultant time.
 * @return Any resultant error code.
 * @since 2023/05/23
 */
typedef sjme_errorCode (*sjme_nal_nanoTimeFunc)(
	sjme_attrOutNotNull sjme_jlong* result);

/**
 * Binds and opens a stream to the given TCP/UDP remote host somewhere on
 * a network. 
 * 
 * @param allocPool The allocation pool to use.
 * @param netIn The stream for reading input data from the remote host.
 * @param netOut The stream for writing output data to the remote host.
 * @param isUdp Should this be a UDP connection?
 * @param listening Is this listening for a connection?
 * @param address The address to connect to or to bind to.
 * @param port The port to connect to or to bind to.
 * @return Any resultant error, if any.
 * @since 2025/09/07
 */
typedef sjme_errorCode (*sjme_nal_tcpUdpFunc)(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrOutNullable sjme_stream_input* netIn,
	sjme_attrOutNullable sjme_stream_output* netOut,
	sjme_attrInValue sjme_jboolean isUdp,
	sjme_attrInValue sjme_jboolean listening,
	sjme_attrInNullable sjme_lpcstr address,
	sjme_attrInRange(0, 65535) sjme_jint port);
	
/**
 * Sleeps for the given time duration.
 * 
 * @param millis The number of milliseconds to sleep for.
 * @param nanos The number of nanoseconds to sleep for.
 * @return Any resultant error, if any.
 * @since 2025/11/26
 */
typedef sjme_errorCode (*sjme_nal_threadSleepFunc)(
	sjme_attrInPositive sjme_jint millis,
	sjme_attrInPositive sjme_jint nanos);
	
/**
 * Yields execution.
 * 
 * @return Any resultant error, if any.
 * @since 2025/11/26
 */
typedef sjme_errorCode (*sjme_nal_threadYieldFunc)(void);
	
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

	/** Opens a network socket. */
	sjme_nal_tcpUdpFunc tcpUdp;
	
	/** Sleep the current thread. */
	sjme_nal_threadSleepFunc threadSleep;
	
	/** Yield the current thread. */
	sjme_nal_threadYieldFunc threadYield;

	/** Standard input/output pipes. */
	sjme_nal_stdIo stdIo[SJME_NVM_MLE_NUM_STD_PIPES];
} sjme_nal;

/** Default native abstraction layer. */
extern const sjme_nal sjme_nal_default;

#if !defined(SJME_CONFIG_HAS_NO_ERRNO_H)

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

/**
 * Writes to the given output.
 * 
 * @param outFunc The output function.
 * @param format The format specifier.
 * @param ... The format data.
 * @return On any resultant error, if any.
 * @since 2025/02/24
 */
sjme_errorCode sjme_nal_stdF(
	sjme_attrInNotNull sjme_nal_stdOFunc outFunc,
	sjme_attrInNotNull sjme_lpcstr format,
	...);
	
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
