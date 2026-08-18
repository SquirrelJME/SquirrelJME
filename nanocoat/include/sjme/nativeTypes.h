/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Native Abstraction Layer Types which are more shared.
 *
 * @file
 * @since 2025/12/01
 */

#ifndef SJME_C_SQUIRRELJME_NATIVETYPES_H
#define SJME_C_SQUIRRELJME_NATIVETYPES_H

#include "sjme/config.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_NATIVETYPES_H

extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

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
	sjme_errorCode (*close)(void);
	
	/** Reads from the input. */
	sjme_errorCode (*in)(void);

	/** Writes to the output. */
	sjme_nal_stdOFunc out;

	/** Flushes the output stream. */
	sjme_nal_stdIoFlush flush;
} sjme_nal_stdIo;
	
/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_NATIVETYPES_H
}
#undef SJME_CXX_SQUIRRELJME_NATIVETYPES_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_NATIVETYPES_H */
#endif /* #ifdef __cplusplus */

#endif /* SJME_C_SQUIRRELJME_NATIVETYPES_H */
