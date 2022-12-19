/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Error States and otherwise.
 * 
 * @since 2021/02/28
 */

#ifndef SQUIRRELJME_ERROR_H
#define SQUIRRELJME_ERROR_H

#include "sjmerc.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_ERROR_H
extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/** This represents an error. */
struct sjme_error
{
	/** Error code. */
	sjme_errorCode code;
	
	/** The value of it. */
	sjme_jint value;
	
	/** The source file. */
	const char* sourceFile;
	
	/** The source line. */
	sjme_jint sourceLine;
	
	/** The source function. */
	const char* sourceFunction;
};

/**
 * Clears the error.
 * 
 * @param error The error to clear.
 * @since 2021/03/04
 */
void sjme_clearError(sjme_error* error);

/**
 * Gets an error from the error holder.
 * 
 * @param error The error to read from.
 * @param ifMissing The value to return if missing.
 * @return The error code or @c ifMissing if there is no error.
 * @since 2021/10/09
 */
sjme_errorCode sjme_getError(sjme_error* error, sjme_errorCode ifMissing);

/**
 * Checks if an error is present.
 * 
 * @param error The error to check. 
 * @return If there is an error present or not.
 * @since 2021/03/04
 */
sjme_returnFail sjme_hasError(sjme_error* error);

/**
 * Sets the error code.
 *
 * @param error The error to set.
 * @param code The error code.
 * @param value The error value.
 * @param file The source file.
 * @param line The source line.
 * @param function The source function.
 * @since 2019/06/25
 */
void sjme_setErrorR(sjme_error* error, sjme_errorCode code, sjme_jint value,
	const char* file, int line, const char* function);

/**
 * Sets the error code using the current source position.
 *
 * @param error The error to set.
 * @param code The error code.
 * @param value The error value.
 * @since 2019/06/25
 */
#define sjme_setError(error, code, value) \
	sjme_setErrorR(error, code, value, __FILE__, __LINE__, __func__)

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_ERROR_H
}
#undef SJME_CXX_SQUIRRELJME_ERROR_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_ERROR_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_ERROR_H */
