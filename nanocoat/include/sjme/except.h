/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Exception handling.
 * 
 * @since 2023/11/14
 */

#ifndef SQUIRRELJME_EXCEPT_H
#define SQUIRRELJME_EXCEPT_H

#include <setjmp.h>
#include "sjme/nvm.h"
#include "sjme/debug.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_EXCEPT_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/** Declare error variable for the error state. */
#define SJME_EXCEPT_VDEF \
	volatile sjme_errorCode errorCode

/** Block to declare exception handling start. */
#define SJME_EXCEPT_WITH \
    errorCode = SJME_ERROR_CODE_NONE; \
    if ((errorCode = setjmp(frame->exceptionPoint)) != 0) \
	{goto sjme_except_fail; goto sjme_except_with;} \
	sjme_except_with

/** Block to declare failing code, for cleanup and return. */
#define SJME_EXCEPT_FAIL \
	sjme_except_fail

/** Fail and jump into exception handler. */
#define SJME_EXCEPT_TOSS(errorCodeId) \
	do {longjmp(frame->exceptionPoint, (sjme_errorCode)(errorCodeId)); \
		goto sjme_except_fail;} \
	while(SJME_JNI_FALSE)

/**
 * Fatal virtual machine error, but graceful death.
 * 
 * @param frame The frame the code is currently in.
 * @param message The message for the output.
 * @param ... Any format parameters.
 * @return Never returns technically.
 * @since 2023/11/14
 */
sjme_jboolean sjme_except_gracefulDeathR(
	SJME_DEBUG_DECL_FILE_LINE_FUNC,
	sjme_attrInNotNull sjme_nvm_frame* frame,
	sjme_attrInRange(SJME_NUM_ERROR_CODES, SJME_ERROR_CODE_NONE)
		sjme_errorCode errorCode,
	sjme_attrInNotNull sjme_attrFormatArg const char* message, ...)
	sjme_attrReturnNever sjme_attrFormatOuter(5, 6);

/**
 * Fatal virtual machine error, but graceful death.
 * 
 * @param message The message for the output.
 * @param ... Any format parameters.
 * @return Never returns technically.
 * @since 2023/11/14
 */
#define sjme_except_gracefulDeath(...) \
	sjme_except_gracefulDeathR(SJME_DEBUG_FILE_LINE_FUNC, \
		(frame), (errorCode), __VA_ARGS__)

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_EXCEPT_H
}
		#undef SJME_CXX_SQUIRRELJME_EXCEPT_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_EXCEPT_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_EXCEPT_H */
