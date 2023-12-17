/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Debugging helpers.
 * 
 * @since 2023/07/27
 */

#ifndef SQUIRRELJME_DEBUG_H
#define SQUIRRELJME_DEBUG_H

#include <stdarg.h>

#include "sjme/nvm.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_DEBUG_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

#define SJME_DEBUG_DECL_FILE_LINE_FUNC \
	sjme_attrInNullable sjme_lpcstr file, \
	sjme_attrInValue int line, \
	sjme_attrInNullable sjme_lpcstr func

#if defined(SJME_CONFIG_RELEASE)
	/** File, line, and function. */
	#define SJME_DEBUG_FILE_LINE_FUNC NULL, -1, NULL

	/** Only emitted in debugging. */
	#define SJME_ONLY_IN_DEBUG(expr) do {} while(0)
#else
	/** File, line, and function. */
	#define SJME_DEBUG_FILE_LINE_FUNC __FILE__, __LINE__, __func__

	/** Only emitted in debugging. */
	#define SJME_ONLY_IN_DEBUG(expr) expr
#endif

/**
 * Prints a debug message.
 *
 * @param file The file printing from.
 * @param line The line printing from.
 * @param func The function printing from.
 * @param isBlank Is this blank?
 * @param message The @c printf style message.
 * @param ... Any @c printf style arguments.
 * @since 2021/10/31
 */
void sjme_messageR(SJME_DEBUG_DECL_FILE_LINE_FUNC,
	sjme_attrInValue sjme_jboolean isBlank,
	sjme_attrInNullable sjme_attrFormatArg sjme_lpcstr message, ...)
	sjme_attrFormatOuter(4, 5);

#if defined(SJME_CONFIG_DEBUG)
/**
 * Prints a debug message.
 * 
 * @param file The file printing from.
 * @param line The line printing from.
 * @param func The function printing from.
 * @param isBlank Is this blank?
 * @param message The @c printf style message.
 * @param args Any @c printf style arguments.
 * @since 2023/11/11
 */
void sjme_messageV(SJME_DEBUG_DECL_FILE_LINE_FUNC,
	sjme_attrInValue sjme_jboolean isBlank,
	sjme_attrInNullable sjme_attrFormatArg sjme_lpcstr message,
	va_list args);
#endif

/**
 * Prints a debug message
 * 
 * @param message The @c printf style message.
 * @param ... Any @c printf style arguments.
 * @since 2021/10/31 
 */
#define sjme_message(...) SJME_ONLY_IN_DEBUG( \
	sjme_messageR(SJME_DEBUG_FILE_LINE_FUNC, SJME_JNI_FALSE, __VA_ARGS__))

/**
 * Indicates a fatal error and exits the program.
 * 
 * @param file The file printing from.
 * @param line The line printing from.
 * @param func The function printing from.
 * @param message The @c printf style message.
 * @param ... Any @c printf style arguments.
 * @return Never returns.
 * @since 2023/11/11 
 */
sjme_errorCode sjme_dieR(SJME_DEBUG_DECL_FILE_LINE_FUNC,
	sjme_attrInNullable sjme_attrFormatArg sjme_lpcstr message, ...)
	sjme_attrReturnNever sjme_attrFormatOuter(3, 4);

/**
 * Indicates a fatal error and exits the program.
 * 
 * @param message The @c printf style message.
 * @param ... Any @c printf style arguments.
 * @return Never returns.
 * @since 2023/11/11 
 */
#define sjme_die(...) sjme_dieR(SJME_DEBUG_FILE_LINE_FUNC, __VA_ARGS__)

/**
 * Indicates a fatal error and exits the program.
 * 
 * @param message The @c printf style message.
 * @param ... Any @c printf style arguments.
 * @return Never returns.
 * @since 2023/11/11 
 */
#define sjme_dieP(...) ((void*)((intptr_t)sjme_dieR(\
	SJME_DEBUG_FILE_LINE_FUNC, __VA_ARGS__)))

/**
 * Indicates a To-Do and then terminates the program.
 * 
 * @param file The file printing from.
 * @param line The line printing from.
 * @param func The function printing from.
 * @param message The @c printf style message.
 * @param ... Any @c printf style arguments.
 * @return Never returns.
 * @since 2021/02/28 
 */
void sjme_todoR(SJME_DEBUG_DECL_FILE_LINE_FUNC,
	sjme_attrInNullable sjme_attrFormatArg sjme_lpcstr message, ...)
	sjme_attrReturnNever sjme_attrFormatOuter(3, 4);

/**
 * Indicates a To-Do and then terminates the program.
 * 
 * @param message The @c printf style message.
 * @param ... Any @c printf style arguments.
 * @return Never returns.
 * @since 2021/02/28 
 */
#define sjme_todo(...) sjme_todoR(SJME_DEBUG_FILE_LINE_FUNC, __VA_ARGS__)

/**
 * Emits a dangling reference message.
 * 
 * @param fullMessage The message to emit. 
 * @param partMessage Partial message, without any prepend.
 * @return Return @c SJME_JNI_TRUE if the message is handled, otherwise
 * a standard @c fprintf to @c stderr will be used.
 * @since 2023/12/05 
 */
typedef sjme_jboolean (*sjme_danglingMessageFunc)(sjme_lpcstr fullMessage,
	sjme_lpcstr partMessage);

/** The dangling message implementation to use. */
extern sjme_danglingMessageFunc sjme_danglingMessage;

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_DEBUG_H
}
		#undef SJME_CXX_SQUIRRELJME_DEBUG_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_DEBUG_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_DEBUG_H */
