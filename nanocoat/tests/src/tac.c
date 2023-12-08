/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "sjme/boot.h"
#include "sjme/nvm.h"
#include "sjme/nvmFunc.h"
#include "sjme/payload.h"
#include "test.h"

int main(int argc, char** argv)
{
	sjme_nvm_bootConfig bootConfig;
	sjme_nvm_state* state;
	sjme_jint exitCode;
	
	/* Setup boot configuration. */
	memset(&bootConfig, 0, sizeof(bootConfig));
	bootConfig.payload = &sjme_static_payload_data;
	
	/* Boot the virtual machine. */
	state = NULL;
	if (SJME_IS_ERROR(sjme_nvm_boot(NULL, &bootConfig,
		&state, argc, argv)))
		return EXIT_FAILURE;
		
	/* Constantly ticks the virtual machine until it stops. */
	while (sjme_nvm_tick(state, -1))
		;
		
	/* Cleanup the virtual machine. */
	exitCode = EXIT_FAILURE;
	if (!sjme_nvm_destroy(state, &exitCode))
		return EXIT_FAILURE;
	
	return exitCode;
}
