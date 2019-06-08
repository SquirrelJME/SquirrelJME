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
#include <errno.h>

#include "sjmerc.h"

/** File name structure. */
typedef struct sjme_nativefilename
{
	/** Native path. */
	char* filename;
} sjme_nativefilename;

/** Open file. */
typedef struct sjme_nativefile
{
	/** File opened. */
	FILE* file;
} sjme_nativefile;

/** Standard C functions. */
static sjme_nativefuncs stdcfuncs;

/** Returns the native file name. */
sjme_nativefilename* sjme_stdc_nativeromfile(void)
{
	sjme_nativefilename* rv;
	
	/* Allocate. */
	rv = calloc(1, sizeof(*rv));
	if (rv == NULL)
		return NULL;
	
	/* Set with string copy. */
	rv->filename = strdup("squirreljme.sqc");
	
	/* Use this one! */
	return rv;
}

/** Opens the specified file. */
sjme_nativefile* sjme_stdc_fileopen(sjme_nativefilename* filename,
	sjme_jint mode, sjme_jint* error)
{
	sjme_nativefile* rv;
	int err;
	
	if (filename == NULL)
		return NULL;
	
	/* Allocate returning file. */
	rv = calloc(1, sizeof(*rv));
	if (rv == NULL)
		return NULL;
	
	/* Try to open file! */
	rv->file = fopen(filename->filename,
		(mode == SJME_OPENMODE_READWRITETRUNCATE ? "w+b" :
		(mode == SJME_OPENMODE_READWRITE ? "r+b" : "r")));
	if (rv->file == NULL)
	{
		// Set error?
		if (error != NULL)
		{
			err = errno;
			if (err == ENOENT)
				*error = SJME_ERROR_NOSUCHFILE;
			else
				*error = SJME_ERROR_UNKNOWN;
		}
		
		// Clear out
		free(rv);
		rv = NULL;
	}
	
	/* Use it! */
	return rv;
}

/** Closes the specified file. */
void sjme_stdc_fileclose(sjme_nativefile* file, sjme_jint* error)
{
	int rv;
	
	/* Not closing a file? */
	if (file == NULL)
		return;
	
	/* Close and clear pointer. */
	rv = fclose(file->file);
	
	/* Set error state? */
	if (error != NULL)
		*error = (rv == 0 ? SJME_ERROR_NONE : SJME_ERROR_UNKNOWN);
	
	/* Free resources. */
	free(file);
}

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
	stdcfuncs.nativeromfile = sjme_stdc_nativeromfile;
	stdcfuncs.fileopen = sjme_stdc_fileopen;
	stdcfuncs.fileclose = sjme_stdc_fileclose;
	
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
