/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "sjme/nvm/boot.h"
#include "sjme/nvm/nvm.h"
#include "sjme/nvm/nvmFunc.h"
#include "sjme/nvm/payload.h"
#include "test.h"

int main(int argc, sjme_lpstr* argv)
{
	sjme_errorCode error;
	sjme_alloc_pool* pool;
	sjme_nvm_bootParam bootConfig;
	sjme_nvm state;
	sjme_jint exitCode;
	const sjme_nal* nal;
	
	/* Use test NAL. */
	nal = &sjme_nal_test;
	
	/* Allocate main pool. */
	pool = NULL;
	if (sjme_error_is(error = sjme_alloc_poolInitMalloc(&pool,
		16777216)) || pool == NULL)
		goto fail_poolInit;
	
	sjme_todo("Impl?");
	
	return EXIT_FAILURE;
#if 0
	/* Setup boot configuration. */
	memset(&bootConfig, 0, sizeof(bootConfig));
	bootConfig.payload = &sjme_payload_config_data;
	
	/* Boot the virtual machine. */
	state = NULL;
	if (sjme_error_is(sjme_nvm_boot(NULL, NULL,
		&bootConfig, &state)))
		return EXIT_FAILURE;
		
	/* Constantly ticks the virtual machine until it stops. */
	while (sjme_nvm_tick(state, -1, NULL))
		;
		
	/* Cleanup the virtual machine. */
	exitCode = EXIT_FAILURE;
	if (!sjme_nvm_destroy(state, &exitCode))
		return EXIT_FAILURE;
	
	return exitCode;
#endif
fail_poolInit:
	sjme_message("Failed TAC test: %d", error);
	return EXIT_FAILURE;
}
