/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
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

#include <stddef.h>
#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include <errno.h>

#include "sjmerc.h"
#include "error.h"
#include "debug.h"

/** Screen size. */
#define SJME_STDC_WIDTH 240
#define SJME_STDC_HEIGHT 320
#define SJME_STDC_VIDEORAMSIZE 76800

/** RatufaCoat's Video RAM memory. */
static sjme_jint sjme_ratufacoat_videoram[SJME_STDC_VIDEORAMSIZE];

/**
 * Main entry point.
 *
 * @param argc Argument count.
 * @param argv Arguments.
 * @since 2019/06/02
 */
int main(int argc, char** argv)
{
	sjme_todo("Implement this?");
	return EXIT_FAILURE;
#if 0
	sjme_jvmoptions options;
	sjme_jvm* jvm;
	sjme_error error;
	
	/* Clear error. */
	memset(&error, 0, sizeof(error));
	
	/* Wipe options because it will get set and such. */
	memset(&options, 0, sizeof(options));
	
	/* Setup arguments. */
	options.args.format = SJME_JVMARG_FORMAT_STDC;
	options.args.args.stdc.argc = argc;
	options.args.args.stdc.argv = argv;
	
	/* Setup native functions. */
	stdcfuncs.nativeromfile = sjme_stdc_nativeromfile;
	stdcfuncs.fileopen = sjme_stdc_fileopen;
	stdcfuncs.fileclose = sjme_stdc_fileclose;
	stdcfuncs.filesize = sjme_stdc_filesize;
	stdcfuncs.fileread = sjme_stdc_fileread;
	stdcfuncs.stdout_write = sjme_stdc_stdout_write;
	stdcfuncs.stderr_write = sjme_stdc_stderr_write;
	stdcfuncs.framebuffer = sjme_stdc_framebuffer;
	
	/* Create VM. */
	jvm = NULL;
	if (sjme_jvmNew(&jvm, &options, &stdcfuncs, &error))
	{
		fprintf(stderr,
			"Failed to create the JVM! (Error %d/0x%X %d/0x%X)\n",
			(int)error.code, (unsigned int)error.code,
			(int)error.value, (unsigned int)error.value);
		return EXIT_FAILURE;
	}
	
	/* Execute until termination. */
	for (;;)
	{
		/* Just execute the VM and disregard nay cycles that remain. */
		sjme_jvmexec(jvm, &error, SJME_JINT_C(1048576));
		
		/* The JVM hit some kind of error? */
		if (error.code != SJME_ERROR_NONE)
		{
			/* Normal JVM exit, not considered a true error. */
			if (error.code == SJME_ERROR_JVMEXIT_SUV_OKAY)
				break;
			
			/* Message on it! */
			fprintf(stderr, "JVM execution fault! (Error %d/0x%X %d/0x%X)\n",
				(int)error.code, (unsigned int)error.code,
				(int)error.value, (unsigned int)error.value);
			
			/* Destroy the JVM to free resources. */
			if (sjme_jvmDestroy(jvm, &error))
				fprintf(stderr,
					"JVM destruction error! (Error %d/0x%X %d/0x%X)\n",
					(int)error.code, (unsigned int)error.code,
					(int)error.value, (unsigned int)error.value);
			
			return EXIT_FAILURE;
		}
		
		/* Keep going! */
		continue;
	}
	
	/* Destroy the VM so it uses no memory. */
	if (sjme_jvmDestroy(jvm, &error))
		fprintf(stderr, "JVM destruction error! (Error %d/0x%X %d/0x%X)\n",
			(int)error.code, (unsigned int)error.code,
			(int)error.value, (unsigned int)error.value);
	
	return EXIT_SUCCESS;
#endif
}
