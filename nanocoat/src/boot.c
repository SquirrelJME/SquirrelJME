/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/
#include "sjme/nvm/allocSizeOf.h"
#include "sjme/nvm/boot.h"
#include "sjme/debug.h"
#include "sjme/nvm/nvm.h"
#include "sjme/nvm/task.h"
#include "sjme/charSeq.h"
#include "sjme/native.h"
#include "sjme/nvm/cleanup.h"
#include "sjme/path.h"

#if defined(SJME_PATH_SHORT)
	/** The name of the SquirrelJME Jar. */
	#define SJME_JAR_NAME "sjme.jar"

	/** The name of the SquirrelJME directory. */
	#define SJME_DIRECTORY_NAME "sjme"
#else
	/** The name of the SquirrelJME Jar. */
	#define SJME_JAR_NAME "squirreljme.jar"

	/** The name of the SquirrelJME directory. */
	#define SJME_DIRECTORY_NAME "squirreljme"
#endif

/**
 * Help parameter storage.
 * 
 * @since 2024/08/08
 */
typedef struct sjme_nvm_helpParam
{
	/** The argument. */
	sjme_lpcstr arg;
	
	/** Description of the parameter. */
	sjme_lpcstr desc;
} sjme_nvm_helpParam;

static const sjme_nvm_helpParam sjme_nvm_helpParams[] =
{
	{"-Xclutter:<release|debug>",
		"If available, selects the given clutter level."},
	{"-Xdebug", 
		"Starts debugging with the built-in debugger."},
	{"-Xemulator:<vm>",
		"Always \"nanocoat\", if \"springcoat\" implies -Xint."},
	{"-Xentry:id",
		"If launching a MIDlet, choose a MIDlet entry."},
	{"-Xint",
		"Force pure interpreter, do not perform optimizations (slow)."},
	{"-Xjdwp:[hostname]:port",
		"Listens or connects to a JDWP debugger."},
	{"-Xrom:<path>",
		"The ROM to use."},
	{"-Xlibraries:<class:path:...>",
		"Libraries to include in the library path, not the classpath."},
	{"-Xscritchui:<ui>",
		"Default interface to choose for ScritchUI."},
	{"-Xsnapshot:<path-to-nps>",
		"Write a VisualVM snapshot (.nps) to the given path."},
	{"-XstartOnFirstThread",
		"Ignored."},
	{"-Xthread:<single|coop|shared|multi|smt>",
		"The threading model to use."},
	{"-Xtrace:<flag|...>",
		"Trace flags to permanently set on by default."},
	{"-D<sysprop>=<value>",
		"Declare system property <sysprop> and set to <value>."},
	{"-classpath <class:path:...>",
		"The additional classpath to use for the application."},
	{"-client",
		"Ignored."},
	{"-? -h -help",
		"Hopefully what you are reading currently, to StdErr."},
	{"--help",
		"Hopefully what you are reading currently, to StdOut."},
	{"-jar <Jar>",
		"Launch the specified Jar."},
	{"-server",
		"Ignored."},
	{"-version",
		"SquirrelJME version information, to StdErr."},
	{"--version",
		"SquirrelJME version information, to StdOut."},
	{"-zero",
		"Same as -Xint."},
	
	{NULL, NULL}
};

/** SquirrelJME ROM names. */
static sjme_lpcstr sjme_nvm_romNames[] =
{
	"squirreljme-"SQUIRRELJME_VERSION"-fast.jar",
	"squirreljme-"SQUIRRELJME_VERSION".jar",
	"squirreljme-"SQUIRRELJME_VERSION"-test.jar",
	"squirreljme-"SQUIRRELJME_VERSION"-slow.jar",
	"squirreljme-"SQUIRRELJME_VERSION"-slow-test.jar",
	"squirreljme-fast.jar",
	"squirreljme.jar",
	"squirreljme-test.jar",
	"squirreljme-slow.jar",
	"squirreljme-slow-test.jar",
	NULL
};

