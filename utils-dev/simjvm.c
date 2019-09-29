/* ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// --------------------------------------------------------------------------*/

/**
 * Simulated Java Virtual Machine.
 *
 * @since 2019/09/02
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define _DEBUG

/**
 * Arguments which modify the starting state of the JVM.
 *
 * @since 2019/09/29
 */
typedef struct simjvm_arguments
{
} simjvm_arguments;

/**
 * Processes the command line arguments used to start the JVM.
 *
 * @param out Output arguments.
 * @param argc Argument count.
 * @param argv Argument strings.
 * @since 2019/09/29
 */
int simjvm_initialize_arguments(simjvm_arguments* out, int argc, char** argv)
{
	int i, filemode;
	char* arg;
	char* next;
	
	/* The pointer must be valid! */
	if (out == NULL || argv == NULL)
		return 1;
	
	/* Starts not in file mode. */
	filemode = 0;
	
	/* Go through all arguments, first skipped because executable. */
	for (i = 1; i < argc; i++)
	{
		/* Get this argument and the next. */
		arg = argv[i];
		next = (i + 1 < argc ? argv[i + 1] : NULL);
		
		/* Print it for debug. */
		fprintf(stderr, "DEBUG: %s\n", arg);
		
		fprintf(stderr, "TODO!\n");
		exit(EXIT_FAILURE);
	}
	
	return 1;
}

/**
 * Main entry point.
 *
 * @param argc Argument count.
 * @param argv Argument strings.
 * @since 2019/09/02
 */
int main(int argc, char** argv)
{
	simjvm_arguments jvmargs;
	
	/* Show welcome message. */
	fprintf(stderr, "SquirrelJME Simulated JVM 0.1\n");
	fprintf(stderr, "Copyright (C) 2019 Stephanie Gawroriski\n");
	fprintf(stderr, "https://squirreljme.cc/\n");
	fprintf(stderr, "\n");
	
	/* Show a warning message why this JVM is being used instead. */
	fprintf(stderr, "This virtual machine is being used to bootstrap the ");
	fprintf(stderr, "build of SquirrelJME because a Java run-time has not ");
	fprintf(stderr, "detected on the host machine or one which was not ");
	fprintf(stderr, "elected to be used. The purpose of this Simulated JVM ");
	fprintf(stderr, "is to allow for building SquirrelJME on any system.\n");
	fprintf(stderr, "\n");
	
	/* Process arguments. */
	memset(&jvmargs, 0, sizeof(jvmargs));
	if (simjvm_initialize_arguments(&jvmargs, argc, argv) != 0)
	{
		fprintf(stderr, "Could not parse arguments!\n");
		return EXIT_FAILURE;
	}
	
	fprintf(stderr, "TODO!\n");
	return EXIT_FAILURE;
}
