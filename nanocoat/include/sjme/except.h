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
	/** Pointer to what is above the trace */
	volatile sjme_exceptTrace* parent;

	/** The storage for registers using C's @c setjmp and @c longjmp . */
	volatile jmp_buf jumpBuf;

	/** The file. */
	sjme_lpcstr volatile file;

	/** The line. */
	volatile sjme_jint line;

	/** The function. */
	sjme_lpcstr volatile func;

	/** Bring in error code from previous stack. */
	volatile sjme_errorCode prevError;
};

/** Declare error variable for the error state. */
#define SJME_EXCEPT_VDEF \
    volatile sjme_errorCode exceptTraceE_sjme; \
	volatile sjme_exceptTrace exceptTrace_sjme; \
	volatile sjme_exceptTrace* volatile* exceptTraceVl_sjme

/**
 * Block to declare exception handling start.
 *
 * @param exceptionState A @c sjme_exceptTrace to act on as a pivot for
 * exception storage.
 * @since 2023/12/08
 */
#define SJME_EXCEPT_WITH(exceptionState) \
    do { \
		memset((sjme_pointer)&exceptTrace_sjme, 0, sizeof(exceptTrace_sjme)); \
		exceptTraceE_sjme = SJME_NUM_ERROR_CODES; \
		exceptTrace_sjme.file = __FILE__; \
		exceptTrace_sjme.line = __LINE__; \
		exceptTrace_sjme.func = __func__; \
		exceptTrace_sjme.parent = (exceptionState); \
		(exceptionState) = (sjme_exceptTrace*)&exceptTrace_sjme; \
		exceptTraceVl_sjme = &(exceptionState);\
        exceptTraceE_sjme = \
			setjmp((*((jmp_buf*)(&exceptTrace_sjme.jumpBuf)))); \
    } while(SJME_JNI_FALSE); \
    if (exceptTraceE_sjme != 0) \
	{goto sjme_except_fail; goto sjme_except_with;} \
	sjme_except_with

/** Exception handling with a Java frame. */
#define SJME_EXCEPT_WITH_FRAME \
	SJME_EXCEPT_WITH(frame->inThread->except)

/** Block to declare that exception handling is done and no more. */
#define SJME_EXCEPT_DONE(x) \
	do { \
    	if (exceptTrace_sjme.parent != NULL) \
			exceptTrace_sjme.prevError = exceptTraceE_sjme; \
	} while(SJME_JNI_FALSE); \
    return x

/** Done, return error code. */
#define SJME_EXCEPT_DONE_RETURN_ERROR() \
	SJME_EXCEPT_DONE(exceptTraceE_sjme)

/** Block to declare failing code, for cleanup and return. */
#define SJME_EXCEPT_FAIL \
	sjme_except_fail: \
	do { \
		sjme_except_printStackTraceR(SJME_DEBUG_FILE_LINE_FUNC, \
			exceptTraceE_sjme, *exceptTraceVl_sjme); \
        if (((*exceptTraceVl_sjme)->parent) != NULL) \
        	((*exceptTraceVl_sjme)->parent)->prevError = exceptTraceE_sjme; \
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
		exceptTraceE_sjme = (errorCodeId); \
		longjmp((*((jmp_buf*)(&exceptTrace_sjme.jumpBuf))), \
			(sjme_errorCode)(errorCodeId)); \
		goto sjme_except_fail;} \
	while(SJME_JNI_FALSE)

/** Toss the same error code as before. */
#define SJME_EXCEPT_TOSS_SAME() \
	SJME_EXCEPT_TOSS(exceptTrace_sjme.prevError)

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
	sjme_attrInNotNull sjme_attrFormatArg sjme_lpcstr message, ...)
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
		NULL, exceptTraceE_sjme, __VA_ARGS__)

sjme_errorCode sjme_except_printStackTraceR(SJME_DEBUG_DECL_FILE_LINE_FUNC,
	sjme_errorCode errorCode,
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
