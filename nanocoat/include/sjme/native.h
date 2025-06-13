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

#ifndef SJME_C_NATIVE_H
#define SJME_C_NATIVE_H

#include "nvm/mleConst.h"

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
 * @param allocPool The pool for allocations.
 * @param inPath The path to open.
 * @param outSeekable The seekable to open within.
 * @return Any resultant error, if any.
 * @since 2024/08/11
 */
typedef sjme_errorCode (*sjme_nal_fileOpenFunc)(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
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
 * Flushes the given output stream.
 *
 * @return Any resultant error.
 * @since 2025/03/03
 */
typedef sjme_errorCode (*sjme_nal_stdIoFlush)(void);
	
/**
 * Writes data to a standard output type stream.
 *
 * @param buf The data buffer to write.
 * @param off The offset into the buffer.
 * @param len The number of bytes to write.
 * @return Any resultant error, if any.
 * @since 2025/02/25
 */
typedef sjme_errorCode (*sjme_nal_stdOFunc)(
	sjme_attrInNotNullBuf(len) sjme_cpointer buf,
	sjme_attrInPositive sjme_jint off,
	sjme_attrInPositiveNonZero sjme_jint len);

/**
 * Contains the needed function calls to perform calls to standard streams.
 *
 * @since 2025/03/03
 */
typedef struct sjme_nal_stdIo
{
	/** Close function. */
	sjme_jboolean (*close)(void);
	
	/** Reads from the input. */
	sjme_jboolean (*in)(void);

	/** Writes to the output. */
	sjme_nal_stdOFunc out;

	/** Flushes the output stream. */
	sjme_nal_stdIoFlush flush;
} sjme_nal_stdIo;
	
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

	/** Standard input/output pipes. */
	sjme_nal_stdIo stdIo[SJME_NVM_MLE_NUM_STD_PIPES];
} sjme_nal;

/** Default native abstraction layer. */
extern const sjme_nal sjme_nal_default;

#if !defined(SJME_CONFIG_HAS_NO_ERRNO)

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
