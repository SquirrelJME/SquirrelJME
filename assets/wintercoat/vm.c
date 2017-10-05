/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// --------------------------------------------------------------------------*/

/**
 * Virtual machine state.
 *
 * @since 2017/10/05
 */

#include "vm.h"

sjme_error wc_initvm(sjme_init* initstruct, sjme_vm** outvm)
{
	int i;
	
	// Load all entries 
	for (i = 0; i < initstruct->numclasspath; i++)
	{
		char* cp = initstruct->classpath[i];
		
		wc_verbose(SJME_DEBUG_INIT, "Loading classpath `%s`...", cp);
		
		wc_todo();
	}
	
	wc_todo();
	
	return SJME_ERROR_OK;
}

