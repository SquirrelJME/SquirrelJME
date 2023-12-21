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

#include "proto.h"
#include "sjme/alloc.h"
#include "sjme/debug.h"
#include "sjme/nvm.h"
#include "test.h"

const sjme_availableTest sjme_availableTests[] =
{
	#include "struct.h"
	{NULL, NULL}
};

/** The current test being executed. */
static sjme_test* sjme_test_currentTest;

static sjme_jboolean sjme_test_abortHandler(void)
{
	return SJME_JNI_FALSE;
}

static sjme_jboolean sjme_test_exitHandler(int exitCode)
{
	/* Exit out of the test? */
	if (sjme_test_currentTest != NULL)
	{
		/* Note it. */
		sjme_message("Test exiting with %d, short circuiting out!",
			exitCode);

		/* Jump back to the debug entry point. */
		longjmp(sjme_test_currentTest->jumpPoint,
			SJME_TEST_RESULT_FAIL);

		/* Was handled. */
		return SJME_JNI_TRUE;
	}

	return SJME_JNI_FALSE;
}

/**
 * Main entry point
 * 
 * @param argc Argument count.
 * @param argv Arguments.
 * @return The exit code of the test.
 * @since 2023/08/09
 */
int sjme_test_main(int argc, sjme_lpstr* argv)
{
	const sjme_availableTest* found;
	sjme_testResult result;
	sjme_test test;
	sjme_jint i, jumpId;
	void* chunk;
	sjme_jint chunkLen;
	
	if (argc <= 1)
	{
		sjme_message("Need a test name.");
		return EXIT_FAILURE;
	}
	
	/* Search for the given test. */
	found = NULL;
	for (i = 0; sjme_availableTests[i].name != NULL; i++)
		if (strcmp(argv[1], sjme_availableTests[i].name) == 0)
		{
			found = &sjme_availableTests[i];
			break;
		}
	
	/* Found nothing? */
	if (found == NULL)
	{
		sjme_message("No test called %s exists.", argv[1]);
		return EXIT_FAILURE;
	}
	
	/* Note where it is. */
	if (argc >= 3)
		sjme_message("Test is at %s:1.", argv[2]);
	
	/* Setup test. */
	memset(&test, 0, sizeof(test));

	/* Store test base. */
	sjme_test_currentTest = &test;

	/* Use a different exit handler? */
	if (sjme_debug_exitHandler == NULL)
		sjme_debug_exitHandler = sjme_test_exitHandler;

	/* Setup base allocation pool. */
	for (chunkLen = 65536; chunkLen >= 1024; chunkLen /= 2)
	{
		/* Try to alloca everything at once. */
		chunk = alloca(chunkLen);
		if (chunk == NULL)
			continue;

		/* Initialize pool. */
		if (SJME_IS_ERROR(sjme_alloc_poolInitStatic(&test.pool,
			chunk, chunkLen)) || test.pool == NULL)
		{
			/* No longer testing. */
			sjme_test_currentTest = NULL;

			sjme_message("Could not initialize pre-allocated pool.");
			return EXIT_FAILURE;
		}

		/* Done! */
		break;
	}

	/* Completely failed to allocate. */
	if (chunk == NULL)
	{
		/* No longer testing. */
		sjme_test_currentTest = NULL;

		sjme_message("Could not allocate test pool.");
		return EXIT_FAILURE;
	}

	/* Set jump point for going back. */
	jumpId = setjmp(test.jumpPoint);
	if (jumpId == 0)
	{
		/* Run the test. */
		sjme_message("Starting test %s...", found->name);
		result = found->function(&test);
		sjme_message("Test resulted in %d.", (sjme_jint)result);
	}
	
	/* Short-circuited to fail the test? */
	else
	{
		result = jumpId;
		sjme_message("Short circuited test: %d.", (sjme_jint)result);
	}
	
	/* Cleanup after test. */
	sjme_test_currentTest = NULL;
	
	/* Handle result. */
	if (result == SJME_TEST_RESULT_SKIP)
		return 2;
	else if (result == SJME_TEST_RESULT_FAIL)
		return EXIT_FAILURE;
	return EXIT_SUCCESS;
}
