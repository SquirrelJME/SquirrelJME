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
	sjme_nvm_bootParam bootParam;
	sjme_nvm state;
	sjme_jint exitCode, i, n;
	sjme_seekable bootSeek;
	sjme_nvm_rom_suite bootSuite;
	sjme_list_sjme_lpstr* classpath;
	sjme_list_sjme_lpstr* mainArgs;
	sjme_nvm inState;
	sjme_jboolean terminated;
	const sjme_nal* nal;
	sjme_lpstr classpathSplice;
	
	/* Incorrect number of arguments? */
	if (argc < 5)
	{
		sjme_message("Not enough arguments to TAC executable.");
		return EXIT_FAILURE;
	}
	
	/* Debug. */
	for (i = 0; i < argc; i++)
		sjme_message("argv[%d]: %s", i, argv[i]);
	
	/* Use default NAL to obtain the boot Jar. */
	nal = &sjme_nal_default;
	
	/* Allocate main pool. */
	pool = NULL;
	if (sjme_error_is(error = sjme_alloc_poolInitMalloc(&pool,
		16777216)) || pool == NULL)
		goto fail_poolInit;
	
	/* Open seekable to the boot Jar. */
	bootSeek = NULL;
	if (sjme_error_is(error = nal->fileOpen(pool, argv[1],
		&bootSeek)) || bootSeek == NULL)
		goto fail_openBootJar;
	
	/* Load boot suite. */
	bootSuite = NULL;
	if (sjme_error_is(error = sjme_nvm_rom_suiteFromZipSeekable(
		pool, &bootSuite, bootSeek)) ||
		bootSuite == NULL)
		goto fail_loadBootJar;
	
	/* Splice up the classpath. */
	n = strlen(argv[3]);
	classpathSplice = NULL;
	if (sjme_error_is(error = sjme_alloc(pool, n + 1,
		(sjme_pointer*)&classpathSplice)) || classpathSplice == NULL)
		goto fail_splicePath;
	
	/* Turn colons into NULs for splitting. */
	for (i = 0; i < n; i++)
		if (argv[3][i] == ':')
			classpathSplice[i] = '\0';
		else
			classpathSplice[i] = argv[3][i];
	classpathSplice[n] = '\0';
	
	/* Setup classpath to use. */
	classpath = NULL;
	if (sjme_error_is(error = sjme_list_flattenArgNul(pool,
		&classpath, classpathSplice)) ||
		classpath == NULL)
		goto fail_initClasspath;
	
	/* Debug. */
	for (i = 0; i < classpath->length; i++)
		sjme_message("classpath[%d]: %s", i, classpath->elements[i]);
		
	/* Setup main arguments to use. */
	mainArgs = NULL;
	if (sjme_error_is(error = sjme_list_newV(pool, sjme_lpstr,
		0, 1, &mainArgs, argv[4])) || mainArgs == NULL)
		goto fail_initMainArgs;
	
	/* Setup boot parameters. */
	memset(&bootParam, 0, sizeof(bootParam));
	bootParam.nal = &sjme_nal_test;
	bootParam.bootSuite = bootSuite;
	bootParam.mainClass = "net.multiphasicapps.tac.MainSingleRunner";
	bootParam.mainClassPathByName = (const sjme_list_sjme_lpcstr*)classpath;
	bootParam.mainArgs = (const sjme_list_sjme_lpcstr*)mainArgs;
	
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
		
	sjme_todo("Impl?");
	
	/* Return with the exit code. */
	return exitCode;
	
fail_destroy:
fail_loop:
fail_boot:
fail_initMainArgs:
fail_initClasspath:
fail_splicePath:
fail_loadBootJar:
fail_openBootJar:
fail_poolInit:
	sjme_message("Failed TAC test: %d", error);
	return EXIT_FAILURE;
}
