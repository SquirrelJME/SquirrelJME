/* -*- Mode: C++; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * RetroArch Cheats.
 *
 * @since 2021/02/27
 */

#include "frontend/libretro/lrlocal.h"

/** Reset cheat. */
SJME_EXTERN_C void retro_cheat_reset(void)
{
}

/** Set cheat. */
SJME_EXTERN_C void retro_cheat_set(unsigned index, bool enabled, const char* code)
{
}
