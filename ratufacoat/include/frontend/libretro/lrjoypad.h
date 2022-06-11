/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Joypad setup.
 * 
 * @since 2022/01/02
 */

#ifndef SQUIRRELJME_LRJOYPAD_H
#define SQUIRRELJME_LRJOYPAD_H

#include "lrlocal.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_LRJOYPAD_H
extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/** The port the joypad is on. */
#define SJME_LIBRETRO_JOYPAD_PORT 0

/** The port the pointer is on. */
#define SJME_LIBRETRO_POINTER_PORT 1

/** The port the light gun is on. */
#define SJME_LIBRETRO_LIGHTGUN_PORT 2

/**
 * Initializes the JoyPad environment set.
 * 
 * @param callback The callback to use for initialization.
 * @since 2022/01/02 
 */
void sjme_libRetro_joyPadSetEnvironment(retro_environment_t callback);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_LRJOYPAD_H
}
#undef SJME_CXX_SQUIRRELJME_LRJOYPAD_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_LRJOYPAD_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_LRJOYPAD_H */