static sjme_errorCode sjme_nvm_defaultBootSuiteAttempt(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInNotNull const sjme_nal* nal,
	sjme_attrOutNotNull sjme_nvm_rom_suite* outSuite,
	sjme_attrInNotNull sjme_lpcstr basePath,
	sjme_attrInNotNull sjme_lpcstr romName,
	sjme_attrInValue sjme_nvm_bootClutterLevel clutterLevel)
{
	sjme_errorCode error;
	sjme_cchar dataPath[SJME_MAX_PATH];
	sjme_seekable rom;
	sjme_nvm_rom_suite result;

	if (allocPool == NULL || nal == NULL || outSuite == NULL ||
		basePath == NULL || romName == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
#if 1
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
#else
	/* Base path here. */
	memset(&dataPath, 0, sizeof(dataPath));
	if (strlen(basePath) > 0)
		if (sjme_error_is(error = sjme_path_resolveAppend(
			dataPath, SJME_MAX_PATH - 1,
			basePath, INT32_MAX)))
			return sjme_error_default(error);
	
	/* Use ROM from here. */
	if (strlen(romName) > 0)
		if (sjme_error_is(error = sjme_path_resolveAppend(
			dataPath, SJME_MAX_PATH - 1,
			romName, INT32_MAX)))
			return sjme_error_default(error);
#endif
	
	/* Open main ROM file. */
	rom = NULL;
	if (sjme_error_is(error = nal->fileOpen(allocPool, dataPath,
		&rom, SJME_NAL_OPEN_READ)) || rom == NULL)
		return sjme_error_default(error);
	
	/* Load suite from the ZIP. */
	result = NULL;
	if (sjme_error_is(error = sjme_nvm_rom_suiteFromZipSeekable(allocPool,
		&result, rom, clutterLevel)) || result == NULL)
	{
		/* Make sure to close the file. */
		sjme_closeable_close(SJME_AS_CLOSEABLE(rom));
		
		/* Fail. */
		return sjme_error_default(error);
	}
	
	/* Success! */
	*outSuite = result;
	return SJME_ERROR_NONE;
}

static sjme_errorCode sjme_nvm_printHelp(
	sjme_attrInNotNull const sjme_nal* nal,
	sjme_attrInNotNull sjme_nal_stdOFunc helpOut,
	sjme_attrInNotNull sjme_nal_stdIoFlush helpFlush,
	sjme_attrInNotNull sjme_lpcstr argSeq,
	sjme_attrInNotNull sjme_lpcstr programName)
{
	const sjme_nvm_helpParam* help;

	if (nal == NULL || helpOut == NULL || helpFlush == NULL ||
		argSeq == NULL || programName == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Where is this information going? */
	if (!strcmp(argSeq, "--help"))
	{
		helpOut = nal->stdIo[SJME_NVM_MLE_STD_PIPE_STDOUT].out;
		helpFlush = nal->stdIo[SJME_NVM_MLE_STD_PIPE_STDOUT].flush;
	}
	
	/* Normal usage. */
	sjme_nal_stdF(helpOut,
		"Usage: %s [Options] <MainClass> [Args...]\n", programName);
	sjme_nal_stdF(helpOut,
		"Usage: %s [Options] -jar <Jar> [Args...]\n", programName);
	sjme_nal_stdF(helpOut,"\n");
	
	/* And all the help parameters. */
	sjme_nal_stdF(helpOut, "Options are:\n");
	for (help = &sjme_nvm_helpParams[0]; help->arg != NULL; help++)
	{
		sjme_nal_stdF(helpOut, "  %s\n",
			help->arg);
		sjme_nal_stdF(helpOut, "    %s\n",
			help->desc);
	}

	/* Flush if possible. */
	if (helpFlush != NULL)
		helpFlush();
	
	/* Exit. */
	return SJME_ERROR_EXIT;
}

static sjme_errorCode sjme_nvm_printVersion(
	sjme_attrInNotNull const sjme_nal* nal,
	sjme_attrInNotNull sjme_nal_stdOFunc helpOut,
	sjme_attrInNotNull sjme_nal_stdIoFlush helpFlush,
	sjme_attrInNotNull sjme_lpcstr argSeq,
	sjme_attrInNotNull sjme_nvm_bootParam* outParam)
{
	if (nal == NULL || helpOut == NULL || helpFlush == NULL ||
		argSeq == NULL || outParam == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Where is this information going? */
	if (!strcmp(argSeq, "--version"))
	{
		helpOut = nal->stdIo[SJME_NVM_MLE_STD_PIPE_STDOUT].out;
		helpFlush = nal->stdIo[SJME_NVM_MLE_STD_PIPE_STDOUT].flush;
	}
	
	/* Print version information to stdout. */
	/* https://www.oracle.com/java/technologies/javase/ */
	/* versioning-naming.html */
	sjme_nal_stdF(helpOut,
		"java version \"1.8.0\"\n");
	sjme_nal_stdF(helpOut,
		"SquirrelJME Class Library, Micro Edition (build %s)\n",
		SQUIRRELJME_VERSION);
	sjme_nal_stdF(helpOut,
		"SquirrelJME NanoCoat VM (build %s, %s, %s %s %s/%s)\n",
		SQUIRRELJME_VERSION,
		(outParam->noOptimize ? SQUIRRELJME_VERSION_SPRINGCOAT :
			SQUIRRELJME_VERSION_NANOCOAT),
		SQUIRRELJME_VERSION_STABILITY, SQUIRRELJME_VERSION_ID,
		SQUIRRELJME_SYSTEM, SQUIRRELJME_ARCH, SJME_CONFIG_HAS_COMPILER);

	/* Flush if possible. */
	if (helpFlush != NULL)
		helpFlush();
	
	/* Exit. */
	return SJME_ERROR_EXIT;
}

sjme_errorCode sjme_nvm_boot(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInNotNull const sjme_nvm_bootParam* param,
	sjme_attrOutNotNull sjme_nvm* outState,
	sjme_attrOutNullable sjme_nvm_task* outInitTask)
{
#define FIXED_SUITE_COUNT 16
	sjme_errorCode error;
	sjme_nvm result;
	sjme_nvm_rom_suite mergeSuites[FIXED_SUITE_COUNT];
	sjme_jint numMergeSuites;
	sjme_nvm_task_taskNewConfig* initTaskConfig;
	const sjme_nvm_bootParam* bootParamCopy;
	sjme_nvm_task initTask;
	sjme_list_sjme_nvm_rom_library* classPath;
	
	if (allocPool == NULL || param == NULL || outState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* These are required. */
	if (param->nal == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Allocate resultant state. */
	result = NULL;
	if (sjme_error_is(error = sjme_nvm_alloc((sjme_nvm)allocPool,
		sizeof(*result), SJME_NVM_STRUCT_STATE,
		SJME_AS_NVM_COMMONP(&result))) || result == NULL)
		goto fail_resultAlloc;
	
	/* Make a defensive copy of the boot parameters. */
	result->bootParamCopy = NULL;
	if (sjme_error_is(error = sjme_alloc_copy(allocPool,
		sizeof(*result->bootParamCopy),
		(sjme_pointer*)&result->bootParamCopy,
		(sjme_pointer)param)) || result->bootParamCopy == NULL)
		goto fail_bootParamCopy;
	bootParamCopy = result->bootParamCopy;

	/* Make defensive init of the initial task configuration. */
	result->initTaskConfig = NULL;
	if (sjme_error_is(error = sjme_alloc(allocPool,
		sizeof(*result->initTaskConfig),
		(sjme_pointer*)&result->initTaskConfig)) ||
		result->initTaskConfig == NULL)
		goto fail_allocInitTaskConfig;

	/* Can only use one or the other to get the class path. */
	if (result->bootParamCopy->mainClassPathById != NULL &&
		result->bootParamCopy->mainClassPathByName != NULL)
		goto fail_bothIdAndName;

	/* Set parameters accordingly. */
	result->allocPool = allocPool;
	result->nal = param->nal;
	
	/* Initialize base for suite merging. */
	memset(mergeSuites, 0, sizeof(mergeSuites));
	numMergeSuites = 0;

#if defined(SJME_CONFIG_DEPRECATED)
	/* Process payload suites. */
	if (result->bootParamCopy->payload != NULL)
	{
		/* Scan accordingly. */
		if (sjme_error_is(error = sjme_nvm_rom_suiteFromPayload(
			allocPool,
			&mergeSuites[numMergeSuites],
			result->bootParamCopy->payload)))
			goto fail_payloadRom;

		/* Was a suite generated? */
		if (mergeSuites[numMergeSuites] != NULL)
			numMergeSuites++;
	}
#endif

	/* Is there a pre-existing boot suite to use? */
	if (result->bootParamCopy->bootSuite != NULL)
		if (numMergeSuites < FIXED_SUITE_COUNT)
			mergeSuites[numMergeSuites++] =
				(sjme_nvm_rom_suite)result->bootParamCopy->bootSuite;
	
	/* Is there a library suite to use? */
	if (result->bootParamCopy->librarySuite != NULL)
		if (numMergeSuites < FIXED_SUITE_COUNT)
			mergeSuites[numMergeSuites++] =
				(sjme_nvm_rom_suite)result->bootParamCopy->librarySuite;

	/* No suites at all? Running with absolutely nothing??? */
	if (numMergeSuites <= 0)
	{
		/* Debug. */
		sjme_message("No suites are available, cannot run.");

		/* Fail. */
		error = SJME_ERROR_NO_SUITES;
		goto fail_noSuites;
	}

	/* Use the single suite only. */
	else if (numMergeSuites == 1)
		result->suite = mergeSuites[0];

	/* Merge everything into one. */
	else
	{
		/* Merge all the suites together into one. */
		if (sjme_error_is(error = sjme_nvm_rom_suiteFromMerge(
			allocPool,
			&result->suite, mergeSuites,
			numMergeSuites)) || result->suite == NULL)
			goto fail_suiteMerge;
	}

	/* Use the classpath of the launcher? If enabled. */
	if (bootParamCopy->launcherFallback &&
		(bootParamCopy->mainClassPathById == NULL &&
		bootParamCopy->mainClassPathByName == NULL))
	{
		/* Determine the default boot suite. */
		if (sjme_error_is(error = sjme_nvm_rom_suiteDefaultLaunch(allocPool,
			result->suite,
			(sjme_lpstr*)&bootParamCopy->mainClass,
			(sjme_list_sjme_lpstr**)&bootParamCopy->mainArgs,
			(sjme_list_sjme_jint**)&bootParamCopy->mainClassPathById,
			(sjme_list_sjme_lpstr**)
				&bootParamCopy->mainClassPathByName)))
			goto fail_defaultLaunch;

		/* No default launcher found? */
		if (bootParamCopy->mainClassPathById == NULL &&
			bootParamCopy->mainClassPathByName == NULL)
		{
			error = SJME_ERROR_NO_SUITES;
			goto fail_defaultLaunch;
		}
	}

	/* Resolve class path libraries. */
	classPath = NULL;
	error = SJME_ERROR_NO_SUITES;
	if (result->bootParamCopy->mainClassPathById != NULL)
		error = sjme_nvm_rom_resolveClassPathById(result->suite,
			result->bootParamCopy->mainClassPathById,
			&classPath);
	else if (result->bootParamCopy->mainClassPathByName != NULL)
		error = sjme_nvm_rom_resolveClassPathByName(result->suite,
			result->bootParamCopy->mainClassPathByName,
			&classPath);

	/* Failed to resolve? */
	if (sjme_error_is(error) || classPath == NULL)
	{
		/* Debug. */
		sjme_message("Classpath resolve failure: %d %p %s",
			error, classPath,
			(result->bootParamCopy->mainClassPathById != NULL ?
				"byId" : "byName"));

		goto fail_badClassPath;
	}

	/* Allocate the task scheduler, if applicable. */
	if (result->threadModel != SJME_NVM_MLE_THREAD_MULTI)
		if (sjme_error_is(error = sjme_alloc(allocPool,
			sizeof(*result->schedule), (sjme_pointer*)&result->schedule)) ||
			result->schedule == NULL)
			goto fail_allocSchedule;

	/* Setup task details. */
	initTaskConfig = result->initTaskConfig;
	initTaskConfig->stdOut = SJME_NVM_TASK_PIPE_REDIRECT_TYPE_TERMINAL;
	initTaskConfig->stdErr = SJME_NVM_TASK_PIPE_REDIRECT_TYPE_TERMINAL;
	initTaskConfig->classPath = classPath;
	initTaskConfig->mainClass = result->bootParamCopy->mainClass;
	initTaskConfig->mainArgs = result->bootParamCopy->mainArgs;
	initTaskConfig->sysProps = result->bootParamCopy->sysProps;
	initTaskConfig->belay = result->bootParamCopy->belay;
	initTaskConfig->noOptimize = result->bootParamCopy->noOptimize;

	/* Only create the task if not belaying it. */
	initTask = NULL;
	if ((result->bootParamCopy->belay & SJME_NVM_BOOT_BELAY_TASK) == 0)
	{
		/* Spawn initial task which uses the main arguments. */
		if (sjme_error_is(error = sjme_nvm_task_taskNew(result,
			initTaskConfig, &initTask)) || initTask == NULL)
			goto fail_initTask;
	}
	
	/* Return newly created VM. */
	*outState = result;
	if (outInitTask != NULL)
		*outInitTask = initTask;
	return SJME_ERROR_NONE;

	/* Failed at specific points... */
fail_initTask:
fail_allocSchedule:
fail_badClassPath:
fail_defaultLaunch:
fail_suiteMerge:
fail_noSuites:
fail_payloadRom:
fail_bothIdAndName:
fail_bootParamCopy:
	if (result != NULL && result->bootParamCopy != NULL)
		sjme_alloc_free((void*)result->bootParamCopy);
fail_allocInitTaskConfig:
	if (result != NULL && result->initTaskConfig != NULL)
		sjme_alloc_free((void*)result->initTaskConfig);

fail_resultInit:
fail_resultAlloc:
	if (result != NULL)
		sjme_alloc_free(result);

fail_reservedPoolAlloc:

	/* Use whatever error code. */
	return sjme_error_defaultOr(error, SJME_ERROR_BOOT_FAILURE);
}

sjme_errorCode sjme_nvm_defaultBootSuite(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInNotNull const sjme_nal* nal,
	sjme_attrOutNotNull sjme_nvm_rom_suite* outSuite)
{
	sjme_errorCode error;
	sjme_cchar dataPath[SJME_MAX_PATH];
	
	if (allocPool == NULL || nal == NULL || outSuite == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* We cannot load if filesystem access is not supported. */
	if (nal->fileOpen == NULL)
		return sjme_error_notImplemented(0);
	
	/* Get default data directory. */
	memset(&dataPath, 0, sizeof(dataPath));
	if (sjme_error_is(error = sjme_nvm_defaultDir(
		SJME_NVM_DEFAULT_DIRECTORY_DATA, nal,
		dataPath, SJME_MAX_PATH - 1)))
		return sjme_error_default(error);

	/* Look in this directory. */
	return sjme_nvm_defaultBootSuiteInDirectory(allocPool, nal,
		dataPath, outSuite);
}

sjme_errorCode sjme_nvm_defaultBootSuiteInDirectory(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInNotNull const sjme_nal* nal,
	sjme_attrInNotNull sjme_lpcstr inDirectory,
	sjme_attrOutNotNull sjme_nvm_rom_suite* outSuite)
{
	sjme_errorCode error;
	sjme_jint i;
	sjme_nvm_rom_suite result;
	
	if (allocPool == NULL || nal == NULL || inDirectory == NULL ||
		outSuite == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* We cannot load if filesystem access is not supported. */
	if (nal->fileOpen == NULL)
		return sjme_error_notImplemented(0);
	
	/* There are multiple possible ROM names. */
	error = SJME_ERROR_UNKNOWN;
	for (i = 0; sjme_nvm_romNames[i]; i++)
	{
		/* Attempt ROM lookup. */
		if (sjme_error_is(error = sjme_nvm_defaultBootSuiteAttempt(
			allocPool, nal, &result, inDirectory,
			sjme_nvm_romNames[i],
			SJME_NVM_BOOT_CLUTTER_RELEASE)))
			continue;

		/* Success! */
		*outSuite = result;
		return SJME_ERROR_NONE;
	}

	/* Failed. */
	return sjme_error_defaultOr(error, SJME_ERROR_NO_SUITES);
}

sjme_errorCode sjme_nvm_defaultDir(
	sjme_attrInValue sjme_nvm_defaultDirectoryType type,
	sjme_attrInNotNull const sjme_nal* nal,
	sjme_attrOutNotNull sjme_lpstr outPath,
	sjme_attrInPositiveNonZero sjme_jint outPathLen)
{
	sjme_errorCode error;
	sjme_lpcstr useEnv, insteadSub;
	sjme_jint limit;
	sjme_lpstr work;
	
	if (nal == NULL || outPath == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (type <= SJME_NVM_DEFAULT_DIRECTORY_UNKNOWN ||
		type >= SJME_NVM_NUM_DEFAULT_DIRECTORY_TYPES)
		return SJME_ERROR_INVALID_ARGUMENT;
	
	if (outPathLen <= 0)
		return SJME_ERROR_INDEX_OUT_OF_BOUNDS;
	
	if (nal->getEnv == NULL)
		return sjme_error_notImplemented(0);
	
	/* Initialize. */
	limit = (SJME_MAX_PATH > outPathLen ? SJME_MAX_PATH : outPathLen);
	work = sjme_alloca(sizeof(*work) * limit);
	if (work == NULL)
		return SJME_ERROR_OUT_OF_MEMORY;
	memset(work, 0, sizeof(*work) * limit);
	
#if defined(SJME_CONFIG_HAS_OS_WINDOWS)
	if (1)
		sjme_todo("Impl?");
#elif defined(SJME_CONFIG_HAS_OS_PC_DOS)
	if (1)
		sjme_todo("Impl?");
		
#elif defined(SJME_CONFIG_HAS_OS_LINUX) || \
	defined(SJME_CONFIG_HAS_OS_BSD) || \
	defined(SJME_CONFIG_HAS_OS_MACOS) || \
	defined(SJME_CONFIG_HAS_OS_CYGWIN)
	
	/* Which are we interested in? */
	useEnv = NULL;
	insteadSub = NULL;
	switch (type)
	{
		case SJME_NVM_DEFAULT_DIRECTORY_CACHE:
			useEnv = "XDG_CACHE_HOME";
			insteadSub = ".cache";
			break;
			
		case SJME_NVM_DEFAULT_DIRECTORY_CONFIG:
			useEnv = "XDG_CONFIG_HOME";
			insteadSub = ".config";
			break;
			
		case SJME_NVM_DEFAULT_DIRECTORY_DATA:
			useEnv = "XDG_DATA_HOME";
			insteadSub = ".local/share";
			break;
			
		case SJME_NVM_DEFAULT_DIRECTORY_STATE:
			useEnv = "XDG_STATE_HOME";
			insteadSub = ".local/state";
			break;
		
			/* Unknown. */
		default:
			return SJME_ERROR_INVALID_ARGUMENT;
	}
	
	/* Check if environment variable is available. */
	if (sjme_error_is(error = nal->getEnv(
		work, limit - 1, useEnv)))
	{
		/* This is not considered an error, we just try something else. */
		if (error != SJME_ERROR_NO_SUCH_ELEMENT)
			return sjme_error_default(error);
		
		/* Get home variable instead, to add onto. */
		memset(work, 0, limit);
		if (sjme_error_is(error = nal->getEnv(
			work, limit - 1, "HOME")))
			return sjme_error_default(error);
		
#if 1
		sjme_todo("Impl?");
		return sjme_error_notImplemented(0);
#else
		/* Append subdirectory path. */
		if (sjme_error_is(error = sjme_path_resolveAppend(work,
			limit - 1, insteadSub, INT32_MAX)))
			return sjme_error_default(error);
#endif
	}
		
#else
	return sjme_error_notImplemented(0);
#endif

#if 1
	sjme_todo("Impl?");
	return sjme_error_notImplemented(0);
#else
	/* Append SquirrelJME on top. */
	if (sjme_error_is(error = sjme_path_resolveAppend(work,
		limit - 1, SJME_DIRECTORY_NAME, INT32_MAX)))
		return sjme_error_default(error);
#endif
	
	/* Is there enough room to fit? */
	limit = strlen(work) + 1;
	if (limit > outPathLen)
		return SJME_ERROR_PATH_TOO_LONG;
	
	/* Copy it over. */
	memmove(outPath, work, sizeof(*outPath) * limit);
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_destroy(sjme_nvm state, sjme_jint* exitCode)
{
	if (state == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	sjme_message("Implement NVM destroy");
	return SJME_ERROR_NONE;
#if 0
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
	return sjme_error_notImplemented(0);
#endif
}

sjme_errorCode sjme_nvm_parseCommandLine(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInNotNull const sjme_nal* nal,
	sjme_attrInOutNotNull sjme_nvm_bootParam* outParam,
	sjme_attrInPositiveNonZero sjme_jint argc,
	sjme_attrInNotNull sjme_lpcstr* argv)
{
	sjme_errorCode error;
	sjme_jint argAt;
	sjme_charSeqStatic argSeq;
	sjme_jboolean jarSpecified;
	sjme_nal_stdOFunc helpOut;
	sjme_nal_stdIoFlush helpFlush;
	sjme_lpcstr bootRom, helpOpt, versionOpt;
	
	if (allocPool == NULL || nal == NULL || outParam == NULL || argv == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	if (argc < 0)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Help defaults to standard error. */
	helpOut = nal->stdIo[SJME_NVM_MLE_STD_PIPE_STDERR].out;
	helpFlush = nal->stdIo[SJME_NVM_MLE_STD_PIPE_STDERR].flush;

	/* By default, no help or version is printed. */
	helpOpt = NULL;
	versionOpt = NULL;

	/* These arguments get filled in. */
	bootRom = NULL;
	
	/* Command line format is: */
	jarSpecified = SJME_JNI_FALSE;
	for (argAt = 1; argAt < argc; argAt++)
	{
		/* Setup sequence to wrap argument for parsing. */
		memset(&argSeq, 0, sizeof(argSeq));
		if (sjme_error_is(error = sjme_charSeq_newUtfStatic(
			&argSeq, argv[argAt], 0, -1)))
			return sjme_error_default(error);
		
		/* -version */
		if (sjme_charSeq_equalsUtfR(&argSeq,
				"-version") ||
			sjme_charSeq_equalsUtfR(&argSeq,
				"--version"))
		{
			versionOpt = argv[argAt];
		}
		
		/* -help */
		else if (sjme_charSeq_equalsUtfR(&argSeq,
				"-?") ||
			sjme_charSeq_equalsUtfR(&argSeq,
				"-h") ||
			sjme_charSeq_equalsUtfR(&argSeq,
				"-help") ||
			sjme_charSeq_equalsUtfR(&argSeq,
				"--help"))
		{
			helpOpt = argv[argAt];
		}
		
		/* -Xclutter:(release|debug) */
		else if (sjme_charSeq_startsWithUtfR(&argSeq,
			"-Xclutter:"))
		{
			/* Debugging? */
			if (!strcasecmp("debug", &argv[argAt][10]))
				outParam->clutterLevel = SJME_NVM_BOOT_CLUTTER_DEBUG;

			/* Otherwise, consider everything else release. */
			else
				outParam->clutterLevel = SJME_NVM_BOOT_CLUTTER_RELEASE;
		}
		
		/* -Xdebug */
		else if (sjme_charSeq_equalsUtfR(&argSeq,
			"-Xdebug"))
		{
			sjme_todo("Impl? %s", argv[argAt]);
		}
		
		/* -Xemulator:(vm) */
		else if (sjme_charSeq_startsWithUtfR(&argSeq,
			"-Xemulator:"))
		{
			/* If SpringCoat is specified, assume no optimizations. */
			if (!strcasecmp("springcoat", &argv[argAt][11]))
				outParam->noOptimize = SJME_JNI_TRUE;
		}
		
		/* -Xentry:id */
		else if (sjme_charSeq_startsWithUtfR(&argSeq,
			"-Xentry:"))
		{
			sjme_todo("Impl? %s", argv[argAt]);
		}
		
		/* -Xjdwp:[hostname]:port */
		else if (sjme_charSeq_startsWithUtfR(&argSeq,
			"-Xjdwp:"))
		{
			sjme_todo("Impl? %s", argv[argAt]);
		}
		
		/* -Xrom:(path) */
		else if (sjme_charSeq_startsWithUtfR(&argSeq,
			"-Xrom:"))
		{
			/* Can only be set once! */
			if (bootRom != NULL)
				return SJME_ERROR_INVALID_ARGUMENT;

			/* Set the boot ROM. */
			bootRom = &argv[argAt][6];
		}
		
		/* -Xlibraries:(class:path:...) */
		else if (sjme_charSeq_startsWithUtfR(&argSeq,
			"-Xlibraries:"))
		{
			sjme_todo("Impl? %s", argv[argAt]);
		}
		
		/* -Xscritchui:(ui) */
		else if (sjme_charSeq_startsWithUtfR(&argSeq,
			"-Xscritchui:"))
		{
			sjme_todo("Impl? %s", argv[argAt]);
		}
		
		/* -Xsnapshot:(path-to-nps) */
		else if (sjme_charSeq_startsWithUtfR(&argSeq,
			"-Xsnapshot:"))
		{
			sjme_todo("Impl? %s", argv[argAt]);
		}
		
		/* -Xthread:(single|coop|multi|smt) */
		else if (sjme_charSeq_startsWithUtfR(&argSeq,
			"-Xthread:"))
		{
			sjme_todo("Impl? %s", argv[argAt]);
		}
		
		/* -Xtrace:(flag|...) */
		else if (sjme_charSeq_startsWithUtfR(&argSeq,
			"-Xtrace:"))
		{
			sjme_todo("Impl? %s", argv[argAt]);
		}
		
		/* -Dsysprop=value */
		else if (sjme_charSeq_startsWithUtfR(&argSeq,
			"-D"))
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
			outParam->noOptimize = SJME_JNI_TRUE;
		}
		
		/* Ignored options. */
		else if (sjme_charSeq_equalsUtfR(&argSeq, "-client") ||
			sjme_charSeq_equalsUtfR(&argSeq, "-server") ||
			sjme_charSeq_equalsUtfR(&argSeq, "-XstartOnFirstThread"))
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
		
		/* Invalid, fail. */
		else
		{
			sjme_message("Invalid command line: %s",
				argv[argAt]);
			
			return SJME_ERROR_INVALID_ARGUMENT;
		}
	}
	
	/* Launching a specific Jar? */
	if (argAt < argc)
	{
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
	}
	
	/* Default launching. */
	else
	{
		outParam->mainArgs = NULL;
		outParam->mainClass = NULL;
	}

	/* Print help options or version? */
	if (helpOpt != NULL)
		return sjme_nvm_printHelp(nal, helpOut, helpFlush,
			helpOpt, argv[0]);
	else if (versionOpt != NULL)
		return sjme_nvm_printVersion(nal, helpOut, helpFlush,
			versionOpt, outParam);
	
	/* Load boot ROM? */
	if (bootRom != NULL)
		if (sjme_error_is(error = sjme_nvm_defaultBootSuiteAttempt(
			allocPool, nal, &outParam->bootSuite,
			"", bootRom, outParam->clutterLevel)) ||
			outParam->bootSuite == NULL)
			return sjme_error_default(error);
	
	/* Success! */
	return SJME_ERROR_NONE;
}
