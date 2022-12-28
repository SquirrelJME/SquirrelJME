/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "frontend/ieee1275/boot.h"
#include "frontend/ieee1275/ieee1275.h"
#include "sjmejni/sjmejni.h"

void sjme_ieee1275BootArch(void)
{
	/* Enter IEEE1275 Mode. */
	sjme_ieee1275Boot();

	/* Exit system. */
	sjme_ieee1275Exit();
}
