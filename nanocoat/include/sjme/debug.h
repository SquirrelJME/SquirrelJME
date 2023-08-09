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

#if defined(SJME_CONFIG_RELEASE)
	/** File, line, and function. */
	#define SJME_DEBUG_FILE_LINE_FUNC NULL, NULL, NULL

	/** Only emitted in debugging. */
	#define SJME_ONLY_IN_DEBUG(expr)
#else
	/** File, line, and function. */
	#define SJME_DEBUG_FILE_LINE_FUNC __FILE__, __LINE__, __func__

	/** Only emitted in debugging. */
	#define SJME_ONLY_IN_DEBUG(expr) expr
#endif

#if defined(SJME_CONFIG_DEBUG)
/**
 * Prints a debug message.
 * 
 * @param file The file printing from.
 * @param line The line printing from.
 * @param func The function printing from.
 * @param message The @c printf style message.
 * @param ... Any @c printf style arguments.
 * @since 2021/10/31
 */
void sjme_messageR(sjme_attrInNullable const char* file,
	sjme_attrInValue int line,
	sjme_attrInNullable const char* func,
	sjme_attrInNullable sjme_attrFormatArg const char* message, ...)
	sjme_attrFormatOuter(3, 4);
#endif
	
/**
 * Prints a debug message
 * 
 * @param message The @c printf style message.
 * @param ... Any @c printf style arguments.
 * @since 2021/10/31 
 */
#define sjme_message(...) SJME_ONLY_IN_DEBUG( \
	sjme_messageR(SJME_DEBUG_FILE_LINE_FUNC, __VA_ARGS__))

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
void sjme_todoR(sjme_attrInNullable const char* file,
	sjme_attrInValue int line,
	sjme_attrInNullable const char* func,
	sjme_attrInNullable sjme_attrFormatArg const char* message, ...)
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
