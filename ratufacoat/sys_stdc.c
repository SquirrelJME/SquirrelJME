/* ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// --------------------------------------------------------------------------*/

/**
 * SquirrelJME Standard C Program (should run on anything).
 *
 * @since 2019/06/02
 */

#if defined(SQUIRRELJME_PORT_STDC)
 
/****************************************************************************/

#include <stddef.h>
#include <stdlib.h>
#include <string.h>
#include <stdio.h>

#include "sjmerc.h"

/** Standard C functions. */
static sjme_nativefuncs stdcfuncs;

/**
 * Main entry point.
 *
 * @param argc Argument count.
 * @param argv Arguments.
 * @since 2019/06/02
 */
int main(int argc, char** argv)
{
	sjme_jvmoptions options;
	sjme_jvm* jvm;
	
	/** Wipe options because it will get set and such. */
	memset(&options, 0, sizeof(options));
	
	/* Setup arguments. */
	options.args.format = SJME_JVMARG_FORMAT_STDC;
	options.args.args.stdc.argc = argc;
	options.args.args.stdc.argv = argv;
	
	/* Setup native functions. */
	
	/* Create VM. */
	jvm = sjme_jvmnew(&options, &stdcfuncs);
	if (jvm == NULL)
	{
		fprintf(stderr, "Failed to create the JVM!\n");
		return EXIT_FAILURE;
	}
	
	/* Execute until termination. */
	while (sjme_jvmexec(jvm) != 0)
		continue;
	
	return EXIT_SUCCESS;
}

/****************************************************************************/

#endif /* defined(SQUIRRELJME_PORT_STDC) */
