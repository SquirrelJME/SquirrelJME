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
 * @return Will return @c code.
 * @since 2019/06/25
 */
sjme_errorCode sjme_setErrorL(sjme_error* error, sjme_errorCode code,
	sjme_jint value, const char* file, int line, const char* function);

/**
 * Sets the error code and returns a boolean.
 *
 * @param returning The error code to return.
 * @param error The error to set.
 * @param code The error code.
 * @param value The error value.
 * @param file The source file.
 * @param line The source line.
 * @param function The source function.
 * @return Return @c returning.
 * @since 2022/12/19
 */
sjme_jboolean sjme_setErrorBL(sjme_jboolean returning, sjme_error* error,
	sjme_errorCode code, sjme_jint value, const char* file, int line,
	const char* function);

/**
 * Sets the error code using the current source position and returns the error
 * code.
 *
 * @param error The error to set.
 * @param code The error code.
 * @param value The error value.
 * @return Returns @c code.
 * @since 2019/06/25
 */
#define sjme_setError(error, code, value) \
	sjme_setErrorL(error, code, value, __FILE__, __LINE__, __func__)

/**
 * Sets the error code using the current source position and returns a boolean.
 *
 * @param returning The boolean value to return.
 * @param error The error to set.
 * @param code The error code.
 * @param value The error value.
 * @return Returns @c returning.
 * @since 2022/12/19
 */
#define sjme_setErrorB(returning, error, code, value) \
	sjme_setErrorBL(returning, error, code, value, __FILE__, __LINE__, \
		__func__)

/**
 * Sets the error code using the current source position and returns
 * @c sjme_false.
 *
 * @param error The error to set.
 * @param code The error code.
 * @param value The error value.
 * @return Returns @c sjme_false.
 * @since 2022/12/19
 */
#define sjme_setErrorF(error, code, value) \
	sjme_setErrorBL(sjme_false, error, code, value, __FILE__, __LINE__, \
		__func__)

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
