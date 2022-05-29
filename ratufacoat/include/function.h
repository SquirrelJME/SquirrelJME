/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Types for functions.
 * 
 * @since 2022/03/09
 */

#ifndef SQUIRRELJME_FUNCTION_H
#define SQUIRRELJME_FUNCTION_H

#include "sjmerc.h"

/*--------------------------------------------------------------------------*/

/**
 * This represents an integer function which may be called to perform an
 * operation. It is unspecified whether the function produces any side effects
 * and stores values in memory.
 * 
 * @param proxy The proxy value which may be acted upon.
 * @param data The data pointer, which may be anything.
 * @param index The index of the item being touched.
 * @param value The value of the given index.
 * @param error Any resultant error state.
 * @return If the mapping was a complete success.
 * @since 2022/03/09
 */
typedef sjme_jboolean (*sjme_integerFunction)(void* proxy, void* data,
	sjme_jint index, sjme_jint value, sjme_error* error);

/*--------------------------------------------------------------------------*/

#endif /* SQUIRRELJME_FUNCTION_H */
