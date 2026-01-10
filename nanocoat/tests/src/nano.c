/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <string.h>

#include "sjme/nvm/task.h"
#include "sjme/nvm/boot.h"
#include "sjme/nvm/nvm.h"
#include "sjme/nvm/nvmFunc.h"
#include "sjme/nvm/payload.h"
#include "sjme/nvm/loop.h"
#include "sjme/nvm/mleShelves.h"
#include "test.h"

SJME_NVM_MLE_SHELF_DECLARE(NanoShelf) =
{
#if 0
	SJME_NVM_MLE_DEFINE(stringCharAt,
		SJME_MD(SJME_MD_C, SJME_MD_STRING SJME_MD_I),
		"I", "LI"),
	SJME_NVM_MLE_DEFINE(stringEquals,
		SJME_MD(SJME_MD_Z, SJME_MD_STRING SJME_MD_STRING),
		"I", "LL"),
	SJME_NVM_MLE_DEFINE(stringHash,
		SJME_MD(SJME_MD_I, SJME_MD_STRING),
		"I", "L"),
	SJME_NVM_MLE_DEFINE_ALT(stringInit, chars,
		SJME_MD(SJME_MD_V, SJME_MD_STRING SJME_MD_AC SJME_MD_I SJME_MD_I),
		"V", "LLII"),
	SJME_NVM_MLE_DEFINE_ALT(stringInit, emptyOrThis,
		SJME_MD(SJME_MD_V, SJME_MD_STRING),
		"V", "L"),
	SJME_NVM_MLE_DEFINE_ALT(stringInit, string,
		SJME_MD(SJME_MD_V, SJME_MD_STRING SJME_MD_STRING),
		"V", "LL"),
	SJME_NVM_MLE_DEFINE(stringIsIntern,
		SJME_MD(SJME_MD_Z, SJME_MD_STRING),
		"I", "L"),
	SJME_NVM_MLE_DEFINE(stringLength,
		SJME_MD(SJME_MD_I, SJME_MD_STRING),
		"I", "L"),
	SJME_NVM_MLE_DEFINE(stringToChar,
		SJME_MD(SJME_MD_V, SJME_MD_STRING SJME_MD_I
			SJME_MD_AC SJME_MD_I SJME_MD_I),
		"V", "LILII"),
	SJME_NVM_MLE_DEFINE_ALT(stringValueOf, chars,
		SJME_MD(SJME_MD_STRING, SJME_MD_Z SJME_MD_AC SJME_MD_I SJME_MD_I),
		"L", "ILII"),
	SJME_NVM_MLE_DEFINE_ALT(stringValueOf, string,
		SJME_MD(SJME_MD_STRING, SJME_MD_Z SJME_MD_STRING),
		"L", "IL"),
#endif
	
	SJME_NVM_MLE_STOP()
};

typedef struct sjme_test_nano_result
{
	/** Was this captured? */
	sjme_jboolean captured;

	/** The value captured. */
	sjme_jvalueTyped value;
} sjme_test_nano_result;

