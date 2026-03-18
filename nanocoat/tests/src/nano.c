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

typedef struct sjme_test_nano_result
{
	/** Was this captured? */
	sjme_jboolean captured;

	/** The value captured. */
	sjme_jvalueTyped value;
	
	/** The string captured. */
	sjme_charSeq string;
	
	/** The exception captured. */
	sjme_charSeq exception;
} sjme_test_nano_result;

SJME_NVM_MLE_FUNCTION_DECL(makeArrayNull)
{
	/* Just return null. */
	argR->t = SJME_JAVA_TYPE_ID_OBJECT;
	argR->v.l = NULL;
	return SJME_ERROR_NONE;
}

SJME_NVM_MLE_FUNCTION_DECL(makeArrayString)
{
#define BUF_SIZE 32
	sjme_errorCode error, otherError;
	sjme_jint i, len;
	sjme_jarray rv;
	sjme_cchar buf[BUF_SIZE];
	sjme_jstring element;
	
	/* Array cannot be negative. */
	len = argV[0].v.i;
	if (len < 0)
		return sjme_die("Negative array size.");

	/* Directly allocate array. */
	rv = NULL;
	if (sjme_error_is(error = sjme_nvm_instance_objectArrayNew(
		SJME_F_T(inFrame), &rv,
		sjme_nvm_task_commonClassR(SJME_F_T(inFrame),
			SJME_NVM_COMMON_STRING), len)) || rv == NULL)
		goto fail_allocArray;
	
	/* Setup string values in the array. */
	for (i = 0; i < len; i++)
	{
		/* Build a sample string. */
		memset(buf, 0, sizeof(buf));
		snprintf(buf, BUF_SIZE - 1,
			"string%d", i);
		buf[BUF_SIZE - 1] = '\0';
		
		/* Create string, do not make intern strings. */
		element = NULL;
		if (sjme_error_is(error = sjme_nvm_task_threadStringValueOfUtf(
			SJME_F_T(inFrame), (sjme_jstring*)&element,
			SJME_JNI_FALSE, buf)) || element == NULL)
			goto fail_stringValue;
		
		/* These do need to be counted up. */
		if (sjme_error_is(error = sjme_nvm_vmField_cisSetS(
			&rv->e, i, NULL, SJME_VLS_JOBJECT(element))))
			goto fail_arraySet;
	}
	
	/* Return the array. */
	argR->t = SJME_JAVA_TYPE_ID_OBJECT;
	argR->v.l = (sjme_jobject)rv;
	return SJME_ERROR_NONE;
	
fail_stringValue:
fail_arraySet:
fail_allocArray:
	/* Deallocate. */
	if (rv != NULL)
	{
		if (sjme_error_is(otherError = sjme_nvm_instance_countDown(
			(sjme_jobject)rv)))
			return sjme_error_vmError(inFrame, otherError);
	}
	
	return sjme_error_vmError(inFrame, error);
#undef BUF_SIZE
}

