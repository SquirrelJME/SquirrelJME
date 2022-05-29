/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Anything to do with debugging.
 * 
 * @since 2021/02/27
 */

#ifndef SQUIRRELJME_DEBUG_H
#define SQUIRRELJME_DEBUG_H

#include "sjmerc.h"

/*--------------------------------------------------------------------------*/

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
void sjme_messageR(const char* file, int line,
	const char* func, const char* message, ...);
	
/**
 * Prints a debug message
 * 
 * @param message The @c printf style message.
 * @param ... Any @c printf style arguments.
 * @since 2021/10/31 
 */
#define sjme_message(...) sjme_messageR(__FILE__, __LINE__, __func__, \
	__VA_ARGS__)

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
sjme_returnNever sjme_todoR(const char* file, int line,
	const char* func, const char* message, ...);

/**
 * Indicates a To-Do and then terminates the program.
 * 
 * @param message The @c printf style message.
 * @param ... Any @c printf style arguments.
 * @return Never returns.
 * @since 2021/02/28 
 */
#define sjme_todo(...) sjme_todoR(__FILE__, __LINE__, __func__, __VA_ARGS__)

/*--------------------------------------------------------------------------*/

#endif /* SQUIRRELJME_DEBUG_H */
