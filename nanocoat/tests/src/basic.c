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

#include "sjme/nvm.h"
#include "sjme/debug.h"
#include "test.h"
#include "proto.h"

const sjme_availableTest sjme_availableTests[] =
{
	#include "struct.h"
	{NULL, NULL}
};

/**
 * Main entry point
 * 
 * @param argc Argument count.
 * @param argv Arguments.
 * @return The exit code of the test.
 * @since 2023/08/09
 */
int main(int argc, char** argv)
{
	const sjme_availableTest* found;
	sjme_testResult result;
	sjme_test test;
	sjme_jint i, jumpId;
	
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

	/* Set jump point for going back. */
	jumpId = setjmp(test.jumpPoint);
	if (jumpId == 0)
	{
		/* Run the test. */
		sjme_message("Starting test %s...", found->name);
		result = found->function(&test);
		sjme_message("Test resulted in %d.", (sjme_jint)result);
	}
	
	/* Short circuited to fail the test? */
	else
	{
		result = jumpId;
		sjme_message("Short circuited test: %d.", (sjme_jint)result);
	}
	
	/* Cleanup after test. */
	sjme_message("TODO: Test cleanup?");
	
	/* Handle result. */
	if (result == SJME_TEST_RESULT_SKIP)
		return 2;
	else if (result == SJME_TEST_RESULT_FAIL)
		return EXIT_FAILURE;
	return EXIT_SUCCESS;
}
