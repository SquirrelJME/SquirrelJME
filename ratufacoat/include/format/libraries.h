/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Support for multiple libraries at once.
 * 
 * @since 2021/09/12
 */

#ifndef SQUIRRELJME_LIBRARIES_H
#define SQUIRRELJME_LIBRARIES_H

#include "sjmerc.h"
#include "error.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_LIBRARIES_H
extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * This represents a library driver that is available for usage.
 * 
 * @since 2021/09/12
 */
typedef struct sjme_libraries_driver
{
	/**
	 * Looks at the data and determines if it is appropriate for the given
	 * type of driver.
	 * 
	 * @param data The pointer to the data section. 
	 * @param size The length of the data section.
	 * @param error Output for errors.
	 * @return If this is a valid driver for the given data.
	 * @since 2021/09/12
	 */
	sjme_jboolean (*detect)(void* data, sjme_jint size, sjme_error* error);
} sjme_libraries_driver;

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_LIBRARIES_H
}
#undef SJME_CXX_SQUIRRELJME_LIBRARIES_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_LIBRARIES_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_LIBRARIES_H */
