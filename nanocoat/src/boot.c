/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <stdio.h>
#include <string.h>

#include "sjme/allocSizeOf.h"
#include "sjme/boot.h"
#include "sjme/debug.h"
#include "sjme/except.h"
#include "sjme/nvm.h"
#include "sjme/task.h"
#include "sjme/charSeq.h"
#include "sjme/native.h"

sjme_errorCode sjme_nvm_allocReservedPool(
	sjme_attrInNotNull sjme_alloc_pool* mainPool,
	sjme_attrOutNotNull sjme_alloc_pool** outReservedPool)
{
	sjme_errorCode error;
	sjme_pointer reservedBase;
	sjme_alloc_pool* reservedPool;
	sjme_jint reservedSize;

	if (mainPool == NULL || outReservedPool == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Determine how big the reserved pool should be... */
	reservedBase = NULL;
	reservedSize = -1;
	if (sjme_error_is(error = sjme_alloc_sizeOf(
		SJME_ALLOC_SIZEOF_RESERVED_POOL, 0, &reservedSize)))
		return sjme_error_default(error);
	if (sjme_error_is(error = sjme_alloc(mainPool,
		reservedSize, (sjme_pointer*)&reservedBase) ||
		reservedBase == NULL))
		return sjme_error_default(error);

	/* Initialize a reserved pool where all of our own data structures go. */
	reservedPool = NULL;
	if (sjme_error_is(error = sjme_alloc_poolInitStatic(
		&reservedPool, reservedBase, reservedSize)) ||
		reservedPool == NULL)
		return sjme_error_default(error);

	/* Use the resultant pool. */
	*outReservedPool = reservedPool;
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_boot(
	sjme_attrInNotNull sjme_alloc_pool* mainPool,
	sjme_attrInNotNull sjme_alloc_pool* reservedPool,
	sjme_attrInNotNull const sjme_nvm_bootParam* param,
	sjme_attrOutNotNull sjme_nvm* outState)
{
#define FIXED_SUITE_COUNT 16
	sjme_errorCode error;
	sjme_exceptTrace* trace;
	sjme_jint i, n;
	sjme_nvm result;
	sjme_rom_suite mergeSuites[FIXED_SUITE_COUNT];
	sjme_jint numMergeSuites;
	sjme_task_startConfig initTaskConfig;
	sjme_nvm_task initTask;
	sjme_list_sjme_rom_library* classPath;
	
	if (param == NULL || outState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Set up a reserved pool where all the data structures for the VM go... */
	/* But only if one does not exist. */
	if (reservedPool == NULL)
		if (sjme_error_is(error = sjme_nvm_allocReservedPool(mainPool,
			&reservedPool)) || reservedPool == NULL)
			goto fail_reservedPoolAlloc;

	/* Allocate resultant state. */
	result = NULL;
	if (sjme_error_is(error = sjme_alloc_weakNew(reservedPool,
		sizeof(*result), sjme_nvm_enqueueHandler, SJME_NVM_ENQUEUE_STATE,
		(sjme_pointer*)&result, NULL)) || result == NULL)
		goto fail_resultAlloc;

	/* Make a defensive copy of the boot parameters. */
	if (sjme_error_is(error = sjme_alloc_copy(reservedPool,
		sizeof(sjme_nvm_bootParam),
		(sjme_pointer*)&result->bootParamCopy, param)) ||
		result == NULL)
		goto fail_bootParamCopy;

	/* Can only use one or the other to get the class path. */
	if (result->bootParamCopy->mainClassPathById != NULL &&
		result->bootParamCopy->mainClassPathByName != NULL)
		goto fail_bothIdAndName;

	/* Set parameters accordingly. */
	result->allocPool = mainPool;
	result->reservedPool = reservedPool;

	/* Initialize base for suite merging. */
	memset(mergeSuites, 0, sizeof(mergeSuites));
	numMergeSuites = 0;

	/* Process payload suites. */
	if (result->bootParamCopy->payload != NULL)
	{
		/* Scan accordingly. */
		if (sjme_error_is(error = sjme_rom_suiteFromPayload(reservedPool,
			&mergeSuites[numMergeSuites],
			result->bootParamCopy->payload)))
			goto fail_payloadRom;

		/* Was a suite generated? */
		if (mergeSuites[numMergeSuites] != NULL)
			numMergeSuites++;
	}

	/* Is there a pre-existing suite to use? */
	if (result->bootParamCopy->suite != NULL)
		mergeSuites[numMergeSuites++] =
			(sjme_rom_suite)result->bootParamCopy->suite;

	/* No suites at all? Running with absolutely nothing??? */
	if (numMergeSuites <= 0)
	{
		/* Debug. */
		sjme_message("No suites are available, cannot run.");
		
		goto fail_noSuites;
	}

	/* Use the single suite only. */
	else if (numMergeSuites == 1)
		result->suite = mergeSuites[0];

	/* Merge everything into one. */
	else
	{
		/* Merge all the suites together into one. */
		if (sjme_error_is(error = sjme_rom_suiteFromMerge(reservedPool,
			&result->suite, mergeSuites,
			numMergeSuites)) || result->suite == NULL)
			goto fail_suiteMerge;
	}

	/* Resolve class path libraries. */
	classPath = NULL;
	error = SJME_ERROR_UNKNOWN;
	if (result->bootParamCopy->mainClassPathById != NULL)
		error = sjme_rom_resolveClassPathById(result->suite,
			result->bootParamCopy->mainClassPathById,
			&classPath);
	else
		error = sjme_rom_resolveClassPathByName(result->suite,
			result->bootParamCopy->mainClassPathByName,
			&classPath);

	/* Failed to resolve? */
	if (sjme_error_is(error) || classPath == NULL)
	{
		/* Debug. */
		sjme_message("Classpath resolve failure: %d %p", error, classPath);

		goto fail_badClassPath;
	}

	/* Setup task details. */
	initTaskConfig.stdOut = SJME_TASK_PIPE_REDIRECT_TYPE_TERMINAL;
	initTaskConfig.stdErr = SJME_TASK_PIPE_REDIRECT_TYPE_TERMINAL;
	initTaskConfig.classPath = classPath;
	initTaskConfig.mainClass = result->bootParamCopy->mainClass;
	initTaskConfig.mainArgs = result->bootParamCopy->mainArgs;
	initTaskConfig.sysProps = result->bootParamCopy->sysProps;

	/* Spawn initial task which uses the main arguments. */
	initTask = NULL;
	if (sjme_error_is(error = sjme_task_start(result,
		&initTaskConfig, &initTask)) || initTask == NULL)
		goto fail_initTask;
	
	/* Return newly created VM. */
	*outState = result;
	return SJME_ERROR_NONE;

	/* Failed at specific points... */
fail_initTask:
fail_badClassPath:
fail_suiteMerge:
fail_noSuites:
fail_payloadRom:
fail_bothIdAndName:
fail_bootParamCopy:
	if (result != NULL && result->bootParamCopy != NULL)
		sjme_alloc_free(result->bootParamCopy);

fail_resultAlloc:
	if (result != NULL)
		sjme_alloc_free(result);

fail_reservedPoolAlloc:

	/* Use whatever error code. */
	return sjme_error_defaultOr(error, SJME_ERROR_BOOT_FAILURE);
}

sjme_errorCode sjme_nvm_destroy(sjme_nvm state, sjme_jint* exitCode)
{
	if (state == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Free sub-structures. */
	if (SJME_JNI_TRUE)
		sjme_todo("sjme_nvm_destroy()");
		
	/* Free main structure. */
	if (SJME_JNI_TRUE)
		sjme_todo("sjme_nvm_destroy()");
	
	/* Set exit code, if requested. */
	if (SJME_JNI_TRUE)
		sjme_todo("sjme_nvm_destroy()");
	
	/* Finished. */
	sjme_todo("sjme_nvm_destroy()");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

sjme_errorCode sjme_nvm_enqueueHandler(
	sjme_attrInNotNull sjme_alloc_weak weak,
	sjme_attrInNullable sjme_pointer data,
	sjme_attrInValue sjme_jboolean isBlockFree)
{
	return sjme_error_notImplemented(0);
}

sjme_errorCode sjme_nvm_parseCommandLine(
	sjme_attrInNotNull sjme_alloc_pool* inPool,
	sjme_attrInOutNotNull sjme_nvm_bootParam* outParam,
	sjme_attrInPositiveNonZero sjme_jint argc,
	sjme_attrInNotNull sjme_lpcstr* argv)
{
	sjme_errorCode error;
	sjme_jint argAt;
	sjme_charSeq argSeq;
	sjme_jboolean jarSpecified;
	
	if (inPool == NULL || outParam == NULL || argv == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (argc <= 0)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	/* Command line format is: */
	jarSpecified = SJME_JNI_FALSE;
	for (argAt = 0; argAt < argc; argAt++)
	{
		/* Setup sequence to wrap argument for parsing. */
		memset(&argSeq, 0, sizeof(argSeq));
		if (sjme_error_is(error = sjme_charSeq_newUtfStatic(
			&argSeq, argv[argAt])))
			return sjme_error_default(error);
		
		/* -Xemulator:(vm) */
		if (sjme_charSeq_startsWithUtfR(&argSeq,
			"-Xemulator:"))
		{
			sjme_todo("Impl? %s", argv[argAt]);
		}
		
		/* -Xsnapshot:(path-to-nps) */
		else if (sjme_charSeq_startsWithUtfR(&argSeq,
			"-Xsnapshot:"))
		{
			sjme_todo("Impl? %s", argv[argAt]);
		}
		
		/* -Xentry:id */
		else if (sjme_charSeq_startsWithUtfR(&argSeq,
			"-Xentry:"))
		{
			sjme_todo("Impl? %s", argv[argAt]);
		}
		
		/* -Xlibraries:(class:path:...) */
		else if (sjme_charSeq_startsWithUtfR(&argSeq,
			"-Xlibraries:"))
		{
			sjme_todo("Impl? %s", argv[argAt]);
		}
		
		/* -Xjdwp:[hostname]:port */
		else if (sjme_charSeq_startsWithUtfR(&argSeq,
			"-Xjdwp:"))
		{
			sjme_todo("Impl? %s", argv[argAt]);
		}
		
		/* -Xdebug */
		else if (sjme_charSeq_equalsUtfR(&argSeq,
			"-Xdebug"))
		{
			sjme_todo("Impl? %s", argv[argAt]);
		}
		
		/* -Xthread:(single|coop|multi|smt) */
		else if (sjme_charSeq_startsWithUtfR(&argSeq,
			"-Xthread:"))
		{
			sjme_todo("Impl? %s", argv[argAt]);
		}
		
		/* -Dsysprop=value */
		else if (sjme_charSeq_startsWithUtfR(&argSeq,
			"-D"))
		{
			sjme_todo("Impl? %s", argv[argAt]);
		}
		
		/* -Xclutter:(release|debug) */
		else if (sjme_charSeq_startsWithUtfR(&argSeq,
			"-Xclutter:"))
		{
			sjme_todo("Impl? %s", argv[argAt]);
		}
		
		/* -Xtrace:(flag|...) */
		else if (sjme_charSeq_startsWithUtfR(&argSeq,
			"-Xtrace:"))
		{
			sjme_todo("Impl? %s", argv[argAt]);
		}
		
		/* -Xscritchui:(ui) */
		else if (sjme_charSeq_startsWithUtfR(&argSeq,
			"-Xscritchui:"))
		{
			sjme_todo("Impl? %s", argv[argAt]);
		}
		
		/* -classpath (class:path:...) */
		else if (sjme_charSeq_equalsUtfR(&argSeq,
			"-classpath"))
		{
			sjme_todo("Impl? %s", argv[argAt]);
		}
		
		/* -zero/-Xint */
		else if (sjme_charSeq_equalsUtfR(&argSeq, "-zero") ||
			sjme_charSeq_equalsUtfR(&argSeq, "-Xint"))
		{
			sjme_todo("Impl? %s", argv[argAt]);
		}
		
		/* -client/-server. */
		else if (sjme_charSeq_equalsUtfR(&argSeq,
			"-client") ||
			sjme_charSeq_equalsUtfR(&argSeq, "-server"))
		{
			sjme_todo("Impl? %s", argv[argAt]);
		}
		
		/* -jar */
		else if (sjme_charSeq_equalsUtfR(&argSeq, "-jar"))
		{
			/* We are using a Jar now. */
			jarSpecified = SJME_JNI_TRUE;
			
			sjme_todo("Impl? %s", argv[argAt]);
		}
		
		/* -version */
		else if (sjme_charSeq_equalsUtfR(&argSeq,
			"-version"))
		{
			/* Print version information to stdout. */
			/* https://www.oracle.com/java/technologies/javase/ */
			/* versioning-naming.html */
			sjme_nal_default.stdOutF(
				"java version \"1.8.0\"\n");
			sjme_nal_default.stdOutF(
				"SquirrelJME Class Library, Micro Edition (build %s)\n",
				SQUIRRELJME_VERSION);
			sjme_nal_default.stdOutF(
				"SquirrelJME NanoCoat VM (build %s)\n",
				SQUIRRELJME_VERSION);
			
			/* Exit. */
			return SJME_ERROR_EXIT;
		}
		
		/* -help */
		else if (sjme_charSeq_equalsUtfR(&argSeq,
			"-help"))
		{
			sjme_todo("Impl? %s", argv[argAt]);
			
			/* Exit. */
			return SJME_ERROR_EXIT;
		}
		
		/* Invalid, fail. */
		else
		{
			sjme_message("Invalid command line: %s",
				argv[argAt]);
			
			return SJME_ERROR_INVALID_ARGUMENT;
		}
		
		sjme_todo("impl?");
	}
	
	/* Main-class, if not -jar */
	if (!jarSpecified)
	{
		sjme_todo("impl?");
	}
	
	/* Arguments... */
	if (SJME_JNI_TRUE)
	{
		sjme_todo("impl?");
	}
	
	return sjme_error_notImplemented(0);
}
