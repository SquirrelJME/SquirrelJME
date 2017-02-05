/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// --------------------------------------------------------------------------*/

/**
 * Handles the bootstrap classpath.
 *
 * @since 2017/02/05
 */

#include "bootpath.h"

void WC_InitializeBootClassPath(const char** paths)
{
	const char** rover;
	
	// {@squirreljme.error WC0k No paths specified.}
	WC_ASSERT("WC0k", paths == NULL);	
	
	// Debug
	WC_VERBOSE(WC_VERBOSE_MODE_CLASS, "WC_InitializeBootClassPath(", 0);
	for (rover = paths; *rover; rover++)
		WC_VERBOSE(WC_VERBOSE_MODE_CLASS, "\t%s", rover);
	WC_VERBOSE(WC_VERBOSE_MODE_CLASS, "\t)", 0);
	
	WC_TODO();
}