static sjme_errorCode sjme_test_nano_nativeCall(
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrInNotNull sjme_jmethodID methodID,
	sjme_attrInNotNull sjme_nvm_class_methodInfo methodInfo,
	sjme_attrInNotNull sjme_jvalueTyped* argR,
	sjme_attrInPositive sjme_jint argC,
	sjme_attrInNullable sjme_jvalueTyped* argV)
{
	sjme_test_nano_result* result;
	
	if (inFrame == NULL || methodID == NULL || argR == NULL || argR == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (argC < 0)
		return SJME_ERROR_INVALID_ARGUMENT;

	if (argC > 0 && argV == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Recover result. */
	result = SJME_F_S(inFrame)->hookData;
	if (result == NULL)
		return SJME_ERROR_ILLEGAL_STATE;

	/* Is this the correct class and method? */
	if (!sjme_charSeq_equalsUtfR(sjme_atomic_g(sjme_nvm_class_info,
		&methodInfo->inClass)->name->seq, "nano/NanoShelf"))
		return SJME_ERROR_UNKNOWN_NATIVE_FUNCTION;
	
	/* Forward MLE call. */
	return sjme_mle_mleCallShelfM(inFrame, &sjme_nvm_mleNanoShelf[0],
		methodID->member.name->seq,
		methodID->member.type->seq,
		argR, argC, argV);
#if 0
	/* Wrong argument count? */
	if (argC != 0 && argC != 1)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Was a result already captured? */
	if (result->captured)
		return SJME_ERROR_ILLEGAL_STATE;

	/* Capture data. */
	result->captured = SJME_JNI_TRUE;
	if (argC == 1)
		memmove(&result->value, &argV[0], sizeof(argV[0]));

	/* Success! */
	return SJME_ERROR_NONE;
#endif
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
#define BUF_SIZE 128
	sjme_errorCode error;
	sjme_alloc_pool runPool, paramPool;
	sjme_nvm_bootParam bootParam;
	sjme_jint exitCode, i, n;
	sjme_seekable bootSeek;
	sjme_nvm_rom_suite bootSuite;
	sjme_list(sjme_lpstr)* classpath;
	sjme_list(sjme_lpstr)* mainArgs;
	sjme_nvm inState;
	sjme_jboolean terminated;
	const sjme_nal* nal;
	sjme_lpstr classpathSplice;
	sjme_test_nano_result result;
	sjme_jint allocCount;
	sjme_jclass mainClass;
	sjme_nvm_task mainTask;
	sjme_cchar mainName[BUF_SIZE];
	sjme_nvm_class_fieldConstVal expected;
	sjme_jfieldID field;
	sjme_list(sjme_nvm_class_annotation)* annotations;
	sjme_nvm_class_annotation annotation;
	
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
	runPool = NULL;
	if (sjme_error_is(error = sjme_alloc_poolInitMalloc(&runPool,
		1048576 * 8)) || runPool == NULL)
		goto fail_poolInit;
	
	/* Allocate parameter pool. */
#if 1
	paramPool = runPool;
#else
	paramPool = NULL;
	if (sjme_error_is(error = sjme_alloc_poolInitMalloc(&paramPool,
		1048576 * 4)) || paramPool == NULL)
		goto fail_poolInit;
#endif
	
	/* Open seekable to the boot Jar. */
	bootSeek = NULL;
	if (sjme_error_is(error = nal->fileOpen(paramPool, argv[1],
		&bootSeek, SJME_NAL_OPEN_READ)) || bootSeek == NULL)
		goto fail_openBootJar;
	
	/* Load boot suite. */
	bootSuite = NULL;
	if (sjme_error_is(error = sjme_nvm_rom_suiteFromZipSeekable(
		paramPool, &bootSuite, bootSeek, SJME_NVM_BOOT_CLUTTER_RELEASE)) ||
		bootSuite == NULL)
		goto fail_loadBootJar;
	
	/* Splice up the classpath. */
	n = strlen(argv[3]) + 1;
	classpathSplice = NULL;
	if (sjme_error_is(error = sjme_alloc(paramPool, n + 1,
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
	if (sjme_error_is(error = sjme_list_flattenArgNul(paramPool,
		&classpath, classpathSplice)) ||
		classpath == NULL)
		goto fail_initClasspath;

	/* No longer needed. */
	if (sjme_error_is(error = sjme_alloc_free(classpathSplice)))
		goto fail_freeSplice;
	classpathSplice = NULL;
	
	/* Debug. */
	for (i = 0; i < classpath->length; i++)
		sjme_message("classpath[%d]: %s", i, classpath->elements[i]);
		
	/* Setup main arguments to use, of which there are none. */
	mainArgs = NULL;
	if (sjme_error_is(error = sjme_list_alloc(paramPool, 0, &mainArgs,
		sjme_lpstr, 0)) || mainArgs == NULL)
		goto fail_initMainArgs;

	/* Clear result for later test expectations. */
	memset(&result, 0, sizeof(result));
	result.value.t = SJME_JAVA_TYPE_ID_VOID;
	
	/* Setup boot parameters. */
	memset(&bootParam, 0, sizeof(bootParam));
	bootParam.nal = &sjme_nal_test;
	bootParam.bootSuite = bootSuite;
	bootParam.freeBootSuite = SJME_JNI_TRUE;
	bootParam.mainClass = argv[4];
	bootParam.mainClassPathByName = (const sjme_list(sjme_lpcstr)*)classpath;
	bootParam.freeMainClassPathByName = SJME_JNI_TRUE;
	bootParam.mainArgs = (const sjme_list(sjme_lpcstr)*)mainArgs;
	bootParam.freeMainArgs = SJME_JNI_TRUE;
	bootParam.extraCloseHandle = SJME_AS_CLOSEABLE(bootSeek);

	/* Hooks specifically for NanoTest. */
	bootParam.hooks = &sjme_test_nano_hooks;
	bootParam.hookData = &result;

	/* JDWP Debugging. */
	bootParam.jdwpAddress = "localhost";
	bootParam.jdwpPort = 5005;
	bootParam.jdwpListening = SJME_JNI_FALSE;
	
	/* Boot the virtual machine. */
	inState = NULL;
	mainTask = NULL;
	if (sjme_error_is(error = sjme_nvm_boot(runPool,
		&bootParam, &inState, &mainTask)) ||
		inState == NULL || mainTask == NULL)
		goto fail_boot;

	/* Convert main class to slashes. */
	memset(&mainName, 0, sizeof(mainName));
	snprintf(mainName, BUF_SIZE - 1, "%s", argv[4]);
	for (i = 0; i < BUF_SIZE; i++)
		if (mainName[i] == '.')
			mainName[i] = '/';
	
	/* Locate the main class, to get the expected value. */
	mainClass = NULL;
	if (sjme_error_is(error = sjme_nvm_vmClass_loaderLoadU(
		mainTask->classLoader, &mainClass,
		sjme_atomic_g(sjme_nvm_thread, &mainTask->globals.mainThread),
		mainName, SJME_JNI_TRUE) || mainClass == NULL))
		goto fail_findMain;

	/* Set expected value to something invalid. */
	memset(&expected, 0, sizeof(expected));
	expected.type = SJME_NUM_BASIC_TYPE_IDS;
	
	/* There needs to be defined annotations. */
	annotations = mainClass->info->annotations;
	if (annotations == NULL || annotations->length <= 0)
	{
		error = SJME_ERROR_NANOTEST_NO_ANNOTATIONS;
		goto fail_noAnnotations;
	}
	
	/* Parse the detail annotation. */
	for (i = 0, n = annotations->length; i < n; i++)
	{
		/* Skip blanks. */
		annotation = annotations->elements[i];
		if (annotation == NULL)
			continue;
		
		/* Ignore non-NanoDetails, as those belong to something else. */
		if (!sjme_charSeq_equalsUtfR(annotation->className->seq,
			"Lnano/NanoDetails;"))
			continue;
		
		/* Expected values. */
		if (sjme_charSeq_equalsUtfR(annotation->fieldName->seq,
				"expectedVoid") ||
			sjme_charSeq_equalsUtfR(annotation->fieldName->seq,
				"expectedInteger") ||
			sjme_charSeq_equalsUtfR(annotation->fieldName->seq,
				"expectedLong") ||
			sjme_charSeq_equalsUtfR(annotation->fieldName->seq,
				"expectedString"))
		{
			expected = annotation->value;
		}
		
		/* Unknown. */
		else
		{
			sjme_emitB("Unknown/unhandled annotation: %s %s",
				sjme_charSeq_tempUtf(annotation->fieldName->seq));
		}
	}

	/* No field was found? */
	if (expected.type == SJME_NUM_BASIC_TYPE_IDS)
	{
		error = SJME_ERROR_NANOTEST_EXPECTED_MISSING;
		goto fail_noExpected;
	}

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

	/* If the exit was successful, check test result. */
	if (exitCode == 0)
	{
		/* If not captured, then the test fails. */
		if (!result.captured)
		{
			error = SJME_ERROR_NO_TEST_RESULT;
			goto fail_notCaptured;
		}

		/* Compare directly. */
		if (memcmp(&result.value, &expected.value,
			sizeof(expected)) != 0)
		{
			/* Debug. */
			sjme_emitB("Failed test: got %d:%08x.%08x, expected %d:%08x.%08x",
				result.value.t,
					result.value.v.j.part.hi,
					result.value.v.j.part.lo,
				expected.type,
					expected.value.java.j.part.hi,
					expected.value.java.j.part.lo);
			
			/* Fail. */
			error = SJME_ERROR_NOT_MATCHED;
			goto fail_unexpected;
		}
	}

	/* There must be no memory blocks allocated, destruction should be */
	/* in an entirely clean slate with nothing left over. */
	allocCount = -1;
	if (sjme_error_is(error = sjme_alloc_poolSpaceTotalSize(runPool,
		NULL, NULL, NULL,
		&allocCount)) || allocCount < 0)
		goto fail_countBlocks;

	/* There must be zero blocks. */
	if (allocCount != 0)
	{
#if defined(SJME_CONFIG_DEBUG)
		/* Dump memory state. */
		sjme_alloc_poolDump(runPool, SJME_JNI_TRUE);
#endif
		
		/* Fail. */
		error = SJME_ERROR_MEMORY_EXISTS;
		goto fail_existingBlocks;
	}
	
	/* Return with the exit code. */
	return exitCode;

fail_free:
fail_unexpected:
fail_noConstant:
fail_noExpected:
fail_noAnnotations:
fail_findMain:
fail_countBlocks:
fail_notCaptured:
fail_destroy:
fail_loop:
fail_boot:
fail_initMainArgs:
fail_freeSplice:
fail_initClasspath:
fail_splicePath:
fail_loadBootJar:
fail_openBootJar:
fail_poolInit:
	
#if defined(SJME_CONFIG_DEBUG)
	/* Always dump memory state for any other error. */
	if (runPool != NULL)
		sjme_alloc_poolDump(runPool, SJME_JNI_TRUE);
#endif
	
fail_existingBlocks:
	sjme_message("Failed NanoTest: %d", error);
	return EXIT_FAILURE;
#undef BUF_SIZE
}
