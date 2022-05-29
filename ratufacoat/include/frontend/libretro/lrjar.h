/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Jar Support.
 * 
 * @since 2022/01/02
 */

#ifndef SQUIRRELJME_LRJAR_H
#define SQUIRRELJME_LRJAR_H

#include "engine/scaffold.h"
#include "sjmerc.h"

/*--------------------------------------------------------------------------*/

/**
 * Determines the ROM to use.
 * 
 * @param config The output configuration.
 * @since 2022/01/02
 */
sjme_jboolean sjme_libRetro_selectRom(sjme_engineConfig* config);

/*--------------------------------------------------------------------------*/

#endif /* SQUIRRELJME_LRJAR_H */
