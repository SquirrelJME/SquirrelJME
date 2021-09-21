/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Detection helpers.
 * 
 * @since 2021/09/12
 */

#ifndef SQUIRRELJME_DETECT_H
#define SQUIRRELJME_DETECT_H

#include "sjmerc.h"
#include "error.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_DETECT_H
extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

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
typedef sjme_jboolean (*sjme_formatDetectFunction)(const void* data,
	sjme_jint size, sjme_error* error);

/**
 * Runs detection on multiple drivers to see if the given block of data meets
 * the criteria for that driver.
 * 
 * @param data The data block.
 * @param size The size of the data block.
 * @param outDriver The output driver, if one was found.
 * @param choices The possible driver choices.
 * @param offsetOfDetect The offset of the {@code sjme_formatDetectFunction}
 * member. 
 * @param error The resultant error.
 * @return If a format was successfully detected or not.
 * @since 2021/09/19
 */
sjme_jboolean sjme_detectFormat(const void* data, sjme_jint size,
	const void** outDriver, const void (**choices), sjme_jint offsetOfDetect,
	sjme_error* error);

/**
 * Checks if the given magic number matches for a given block of data.
 * 
 * @param data The data pointer.
 * @param size The length of the pointer.
 * @param wantMagic The magic number to detect.
 * @param error The error state.
 * @return If this magic number was detected.
 * @since 2021/09/12
 */
sjme_jboolean sjme_detectMagicNumber(const void* data, sjme_jint size,
	sjme_jint wantMagic, sjme_error* error);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_DETECT_H
}
#undef SJME_CXX_SQUIRRELJME_DETECT_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_DETECT_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_DETECT_H */
