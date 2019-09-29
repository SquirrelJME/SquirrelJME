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

#define _DEBUG

/**
 * Main entry point.
 *
 * @param argc Argument count.
 * @param argv Argument strings.
 * @since 2019/09/02
 */
int main(int argc, char** argv)
{
	int i;
	
	/* Show welcome message. */
	fprintf(stderr, "SquirrelJME Simulated JVM 0.1\n");
	fprintf(stderr, "Copyright (C) 2019 Stephanie Gawroriski\n");
	fprintf(stderr, "https://squirreljme.cc/\n");
	fprintf(stderr, "\n");
	
	fprintf(stderr, "This virtual machine is being used to bootstrap the ");
	fprintf(stderr, "build of SquirrelJME because a Java run-time has not ");
	fprintf(stderr, "detected on the host machine or one which was not ");
	fprintf(stderr, "elected to be used. The purpose of this Simulated JVM ");
	fprintf(stderr, "is to allow for building SquirrelJME on any system.\n");
	fprintf(stderr, "\n");
	
	/* Print arguments. */
#if defined(_DEBUG)
	for (i = 0; i < argc; i++)
		fprintf(stderr, "Arg %d: %s\n", i, argv[i]);
#endif
	
	fprintf(stderr, "TODO!\n");
	return EXIT_FAILURE;
}
