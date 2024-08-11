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

static sjme_debug_handlerFunctions sjme_test_debugHandlers =
{
	.abort = sjme_test_abortHandler,
	.exit = sjme_test_exitHandler,
	.message = NULL,
};

#if defined(SJME_CONFIG_DEBUG)
void sjme_test_leakCheck(sjme_alloc_pool* pool)
{
	sjme_alloc_link* link;
	
	/* Go through every link. */
	for (link = pool->frontLink; link != NULL; link = link->next)
		if (link->space == SJME_ALLOC_POOL_SPACE_USED)
		{
			/* Indicate it leaked */
			sjme_messageR(NULL, -1, NULL, SJME_JNI_TRUE,
				"LEAK:\t%dB\t%s\t%s:%d\tl=%p\tp=%p",
				link->allocSize, link->debugFunction,
				sjme_debug_shortenFile(link->debugFile), link->debugLine,
				link, pool);
				
			/* Recursively check the nested pool. */
			if ((link->flags & SJME_ALLOC_LINK_FLAG_NESTED_POOL) != 0)
				sjme_test_leakCheck((sjme_alloc_pool*)&link->block[0]);
		}
}
#endif

int sjme_test_main(int argc, sjme_lpstr* argv, sjme_lpcstr* nextTest)
{
	const sjme_availableTest* found;
	const sjme_availableTest* nextFound;
	sjme_testResult result;
	sjme_test test;
	sjme_jint i, jumpId;
	sjme_pointer chunk;
	sjme_jint chunkLen;
	
	if (argc <= 1)
	{
		sjme_message("Need a test name.");
		return EXIT_FAILURE;
	}
	
	/* Search for the given test, blank just uses first test. */
	found = NULL;
	nextFound = NULL;
	if (0 == strcmp("", argv[1]))
	{
		/* Default to first. */
		if (sjme_availableTests[0].name != NULL)
		{
			found = &sjme_availableTests[0];

			/* If next exists, use this for sequential running. */
			if (sjme_availableTests[1].name != NULL)
				nextFound = &sjme_availableTests[1];
		}
	}
	else
	{
		for (i = 0; sjme_availableTests[i].name != NULL; i++)
			if (strcmp(argv[1], sjme_availableTests[i].name) == 0)
			{
				/* Use this. */
				found = &sjme_availableTests[i];

				/* Get the next test for sequential running. */
				if (sjme_availableTests[i + 1].name != NULL)
					nextFound = &sjme_availableTests[i + 1];

				break;
			}
	}

	/* Set next found test accordingly. */
	if (nextTest != NULL)
		if (nextFound != NULL)
			*nextTest = nextFound->name;
		else
			*nextTest = NULL;
	
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

	/* Use a different debug handlers? */
	if (sjme_debug_handlers == NULL)
		sjme_debug_handlers = &sjme_test_debugHandlers;

	/* Setup base allocation pool. */
	for (chunkLen = 65536; chunkLen >= 1024; chunkLen /= 2)
	{
		/* Try to alloca everything at once. */
		chunk = sjme_alloca(chunkLen);
		if (chunk == NULL)
			continue;

		/* Initialize pool. */
		if (sjme_error_is(sjme_alloc_poolInitStatic(&test.pool,
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

#if defined(SJME_CONFIG_DEBUG)
	/* Check for never freed memory. */
	sjme_test_leakCheck(test.pool);
#endif
	
	/* Cleanup after test. */
	sjme_test_currentTest = NULL;
	
	/* Handle result. */
	if (result == SJME_TEST_RESULT_SKIP)
		return 2;
	else if (result == SJME_TEST_RESULT_FAIL)
		return EXIT_FAILURE;
	return EXIT_SUCCESS;
}