SJME_NVM_MLE_FUNCTION_DECL_ALT(result, integer)
{
	sjme_test_nano_result* result;
	
	/* Recover result. */
	result = SJME_F_S(inFrame)->hookData;
	if (result == NULL)
		return sjme_die("No hookData.");
	
	/* Result can only be called once! */
	if (result->captured)
		return sjme_die("Result already captured.");
	
	/* Set test string result. */
	result->captured = SJME_JNI_TRUE;
	result->value.t = SJME_JAVA_TYPE_ID_INTEGER;
	result->value.v.i = argV[0].v.i;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

SJME_NVM_MLE_FUNCTION_DECL_ALT(result, string)
{
	sjme_errorCode error;
	sjme_test_nano_result* result;
	sjme_jstring string;
	
	/* String cannot be null. */
	string = (sjme_jstring)argV[0].v.l;
	if (string < 0)
		return sjme_die("Null string.");
	
	/* Recover result. */
	result = SJME_F_S(inFrame)->hookData;
	if (result == NULL)
		return sjme_die("No hookData.");
	
	/* Result can only be called once! */
	if (result->captured)
		return sjme_die("Result already captured.");
	
	/* Set test string result. */
	result->captured = SJME_JNI_TRUE;
	result->value.t = SJME_JAVA_TYPE_ID_OBJECT;
	
	/* Duplicate string so the object can go through normal GC. */
	if (sjme_error_is(error = sjme_charSeq_dup(SJME_F_S(inFrame)->allocPool,
		&result->string,
		sjme_atomic_g(sjme_charSeq, &string->seq))) ||
		result->string == NULL)
		return sjme_die("Failed to dup string: %d", error);
	
	/* Success! */
	return SJME_ERROR_NONE;
}

SJME_NVM_MLE_FUNCTION_DECL_ALT(result, void)
{
	sjme_test_nano_result* result;
	
	/* Recover result. */
	result = SJME_F_S(inFrame)->hookData;
	if (result == NULL)
		return sjme_die("No hookData.");
	
	/* Result can only be called once! */
	if (result->captured)
		return sjme_die("Result already captured.");
	
	/* Set test string result. */
	result->captured = SJME_JNI_TRUE;
	result->value.t = SJME_JAVA_TYPE_ID_VOID;
	
	/* Success! */
	return SJME_ERROR_NONE;
}

SJME_NVM_MLE_SHELF_DECLARE(NanoShelf) =
{
	SJME_NVM_MLE_DEFINE(makeArrayNull,
		SJME_MD(SJME_MD_A(SJME_MD_STRING), ),
		"L", ""),
	SJME_NVM_MLE_DEFINE(makeArrayString,
		SJME_MD(SJME_MD_A(SJME_MD_STRING), SJME_MD_I),
		"L", "I"),
	
	SJME_NVM_MLE_DEFINE_ALT(result, integer,
		SJME_MD(SJME_MD_V, SJME_MD_I),
		"V", "I"),
	SJME_NVM_MLE_DEFINE_ALT(result, string,
		SJME_MD(SJME_MD_V, SJME_MD_STRING),
		"V", "L"),
	SJME_NVM_MLE_DEFINE_ALT(result, void,
		SJME_MD(SJME_MD_V, ),
		"V", ""),
	
	SJME_NVM_MLE_STOP()
};

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
}

