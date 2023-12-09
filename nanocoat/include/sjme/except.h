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

struct sjme_exceptTrace
{
	/** The error code used. */
	sjme_errorCode error;

	/** Pointer to what is above the trace */
	volatile sjme_exceptTrace* parent;

	/** The storage for registers using C's @c setjmp and @c longjmp . */
	jmp_buf jumpBuf;

	/** The file. */
	const char* file;

	/** The line. */
	sjme_jint line;

	/** The function. */
	const char* func;
};

/** Declare error variable for the error state. */
#define SJME_EXCEPT_VDEF \
	volatile sjme_exceptTrace exceptTrace_sjme; \
	volatile sjme_exceptTrace* volatile* exceptTraceVl_sjme

/**
 * Block to declare exception handling start.
 *
 * @param x What to act on as a pivot for exception storage.
 * @since 2023/12/08
 */
#define SJME_EXCEPT_WITH(x) \
    do { \
		memset((void*)&exceptTrace_sjme, 0, sizeof(exceptTrace_sjme)); \
		exceptTrace_sjme.error = SJME_ERROR_NONE; \
		exceptTrace_sjme.file = __FILE__; \
		exceptTrace_sjme.line = __LINE__; \
		exceptTrace_sjme.func = __func__; \
		exceptTrace_sjme.parent = (x); \
		(x) = &exceptTrace_sjme; \
		exceptTraceVl_sjme = &(x); \
    } while(SJME_JNI_FALSE); \
    if ((exceptTrace_sjme.error = \
		setjmp((*((jmp_buf*)(&exceptTrace_sjme.jumpBuf)))) != 0)) \
	{goto sjme_except_fail; goto sjme_except_with;} \
	sjme_except_with

/** Exception handling with a Java frame. */
#define SJME_EXCEPT_WITH_FRAME \
	SJME_EXCEPT_WITH(frame->inThread->except)

/** Block to declare failing code, for cleanup and return. */
#define SJME_EXCEPT_FAIL \
	do { \
		sjme_except_fail: \
		sjme_except_printStackTraceR(SJME_DEBUG_FILE_LINE_FUNC, \
			*exceptTraceVl_sjme); \
        (*exceptTraceVl_sjme) = (*exceptTraceVl_sjme)->parent; \
        exceptTraceVl_sjme = NULL; \
		goto sjme_except_failVl;} \
    while(SJME_JNI_FALSE); \
	sjme_except_failVl

/** Fail and jump into exception handler. */
#define SJME_EXCEPT_TOSS(errorCodeId) \
	do { \
		exceptTrace_sjme.file = __FILE__; \
        exceptTrace_sjme.line = __LINE__; \
        exceptTrace_sjme.func = __func__; \
        exceptTrace_sjme.error = (errorCodeId); \
		longjmp((*((jmp_buf*)(&exceptTrace_sjme.jumpBuf))), \
			(sjme_errorCode)(errorCodeId)); \
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
sjme_errorCode sjme_except_gracefulDeathR(
	SJME_DEBUG_DECL_FILE_LINE_FUNC,
	sjme_attrInNotNull sjme_nvm_frame* frame,
	sjme_attrInRange(SJME_NUM_ERROR_CODES, SJME_ERROR_NONE)
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
		NULL, exceptTrace_sjme.error, __VA_ARGS__)

sjme_errorCode sjme_except_printStackTraceR(SJME_DEBUG_DECL_FILE_LINE_FUNC,
	volatile sjme_exceptTrace* exceptTrace);

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
