/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <stdlib.h>
#include <string.h>

#include "sjme/debug.h"
#include "sjme/alloc.h"
#include "sjme/nvm/boot.h"
#include "sjme/nvm/nvm.h"
#include "sjme/nvm/nvmFunc.h"

/**
 * Main program entry point.
 * 
 * @param argc Argument count. 
 * @param argv Arguments passed.
 * @return Returns @c 0 on success, otherwise another exit code.
 */
int main(int argc, sjme_lpcstr* argv)
{
	sjme_errorCode error;
	sjme_alloc_pool* pool;
	sjme_nvm inState;
	sjme_nvm_bootParam bootParam;
	sjme_jint exitCode;
	sjme_jboolean terminated;
	const sjme_nal* nal;
	
	/* Use default NAL. */
	nal = &sjme_nal_default;
	
	/* Allocate main pool. */
	pool = NULL;
	if (sjme_error_is(error = sjme_alloc_poolInitMalloc(&pool,
		16777216)) || pool == NULL)
		goto fail_poolInit;
	
	/* Setup boot parameters. */
	memset(&bootParam, 0, sizeof(bootParam));
	bootParam.nal = nal;
	
	/* Parse main arguments. */
	if (sjme_error_is(error = sjme_nvm_parseCommandLine(pool,
		nal, &bootParam, argc, argv)))
	{
		/* Exit instead of continuing? */
		if (error == SJME_ERROR_EXIT)
		{
			exitCode = 0;
			goto okay_shortExit;
		}
		
		goto fail_argParse;
	}
	
	/* If there was no boot suite specified, load a default one. */
	if (bootParam.bootSuite == NULL)
		if (sjme_error_is(error = sjme_nvm_defaultBootSuite(pool,
			nal, &bootParam.bootSuite)) ||
			bootParam.bootSuite == NULL)
			goto fail_bootSuiteBackup;
	
	/* If no classpath was specified, load the launcher instead. */
	if (bootParam.mainClassPathById == NULL &&
		bootParam.mainClassPathByName == NULL)
	{
		/* Try to find default launcher. */
		if (sjme_error_is(error = sjme_nvm_rom_suiteDefaultLaunch(pool,
			bootParam.bootSuite,
			(sjme_lpstr*)&bootParam.mainClass,
			(sjme_list_sjme_lpstr**)&bootParam.mainArgs,
			(sjme_list_sjme_jint**)&bootParam.mainClassPathById,
			(sjme_list_sjme_lpstr**)&bootParam.mainClassPathByName)))
			goto fail_defaultLaunch;
		
		/* Still failed? */
		if (bootParam.mainClassPathById == NULL &&
			bootParam.mainClassPathByName == NULL)
		{
			error = SJME_ERROR_NO_CLASS;
			goto fail_defaultLaunch;
		}
	}
	
	/* Boot the virtual machine. */
	inState = NULL;
	if (sjme_error_is(error = sjme_nvm_boot(pool,
		NULL, &bootParam, &inState)))
		goto fail_boot;
	
	/* Iterate the virtual machine loop. */
	for (terminated = SJME_JNI_FALSE; !terminated;)
	{
		/* Let other threads run. */
		sjme_thread_yield();
		
		/* Tick the virtual machine. */
		if (sjme_error_is(error = sjme_nvm_tick(inState, -1,
			&terminated)))
		{
			/* Fail unless this was interrupted. */
			if (error == SJME_ERROR_INTERRUPTED)
				continue;
			
			goto fail_loop;
		}
	}
	
	/* Destroy the VM before exit. */
	exitCode = -1;
	if (sjme_error_is(error = sjme_nvm_destroy(inState, &exitCode)))
		goto fail_destroy;
	
okay_shortExit:
	/* Destroy the main memory pool. */
	if (sjme_error_is(error = sjme_alloc_poolDestroy(pool)))
		goto fail_destroyPool;
	
	/* Exit with the given code. */
	return exitCode;

fail_destroyPool:
fail_destroy:
fail_loop:
fail_boot:
	if (inState != NULL)
		sjme_nvm_destroy(inState, NULL);

fail_defaultLaunch:
fail_bootSuiteBackup:
fail_argParse:
fail_poolInit:
	if (pool != NULL)
		sjme_alloc_poolDestroy(pool);
	
	if (error == SJME_ERROR_NONE || error == SJME_ERROR_UNKNOWN)
		return EXIT_FAILURE;
	return (-error);
}