static sjme_errorCode sjme_test_nano_uncaught(
	sjme_attrInNotNull sjme_nvm_thread inThread,
	sjme_attrInNotNull sjme_jthrowable uncaught)
{
	sjme_errorCode error;
	sjme_test_nano_result* result;
	
	if (inThread == NULL || uncaught == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Recover result. */
	result = SJME_F_S(inThread)->hookData;
	if (result == NULL)
		return SJME_ERROR_ILLEGAL_STATE;
	
	/* An exception was captured. */
	result->captured = SJME_JNI_TRUE;
	result->value.t = SJME_NUM_JAVA_TYPE_IDS;
	
	/* Duplicate string so the object can go through normal GC. */
	if (sjme_error_is(error = sjme_charSeq_dup(SJME_T_S(inThread)->allocPool,
		&result->exception, sjme_atomic_g(sjme_jclass, 
			&uncaught->object.isClass)->fieldName)) ||
		result->exception == NULL)
		return sjme_die("Failed to dup string: %d", error);
	
	/* Success! */
	return SJME_ERROR_NONE;
}

static const sjme_nvm_stateHooks sjme_test_nano_hooks =
{
	sjme_sm(.gc, NULL),
	sjme_sm(.nativeCall, sjme_test_nano_nativeCall),
	sjme_sm(.uncaught, sjme_test_nano_uncaught),
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
	sjme_list(sjme_nvm_class_annotation)* annotations;
	sjme_nvm_class_annotation annotation;
	sjme_jvalueTyped expectedJava;
	sjme_charSeq expectedString, expectedException;
	
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
	memset(&expectedJava, 0, sizeof(expectedJava));
	expectedJava.t = SJME_NUM_BASIC_TYPE_IDS;
	expectedString = NULL;
	
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
		
		/* Ignore blank field, this indicates an annotation is starting. */
		if (annotation->fieldName == NULL)
			continue;
		
		/* Java type value. */
		if (sjme_charSeq_equalsUtfR(annotation->fieldName->seq,
				"expectedInteger") ||
			sjme_charSeq_equalsUtfR(annotation->fieldName->seq,
				"expectedLong"))
			expectedJava = annotation->valueJava;
		
		/* Exception. */
		else if (sjme_charSeq_equalsUtfR(annotation->fieldName->seq,
			"expectedException"))
		{
			/* Duplicate expected string so it does not interfere with GC. */
			if (sjme_error_is(error = sjme_charSeq_dup(paramPool,
				&expectedException, annotation->valueString->seq)))
				goto fail_dupExpected;
		}
		
		/* String. */
		else if (sjme_charSeq_equalsUtfR(annotation->fieldName->seq,
				"expectedString"))
		{
			/* Duplicate expected string so it does not interfere with GC. */
			if (sjme_error_is(error = sjme_charSeq_dup(paramPool,
				&expectedString, annotation->valueString->seq)))
				goto fail_dupExpected;
			
			expectedJava.t = SJME_JAVA_TYPE_ID_OBJECT;
		}
		
		/* Void, as nothing can directly use void. */
		else if (sjme_charSeq_equalsUtfR(annotation->fieldName->seq,
			"expectedVoid"))
			expectedJava.t = SJME_BASIC_TYPE_ID_VOID;
		
		/* Unknown. */
		else
		{
			sjme_emitB("Unknown/unhandled annotation: %s %s",
				sjme_charSeq_tempUtf(annotation->fieldName->seq));
		}
	}
	
	/* No expected value was found? */
	if (expectedString == NULL && expectedJava.t == SJME_NUM_BASIC_TYPE_IDS &&
		expectedException == NULL)
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
			
			/* Uncaught exception, which is what we might want, however */
			/* the hook and cleanup were already called. */
			else if (error == SJME_ERROR_UNCAUGHT_EXCEPTION)
				break;
			
			/* Everything else. */
			else
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
		
		/* Exception type. */
		if ((expectedException != NULL || result.exception != NULL) &&
			!sjme_charSeq_equalsR(expectedException,
				result.exception))
		{
			/* Debug. */
			sjme_emitB("Failed test: got exception %s, exception %s",
				sjme_charSeq_tempUtf(result.exception),
				sjme_charSeq_tempUtf(expectedException));
			
			/* Fail. */
			error = SJME_ERROR_NOT_MATCHED;
			goto fail_unexpected;
		}
		
		/* Wrong type? */
		else if (result.value.t != expectedJava.t &&
			expectedException == NULL)
		{
			/* Debug. */
			sjme_emitB("Failed test: got type %d, expected %d",
				result.value.t,
				expectedJava.t);
			
			/* Fail. */
			error = SJME_ERROR_NOT_MATCHED;
			goto fail_unexpected;
		}
		
		/* String type. */
		else if (expectedString != NULL && (result.string == NULL ||
			!sjme_charSeq_equalsR(expectedString,
				result.string)))
		{
			/* Debug. */
			sjme_emitB("Failed test: got %d:%s, expected %d:%s",
				result.value.t,
					sjme_charSeq_tempUtf(result.string),
				expectedJava.t,
					sjme_charSeq_tempUtf(expectedString));
			
			/* Fail. */
			error = SJME_ERROR_NOT_MATCHED;
			goto fail_unexpected;
		}
		
		/* Java Type. */
		else if (expectedString == NULL && expectedException == NULL &&
			memcmp(&result.value, &expectedJava,
				sizeof(expectedJava)) != 0)
		{
			/* Debug. */
			sjme_emitB("Failed test: got %d:%08x.%08x, expected %d:%08x.%08x",
				result.value.t,
					result.value.v.j.part.hi,
					result.value.v.j.part.lo,
				expectedJava.t,
					expectedJava.v.j.part.hi,
					expectedJava.v.j.part.lo);
			
			/* Fail. */
			error = SJME_ERROR_NOT_MATCHED;
			goto fail_unexpected;
		}
	}
	
	/* Free result string if it was created. */
	if (result.string != NULL)
	{
		if (sjme_error_is(error = sjme_charSeq_delete(result.string)))
			goto fail_deleteResultString;
		result.string = NULL;
	}
	
	/* Free result exception if it was created. */
	if (result.exception != NULL)
	{
		if (sjme_error_is(error = sjme_charSeq_delete(result.exception)))
			goto fail_deleteResultString;
		result.exception = NULL;
	}
	
	/* And the expected string as well. */
	if (expectedString != NULL)
	{
		if (sjme_error_is(error = sjme_charSeq_delete(expectedString)))
			goto fail_deleteExpectedString;
		expectedString = NULL;
	}
	
	/* And the expected exception as well. */
	if (expectedException != NULL)
	{
		if (sjme_error_is(error = sjme_charSeq_delete(expectedException)))
			goto fail_deleteExpectedString;
		expectedException = NULL;
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
		
#if !defined(SJME_CONFIG_DEBUG_NO_REAL_GC)
		/* Fail. */
		error = SJME_ERROR_MEMORY_EXISTS;
		goto fail_existingBlocks;
#endif
	}
	
	/* Return with the exit code. */
	return exitCode;

fail_free:
fail_unexpected:
fail_noConstant:
fail_noExpected:
fail_dupExpected:
fail_noAnnotations:
fail_findMain:
fail_countBlocks:
fail_deleteExpectedString:
fail_deleteResultString:
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
	sjme_message("Failed NanoTest (via Error): %d", error);
	return EXIT_FAILURE;
	
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
