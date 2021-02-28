/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// --------------------------------------------------------------------------*/

#include <stdlib.h>
#include <stdio.h>
#include <string.h>

#include "tests.h"

/**
 * Single test information.
 * 
 * @since 2020/08/12
 */
typedef struct sjme_singleTest
{
	const char* name;
	int (*function)(void);
} sjme_singleTest;

/** Macro for making a test. */
#define SJME_TEST(name) {#name, name}

/** Test table. */
static const sjme_singleTest sjme_singleTests[] =
{
	SJME_TEST(testMemHandleCycle),
	SJME_TEST(testMemHandleInit),
	SJME_TEST(testMemHandleInvalid),
	SJME_TEST(testNothing),
	SJME_TEST(testOpCodes),
	
	/* End of tests. */
	{NULL, NULL}
};

/**
 * Main entry point.
 * 
 * @param argc Argument count.
 * @param argv Arguments.
 * @return @a EXIT_SUCCESS on success, otherwise @a EXIT_FAILURE.
 * @since 2020/08/12
 */
int main(int argc, char** argv)
{
	int allTests, execTest, currentExitCode, ranTests;
	const sjme_singleTest* test;
	
	/* Running all tests? */
	if (argc == 1 || argv[0] == NULL)
		allTests = 1;
	else
		allTests = 0;
	
	/* Assume all pass initially. */
	currentExitCode = EXIT_SUCCESS;
	
	/* Go through the test table and find the named tests if applicable. */
	ranTests = 0;
	for (test = &sjme_singleTests[0]; test->name != NULL; test++)
	{
		/* Determine if this is a test to run */
		execTest = allTests;
		if (!allTests)
			for (int i = 1; i < argc; i++)
				if (strcmp(argv[i], test->name) == 0)
				{
					execTest = 1;
					break;
				}
		
		/* Execute the test. */
		if (execTest)
		{
			/* Indicate a message that it is running. */
			fprintf(stderr, "Running test %s...\n", test->name);
			
			/* Run it, if it fails, then signal. */
			ranTests++;
			if (test->function() != EXIT_SUCCESS)
				currentExitCode = EXIT_FAILURE;
		}
	}
	
	/* Specified a specific test but was not ran, so fail. */
	if (!allTests && ranTests <= 0)
		return EXIT_FAILURE;
	
	/* And the code if all tests ran. */
	return currentExitCode;
}
