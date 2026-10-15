/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/nvm/boot.h"
#include "sjme/debug.h"
#include "sjme/nvm/nvm.h"
#include "sjme/nvm/task.h"
#include "sjme/charSeq.h"
#include "sjme/native.h"
#include "sjme/nvm/cleanup.h"
#include "sjme/path.h"
#include "sjme/joptarg.h"
#include "sjme/externalWeak.h"
#include "sjme/nvm/romMeepSwm.h"
#include "sjme/nvm/jdwp.h"

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

static const sjme_joptarg_helpParam sjme_joptarg_helpParams[] =
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
	sjme_attrInNotNull const sjme_path* basePath,
	sjme_attrInNotNull sjme_lpcstr romName,
	sjme_attrInValue sjme_nvm_bootClutterLevel clutterLevel)
{
	sjme_errorCode error;
	sjme_seekable rom;
	sjme_nvm_rom_suite result;
	sjme_path checkPath;

	if (allocPool == NULL || nal == NULL || outSuite == NULL ||
		(basePath == NULL && romName == NULL))
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Cannot open files? */
	if (nal->fileOpen == NULL)
		return SJME_ERROR_FILE_NOT_FOUND;

	/* Determine path to check. */
	memset(&checkPath, 0, sizeof(checkPath));

	/* Base path first, if any. */
	if (basePath != NULL)
		if (sjme_error_is(error = sjme_path_resolveP(
			&checkPath, basePath)))
			return sjme_error_default(error);
	
	/* Then any ROM which may be directly specified. */
	if (romName != NULL && strlen(romName) > 0)
		if (sjme_error_is(error = sjme_path_resolveS(
			&checkPath, romName)))
			return sjme_error_default(error);
	
	/* Open main ROM file. */
	rom = NULL;
	if (sjme_error_is(error = nal->fileOpen(allocPool, checkPath.chars,
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

#if defined(SJME_CONFIG_DEBUG)
static sjme_jint sjme_nvm_pointerId(
	sjme_attrInNotNull sjme_pointer p)
{
	sjme_nvm_structType typeId;

	/* Try our best to ensure the type is valid. */
	typeId = sjme_nvm_typeOf(p);
	if (typeId > SJME_NVM_STRUCT_UNKNOWN &&
		typeId < SJME_NVM_NUM_STRUCT)
		return typeId;
	return -1;
}
#endif

static sjme_errorCode sjme_nvm_printHelp(
	sjme_attrInNotNull const sjme_nal* nal,
	sjme_attrInNotNull sjme_nal_stdOFunc helpOut,
	sjme_attrInNotNull sjme_nal_stdIoFlush helpFlush,
	sjme_attrInNotNull sjme_lpcstr argSeq,
	sjme_attrInNotNull sjme_lpcstr programName)
{
	const sjme_joptarg_helpParam* help;

	if (nal == NULL || helpOut == NULL ||
		argSeq == NULL || programName == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Where is this information going? */
	if (!strcmp(argSeq, "--help"))
	{
		helpOut = nal->stdIo[SJME_NVM_MLE_STD_PIPE_STDOUT].out;
		helpFlush = nal->stdIo[SJME_NVM_MLE_STD_PIPE_STDOUT].flush;

		/* Printing nowhere? */
		if (helpOut == NULL)
			return SJME_ERROR_EXIT;
	}
	
	/* Normal usage. */
	sjme_nal_stdF(helpOut,
		"Usage: %s [Options] <MainClass> [Args...]\n", programName);
	sjme_nal_stdF(helpOut,
		"Usage: %s [Options] -jar <Jar> [Args...]\n", programName);
	sjme_nal_stdF(helpOut,"\n");
	
	/* And all the help parameters. */
	sjme_nal_stdF(helpOut, "Options are:\n");
	for (help = &sjme_joptarg_helpParams[0]; help->arg != NULL; help++)
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
	if (nal == NULL || helpOut == NULL ||
		argSeq == NULL || outParam == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Where is this information going? */
	if (!strcmp(argSeq, "--version"))
	{
		helpOut = nal->stdIo[SJME_NVM_MLE_STD_PIPE_STDOUT].out;
		helpFlush = nal->stdIo[SJME_NVM_MLE_STD_PIPE_STDOUT].flush;

		/* Printing nowhere? */
		if (helpOut == NULL)
			return SJME_ERROR_EXIT;
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

/**
 * Initializes ScritchUI so that it can be used by the virtual machine, this
 * is done as early as possible so that the UI can be used immediately. This is
 * needed by macOS due to threading and event handling issues, as there
 * traditionally always has been @code -XstartOnFirstThread @endcode. This
 * parameter should technically always apply.
 *
 * @param inState The virtual machine state.
 * @param prefer The optional interface to prefer.
 * @return Any resultant error, if any.
 * @since 2026/09/20
 */
static sjme_errorCode sjme_nvm_initScritchUi(
	sjme_attrInNotNull sjme_nvm inState,
	sjme_attrInNullable sjme_lpcstr prefer)
{
#define BUF_SIZE 64
	sjme_errorCode error;
	sjme_scritchui result;
#if !defined(SJME_CONFIG_HAS_NO_DYLIB_SUPPORT)
	sjme_dylib handle;
	sjme_cchar buf[BUF_SIZE];
	sjme_scritchui_dylibApiFunc apiInit;
	sjme_lpcstr actualSubComponent;
#endif

	if (inState == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Is there a hook to initialize ScritchUI? */
	result = NULL;
	if (inState->hooks != NULL && inState->hooks->scritchUi != NULL)
	{
		/* Call the hook. Note if the hook is set and there is a headless */
		/* error, then we do not want to perform any default initialization */
		/* as there may be a reason why a hook is passed. */
		if (sjme_error_is(error = inState->hooks->scritchUi(inState, &result)))
			return sjme_error_default(error);

		/* Hook call is valid? */
		if (result != NULL)
		{
			/* Use this as the ScritchUI state. */
			sjme_atomic_s(sjme_pointer, &inState->globals.scritchUi,
				sjme_weakUpR(sjme_pointer, result));
			return SJME_ERROR_NONE;
		}
	}

#if !defined(SJME_CONFIG_HAS_NO_DYLIB_SUPPORT)
	/* Load in the ScritchUI library that we find first. */
	actualSubComponent = NULL;
	if (sjme_error_is(error = sjme_dylib_openExtra(
		inState->nal, SJME_DYLIB_EXTRA_FAMILY_SCRITCHUI, NULL, &handle,
		&actualSubComponent)) ||
		handle == NULL)
	{
		if (error == SJME_ERROR_COULD_NOT_LOAD_LIBRARY ||
			error == SJME_ERROR_LIBRARY_NOT_FOUND)
			return SJME_ERROR_HEADLESS_DISPLAY;

		return sjme_error_default(error);
	}

	/* What is the API function entrypoint called? */
	memset(&buf, 0, sizeof(buf));
	snprintf(buf, BUF_SIZE - 1,
	SJME_TOKEN_STRING_PP(SJME_SCRITCHUI_DYLIB_SYMBOL()) "%s",
		(actualSubComponent != NULL ? actualSubComponent : ""));

	/* Lookup the function pointer for the call. */
	apiInit = NULL;
	if (sjme_error_is(error = sjme_dylib_lookup(handle, buf,
		(sjme_pointer*)&apiInit)) || apiInit == NULL)
	{
		/* Some other library is here instead, or this is the wrong */
		/* type of interface library? */
		if (error == SJME_ERROR_INVALID_LIBRARY_SYMBOL)
		{
			/* Close before failing. */
			if (sjme_error_is(error = sjme_dylib_close(handle)))
				return sjme_error_default(error);
			return SJME_ERROR_HEADLESS_DISPLAY;
		}

		/* Some other failure. */
		goto fail_lookup;
	}

	/* Attempt initialization call. */
	/* Note that we do not need to bind the event thread to anything JNI */
	/* or otherwise, because we are the JVM! Yay! */
	result = NULL;
	if (sjme_error_is(error = apiInit(inState->allocPool, &result,
		NULL, NULL, NULL)))
		goto fail_initApi;

	/* Success! */
	sjme_atomic_s(sjme_pointer, &inState->globals.scritchUi, result);
	sjme_atomic_s(sjme_pointer, &inState->globals.scritchUiLib,
		handle);
	return SJME_ERROR_NONE;

fail_initApi:
fail_lookup:
fail_open:
	if (handle != NULL)
		sjme_dylib_close(handle);
	return sjme_error_default(error);
#else
	/* No dynamic library support. */
	return SJME_ERROR_HEADLESS_DISPLAY;
#endif

#undef BUF_SIZE
}

sjme_errorCode sjme_nvm_boot(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInNotNull const sjme_nvm_bootParam* param,
	sjme_attrOutNotNull sjme_nvm* outState,
	sjme_attrOutNullable sjme_nvm_task* outInitTask)
{
#define FIXED_SUITE_COUNT 16
	sjme_errorCode error, deferRunJar;
	sjme_nvm result;
	sjme_nvm_rom_suite mergeSuites[FIXED_SUITE_COUNT];
	sjme_jint numMergeSuites, i, n;
	sjme_nvm_task_taskNewConfig* initTaskConfig;
	const sjme_nvm_bootParam* bootParamCopy;
	sjme_nvm_task initTask;
	sjme_list(sjme_nvm_rom_library)* classPath;
	sjme_jlong yieldIn, yieldOut;
	sjme_nvm_rom_suite jarSuite;
	sjme_nvm_rom_library jarLibrary;
	sjme_path runJarPath;
	sjme_scritchui scritchUi;
	sjme_jboolean cancelScritchUi;
	
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
	result->hooks = param->hooks;
	result->hookData = param->hookData;
	
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

	/* Is a Jar being run? We need to make sure it is actually loaded in */
	/* otherwise we cannot use it. */
	deferRunJar = SJME_ERROR_NONE;
	if (result->bootParamCopy->runJar != NULL)
	{
		/* Resolve the path first. */
		memset(&runJarPath, 0, sizeof(runJarPath));
		if (sjme_error_is(sjme_path_resolveS(&runJarPath,
			result->bootParamCopy->runJar)))
			goto fail_invalidJarPath;

		/* It is possible that loading the library will fail, such as the */
		/* file not existing. Defer the error for later. */
		jarSuite = NULL;
		if (sjme_error_is(error = sjme_nvm_rom_suiteFromZipFileSingle(
			allocPool, &jarSuite, result->nal, &runJarPath)) ||
			jarSuite == NULL)
			deferRunJar = error;

		/* It did actually load. */
		else
			mergeSuites[numMergeSuites++] = jarSuite;
	}

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

	/* If we are running a specific Jar, since we have all the dependency */
	/* info loaded, and otherwise, we can look up the Jar to run and setup */
	/* the classpath that it needs to start. */
	if (bootParamCopy->runJar != NULL &&
		0 != strcmp("", bootParamCopy->runJar))
	{
		/* It is possible the Jar passed via -jar does not exist or is just */
		/* broken. */
		jarLibrary = NULL;
		if (sjme_error_is(error = sjme_nvm_rom_resolveLibraryByName(
			result->suite, bootParamCopy->runJar, &jarLibrary)))
		{
			/* If there was an error from the defer, then use that instead. */
			if (sjme_error_is(deferRunJar))
				error = deferRunJar;
			goto fail_resolveJar;
		}

		/* We have to load the MEEP SWM dependency information for our */
		/* entire suite of libraries so that dependency resolution works */
		/* properly. We only need this for -jar usage. */
		result->swmManager = NULL;
		if (sjme_error_is(error = sjme_nvm_rom_swmLoad(allocPool,
			result->suite, &result->swmManager)) ||
			result->swmManager == NULL)
			goto fail_loadMeepSwm;

		/* Now that we have, hopefully, loaded all the MEEP SWM */
		/* dependency information we can perform an actual lookup of */
		/* whatever was passed via -jar. */
		if (sjme_error_is(error = sjme_nvm_rom_swmResolve(result->swmManager,
			jarLibrary,
			(sjme_lpstr*)&bootParamCopy->mainClass,
			(sjme_list(sjme_lpstr)**)&bootParamCopy->mainArgs,
			(sjme_list(sjme_jint)**)&bootParamCopy->mainClassPathById,
			(sjme_list(sjme_lpstr)**)
				&bootParamCopy->mainClassPathByName)))
			goto fail_resolveJarClasspath;
	}

	/* Use the classpath of the launcher? If enabled and nothing is */
	/* being launched? */
	if (bootParamCopy->launcherFallback &&
		(bootParamCopy->mainClassPathById == NULL &&
		bootParamCopy->mainClassPathByName == NULL))
	{
		/* Determine the default boot suite. */
		if (sjme_error_is(error = sjme_nvm_rom_suiteDefaultLaunch(allocPool,
			result->suite,
			(sjme_lpstr*)&bootParamCopy->mainClass,
			(sjme_list(sjme_lpstr)**)&bootParamCopy->mainArgs,
			(sjme_list(sjme_jint)**)&bootParamCopy->mainClassPathById,
			(sjme_list(sjme_lpstr)**)
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

		/* Fail. */
		goto fail_badClassPath;
	}
	
	/* Count up all classpath entries as we are using them now. */
	for (n = classPath->length, i = 0; i < n; i++)
		sjme_weakUp(classPath->elements[i]);

	/* Allocate the task scheduler, if applicable. */
	if (result->threadModel != SJME_NVM_MLE_THREAD_MULTI)
	{
		/* Allocate. */
		if (sjme_error_is(error = sjme_alloc(allocPool,
			sizeof(*result->schedule), (sjme_pointer*)&result->schedule)) ||
			result->schedule == NULL)
			goto fail_allocSchedule;
		
		/* Determine the number of yields that occur for a very small slice */
		/* of time. */
		memset(&yieldIn, 0, sizeof(yieldIn));
		memset(&yieldOut, 0, sizeof(yieldOut));
		result->nal->nanoTime(&yieldIn);
		for (i = 0; i >= 0; i++)
		{
			/* Yield. */
			sjme_thread_yield();
			
			/* How much time has passed? */
			result->nal->nanoTime(&yieldOut);
			if ((yieldOut.full - yieldIn.full) >= INT64_C(100000000))
				break;
		}
		
		/* We want to yield up to this point. */
		result->schedule->yieldTimer = 0;
		result->schedule->yieldMax = i;
		
		/* Then transition to actual sleeps. */
		result->schedule->nothingMillis = 100;
		result->schedule->nothingNanos = 0;
	}

	/* Setup task details. */
	initTaskConfig = (sjme_nvm_task_taskNewConfig*)result->initTaskConfig;
	initTaskConfig->stdOut = SJME_NVM_TASK_PIPE_REDIRECT_TYPE_TERMINAL;
	initTaskConfig->stdErr = SJME_NVM_TASK_PIPE_REDIRECT_TYPE_TERMINAL;
	initTaskConfig->mainClass = result->bootParamCopy->mainClass;
	initTaskConfig->mainArgs = result->bootParamCopy->mainArgs;
	initTaskConfig->sysProps = result->bootParamCopy->sysProps;
	initTaskConfig->belay = result->bootParamCopy->belay;
	initTaskConfig->noOptimize = result->bootParamCopy->noOptimize;
	initTaskConfig->classPath = classPath;

	/* Initialize ScritchUI? Or is this headless by default? */
	if (result->bootParamCopy->noScritchUi)
		sjme_atomic_s(sjme_jint, &result->globals.headlessDisplay, 1);
	else
	{
		/* Try initializing ScritchUI. */
		if (sjme_error_is(error = sjme_nvm_initScritchUi(result,
			bootParamCopy->preferScritchUi)))
		{
			/* This could be an actual headless system. */
			if (error != SJME_ERROR_HEADLESS_DISPLAY)
				goto fail_scritchUiInit;

			/* Set that this is a headless system. */
			sjme_atomic_s(sjme_jint, &result->globals.headlessDisplay, 1);
		}

		/* Get whatever ScritchUI as initialized. */
		scritchUi = sjme_atomic_g(sjme_pointer, &result->globals.scritchUi);

		/* Sanity checks to determine if ScritchUI can be used. */
		cancelScritchUi = SJME_JNI_FALSE;
		if (scritchUi != NULL)
		{
#if defined(SJME_CONFIG_ONLY_THREAD_SINGLE)
			/* Was ScritchUI initialized, and we ended up in a very */
			/* difficult threading combination that effectively will make */
			/* ScritchUI pretty useless? */
			/* Note that multithreaded NanoCoat can use a single-threaded */
			/* ScritchUI, just that it will have limited interface support */
			/* as most systems expect multiple threads. */
			if (!scritchUi->bugs.onlyThreadSingle)
				cancelScritchUi = SJME_JNI_TRUE;
#endif
		}

		/* This was determined to be true, we must cancel ScritchUI. */
		if (cancelScritchUi)
		{
			/* Clear reference to it. */
			sjme_atomic_s(sjme_pointer, &result->globals.scritchUi,
				NULL);

			/* Count down, if this does reach zero then no other virtual */
			/* machine is using ScritchUI. */
			if (sjme_error_is(error = sjme_closeable_close(
				SJME_AS_CLOSEABLE(scritchUi))))
				goto fail_scritchUiDeInit;
		}
	}

	/* Only create the task if not belaying it. */
	initTask = NULL;
	if ((result->bootParamCopy->belay & SJME_NVM_BOOT_BELAY_TASK) == 0)
	{
		/* Spawn initial task which uses the main arguments. */
		if (sjme_error_is(error = sjme_nvm_task_taskNew(result,
			initTaskConfig, &initTask)) || initTask == NULL)
			goto fail_initTask;
	}

#if SJME_CONFIG_DEBUG_VERBOSE && !defined(SJME_CONFIG_NETWORK_NONE)
	/* Using JDWP for this virtual machine? */
	if (bootParamCopy->jdwpPort > 0 || bootParamCopy->jdwpAddress != NULL ||
		bootParamCopy->jdwpListening)
		if (sjme_error_is(error = sjme_jdwp_sessionNewTcpNetwork(allocPool,
			result, &result->jdwp,
			bootParamCopy->jdwpListening,
			bootParamCopy->jdwpAddress,
			bootParamCopy->jdwpPort)) || result->jdwp == NULL)
			sjme_message("Failed to establish JDWP connection: %d", error);
#endif

#if defined(SJME_CONFIG_DEBUG)
	/* If debugging, set the pointer ID type. */
	if (allocPool->pointerIdType == NULL)
		allocPool->pointerIdType = sjme_nvm_pointerId;
#endif
	
	/* Return newly created VM. */
	*outState = result;
	if (outInitTask != NULL)
		*outInitTask = initTask;
	return SJME_ERROR_NONE;

	/* Failed at specific points... */
fail_initTask:
fail_scritchUiDeInit:
fail_scritchUiInit:
fail_allocSchedule:
fail_badClassPath:
fail_defaultLaunch:
fail_resolveJarClasspath:
fail_loadMeepSwm:
	if (result != NULL && result->swmManager != NULL)
		sjme_closeable_close(SJME_AS_CLOSEABLE(result->swmManager));
fail_resolveJar:
fail_suiteMerge:
fail_noSuites:
fail_invalidJarPath:
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
	sjme_path dataPath;
	
	if (allocPool == NULL || nal == NULL || outSuite == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* We cannot load if filesystem access is not supported. */
	if (nal->fileOpen == NULL)
		return sjme_error_notImplemented(0);

	/* Debug. */
	sjme_message("Looking for default boot suite...");

	/* There may be a runtime specified directory, which may be used by */
	/* front-ends to change where libraries exist. */
	memset(&dataPath, 0, sizeof(dataPath));
	if (sjme_error_is(error = sjme_path_default(
		nal, &dataPath, SJME_NVM_DEFAULT_DIRECTORY_RUNTIME, -1)) ||
		dataPath.chars[0] == '\0')
	{
		/* Some other error. */
		if (error != SJME_ERROR_PATH_NOT_DEFINED)
			return sjme_error_default(error);

		/* Otherwise, use the default data directory. */
		memset(&dataPath, 0, sizeof(dataPath));
		if (sjme_error_is(error = sjme_path_default(
			nal, &dataPath, SJME_NVM_DEFAULT_DIRECTORY_DATA,
			-1)) ||
			dataPath.chars[0] == '\0')
			return sjme_error_default(error);
	}

	/* Debug. */
	sjme_message("Looking for boot suite in `%s`...",
		dataPath.chars);

	/* Look in this directory. */
	return sjme_nvm_defaultBootSuiteInDirectory(allocPool, nal,
		&dataPath, outSuite);
}

sjme_errorCode sjme_nvm_defaultBootSuiteInDirectory(
	sjme_attrInNotNull sjme_alloc_pool allocPool,
	sjme_attrInNotNull const sjme_nal* nal,
	sjme_attrInNotNull const sjme_path* inDirectory,
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
		result = NULL;
		if (sjme_error_is(error = sjme_nvm_defaultBootSuiteAttempt(
			allocPool, nal, &result, inDirectory,
			sjme_nvm_romNames[i],
			SJME_NVM_BOOT_CLUTTER_RELEASE)) || result == NULL)
		{
			if (error != SJME_ERROR_FILE_NOT_FOUND)
				return sjme_error_default(error);
			continue;
		}

		/* Success! */
		*outSuite = result;
		return SJME_ERROR_NONE;
	}

	/* Failed. */
	return SJME_ERROR_NO_SUITES;
}

sjme_errorCode sjme_nvm_destroy(
	sjme_attrInNotNull sjme_nvm state,
	sjme_attrOutNullable sjme_jint* exitCode)
{
	if (state == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Copy out the exit code, if requested. */
	if (exitCode != NULL)
		*exitCode = sjme_atomic_g(sjme_jint, &state->mainExitCode);
	
	/* Forward to the normal cleanup process. */
	return sjme_closeable_close(SJME_AS_CLOSEABLE(state));
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
	sjme_jboolean jarSpecified, runViaMain;
	sjme_nal_stdOFunc helpOut;
	sjme_nal_stdIoFlush helpFlush;
	sjme_lpcstr bootRom, helpOpt, versionOpt, tempUtf, tempTwo, runJar;
	sjme_path path;
	
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
	runJar = NULL;
	
	/* Command line format is: */
	/* Note that this can possible start at argument zero. */
	jarSpecified = SJME_JNI_FALSE;
	for (argAt = (outParam->startAtArgZero ? 0 : 1);
		argAt < argc && !jarSpecified; argAt++)
	{
		/* Cannot have a null argument here. */
		if (argv[argAt] == NULL)
			return SJME_ERROR_NULL_ARGUMENTS;

		/* Stop parsing if it does not start with a dash, as this is not */
		/* an argument. */
		if (argv[argAt][0] != '-')
			break;

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
			if (0 == strcasecmp("debug", &argv[argAt][10]))
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
			if (0 == strcasecmp("springcoat", &argv[argAt][11]))
				outParam->noOptimize = SJME_JNI_TRUE;

			/* Otherwise, if no nanocoat then fail... */
			else if (0 != strcasecmp("nanocoat", &argv[argAt][11]))
				return SJME_ERROR_INVALID_ARGUMENT;
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
			/* Simpler to use as UTF. */
			tempUtf = sjme_charSeq_tempUtf(&argSeq);
			
			/* Only the port specified? We are listening... */
			if (sjme_charSeq_charAtR(&argSeq,
				strlen("-Xjdwp:") == ':'))
			{
				outParam->jdwpListening = SJME_JNI_TRUE;
				outParam->jdwpAddress = NULL;
				outParam->jdwpPort = atoi(&tempUtf[strlen("-Xjdwp:")]);
			}

			/* Otherwise we are connecting to an address. */
			else
			{
				/* Find the last colon, in the event of IPv6. */
				tempTwo = strrchr(tempUtf, ':');
				if (tempTwo == NULL)
					return SJME_ERROR_INVALID_ARGUMENT;

				/* Duplicate address. */
				if (sjme_error_is(error = sjme_alloc_strdup(allocPool,
					&outParam->jdwpAddress, tempUtf)))
					return sjme_error_default(error);
				
				/* Fill in. */
				outParam->jdwpListening = SJME_JNI_FALSE;
				outParam->jdwpAddress[tempTwo - tempUtf] = '\0';
				outParam->jdwpPort = atoi(&tempTwo[1]);
			}
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
			/* Force headless? */
			if (0 == strcasecmp("none", &argv[argAt][12]))
				outParam->noScritchUi = SJME_JNI_TRUE;
			else
				outParam->preferScritchUi = &argv[argAt][12];
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
			/* Another argument needs to follow! */
			if ((argAt + 1) >= argc)
			{
				/* Should hopefully help the user. */
				sjme_message("A jar must follow -jar. (%s %s %d %d)",
					argv[argAt], argv[argAt + 1], argAt, argc);

				/* Fail. */
				return SJME_ERROR_INVALID_ARGUMENT;
			}

			/* We are using a Jar now. */
			/* Anything that follows the name of the Jar is a main argument. */
			jarSpecified = SJME_JNI_TRUE;

			/* Set the Jar to run. */
			runJar = &argv[++argAt][0];
		}
		
		/* Invalid, fail. */
		else
		{
			sjme_message("Invalid command line: %s",
				argv[argAt]);
			
			return SJME_ERROR_INVALID_ARGUMENT;
		}
	}

	/* Main-class, if not -jar and there are arguments to pass */
	runViaMain = SJME_JNI_FALSE;
	if (!jarSpecified && argAt < argc)
	{
		/* We are running via main now. */
		runViaMain = SJME_JNI_TRUE;

		sjme_todo("impl?");
	}

	/* Arguments to main or -jar? */
	if (runViaMain || jarSpecified)
		for (; argAt < argc; argAt++)
		{
			sjme_todo("impl?");
		}

	/* Default launch if not running a main class or using -jar. */
	if (!runViaMain && !jarSpecified)
	{
		outParam->mainArgs = NULL;
		outParam->mainClass = NULL;
	}

	/* Print help options or version? */
	if (helpOpt != NULL || versionOpt != NULL)
	{
		/* Cannot actually print help text anywhere? */
		if (helpOut == NULL)
			return SJME_ERROR_EXIT;

		/* Now print. */
		if (helpOpt != NULL)
			return sjme_nvm_printHelp(nal, helpOut, helpFlush,
				helpOpt, argv[0]);
		else if (versionOpt != NULL)
			return sjme_nvm_printVersion(nal, helpOut, helpFlush,
				versionOpt, outParam);
	}

	/* No boot ROM was specified? Try to find a default one. */
	if (bootRom == NULL)
	{
		/* This goes through and tries multiple ROM names to try to find */
		/* one that works. */
		if (sjme_error_is(error = sjme_nvm_defaultBootSuite(
			allocPool, nal, &outParam->bootSuite)))
			return sjme_error_default(error);
	}
	
	/* Otherwise, attempt to load the specific ROM. */
	else
	{
		/* Resolve a "blank" path. */
		memset(&path, 0, sizeof(path));
		if (sjme_error_is(error = sjme_path_resolveS(&path, "")))
			return sjme_error_default(error);

		/* Just use a "normal" attempt which directly sets and uses the */
		/* path that was specified. */
		if (sjme_error_is(error = sjme_nvm_defaultBootSuiteAttempt(
			allocPool, nal, &outParam->bootSuite,
			&path, bootRom, outParam->clutterLevel)) ||
			outParam->bootSuite == NULL)
			return sjme_error_default(error);
	}

	/* Never fallback to the launcher if main class or -jar were used. */
	if (jarSpecified || runViaMain)
		outParam->launcherFallback = SJME_JNI_FALSE;

	/* Set Jar to be used? */
	if (jarSpecified)
		outParam->runJar = runJar;
	
	/* Success! */
	return SJME_ERROR_NONE;
}
