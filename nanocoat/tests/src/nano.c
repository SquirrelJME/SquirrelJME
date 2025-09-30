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
#include "sjme/nvm/loop.h"
#include "test.h"

sjme_errorCode sjme_test_nano_nativeCall(
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrInNotNull sjme_jmethodID methodID,
	sjme_attrInNotNull sjme_nvm_class_methodInfo methodInfo,
	sjme_attrInNotNull sjme_jvalueTyped* argR,
	sjme_attrInPositive sjme_jint argC,
	sjme_attrInNullable sjme_jvalueTyped* argV)
{
	if (inFrame == NULL || methodID == NULL || argR == NULL || argR == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (argC < 0)
		return SJME_ERROR_INVALID_ARGUMENT;

	if (argC > 0 && argV == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
}

static const sjme_nvm_stateHooks sjme_test_nano_hooks =
{
	sjme_sm(.gc, NULL),
	sjme_sm(.nativeCall, sjme_test_nano_nativeCall),
};

/**
 * NanoTests are meant to be as light as possible, only requiring the
 * target class and anything it relies upon. The purpose of these is to
 * make it so debugging issues is far easier to figure out, as there is
 * far simpler JVM state to sift through.
 * 
 * @param argc Argument count.
 * @param argv Arguments.
 * @return Standard exit codes.
 * @since 2025/09/24
 */
int main(int argc, sjme_lpstr* argv)
{
	sjme_errorCode error;
	sjme_alloc_pool pool;
	sjme_nvm_bootParam bootParam;
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
		sjme_message("Not enough arguments to NanoTest executable.");
		return EXIT_FAILURE;
	}

	/* Register the crash handler. */
	sjme_debug_crashRegister();
	
	/* Debug. */
	for (i = 0; i < argc; i++)
		sjme_message("argv[%d]: %s", i, argv[i]);
	
	/* Use default NAL to obtain the boot Jar. */
	nal = &sjme_nal_default;
	
	/* Allocate main pool. */
	pool = NULL;
	if (sjme_error_is(error = sjme_alloc_poolInitMalloc(&pool,
		1048576 * 32)) || pool == NULL)
		goto fail_poolInit;
	
	/* Open seekable to the boot Jar. */
	bootSeek = NULL;
	if (sjme_error_is(error = nal->fileOpen(pool, argv[1],
		&bootSeek, SJME_NAL_OPEN_READ)) || bootSeek == NULL)
		goto fail_openBootJar;
	
	/* Load boot suite. */
	bootSuite = NULL;
	if (sjme_error_is(error = sjme_nvm_rom_suiteFromZipSeekable(
		pool, &bootSuite, bootSeek, SJME_NVM_BOOT_CLUTTER_RELEASE)) ||
		bootSuite == NULL)
		goto fail_loadBootJar;
	
	/* Splice up the classpath. */
	n = strlen(argv[3]) + 1;
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
		
	/* Setup main arguments to use, of which there are none. */
	mainArgs = NULL;
	if (sjme_error_is(error = sjme_list_alloc(pool, 0, &mainArgs,
		sjme_lpstr, 0)) || mainArgs == NULL)
		goto fail_initMainArgs;
	
	/* Setup boot parameters. */
	memset(&bootParam, 0, sizeof(bootParam));
	bootParam.nal = &sjme_nal_test;
	bootParam.bootSuite = bootSuite;
	bootParam.mainClass = argv[4];
	bootParam.mainClassPathByName = (const sjme_list_sjme_lpcstr*)classpath;
	bootParam.mainArgs = (const sjme_list_sjme_lpcstr*)mainArgs;

	/* Hooks specifically for NanoTest. */
	bootParam.hooks = &sjme_test_nano_hooks;
	bootParam.hookData = &result;

	/* JDWP Debugging. */
	bootParam.jdwpAddress = "localhost";
	bootParam.jdwpPort = 5005;
	bootParam.jdwpListening = SJME_JNI_FALSE;
	
	/* Boot the virtual machine. */
	inState = NULL;
	if (sjme_error_is(error = sjme_nvm_boot(pool,
		&bootParam, &inState, NULL)))
		goto fail_boot;
	
	/* Iterate the virtual machine loop. */
	sjme_messageB("--------------------------------------------------------");
	for (terminated = SJME_JNI_FALSE; !terminated;)
	{
		/* Let other threads run. */
		sjme_thread_yield();
		
		/* Tick the virtual machine. */
		if (sjme_error_is(error = sjme_nvm_loop_tick(inState, -1,
			NULL, &terminated)))
		{
			/* Fail unless this was interrupted. */
			if (error == SJME_ERROR_INTERRUPTED)
				continue;
			
			goto fail_loop;
		}
	}
	sjme_messageB("--------------------------------------------------------");
	
	/* Destroy the VM before exit. */
	exitCode = -1;
	if (sjme_error_is(error = sjme_nvm_destroy(inState, &exitCode)))
		goto fail_destroy;
	
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
	sjme_message("Failed NanoTest: %d", error);
	return EXIT_FAILURE;
}
