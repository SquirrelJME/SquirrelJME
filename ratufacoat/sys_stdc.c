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
	{
		if (error != NULL)
			*error = SJME_ERROR_INVALIDARG;
		
		return NULL;
	}
	
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
	{
		if (error != NULL)
			*error = SJME_ERROR_INVALIDARG;
		
		return;
	}
	
	/* Close and clear pointer. */
	rv = fclose(file->file);
	file->file = NULL;
	
	/* Set error state? */
	if (error != NULL)
		*error = (rv == 0 ? SJME_ERROR_NONE : SJME_ERROR_UNKNOWN);
	
	/* Free resources. */
	free(file);
}

/** Returns the size of the file. */
sjme_jint sjme_stdc_filesize(sjme_nativefile* file, sjme_jint* error)
{
	long now, size;
	
	/* Not valid? */
	if (file == NULL)
	{
		if (error != NULL)
			*error = SJME_ERROR_INVALIDARG;
		
		return SJME_JINT_C(-1);
	}
	
	/* Remember current file size, to go back. */
	now = ftell(file->file);
	
	/* Seek to end. */
	if (fseek(file->file, 0, SEEK_END) != 0)
	{
		if (error != NULL)
			*error = SJME_ERROR_UNKNOWN;
		
		return SJME_JINT_C(-1);
	}
	
	/* Set size. */
	size = ftell(file->file);
	if (size < 0)
	{
		if (error != NULL)
			*error = SJME_ERROR_UNKNOWN;
		
		return SJME_JINT_C(-1);
	}
	
	/* Seek back. */
	if (fseek(file->file, now, SEEK_SET) != 0)
	{
		if (error != NULL)
			*error = SJME_ERROR_UNKNOWN;
		
		return SJME_JINT_C(-1);
	}
	
	/* Return the size. */
	if (size >= SJME_JINT_MAX_VALUE)
		return SJME_JINT_MAX_VALUE;
	return (sjme_jint)size;
}

/** Reads from a file. */
sjme_jint sjme_stdc_fileread(sjme_nativefile* file, void* dest, sjme_jint len,
	sjme_jint* error)
{
	size_t rv;
	
	/* Invalid argument? */
	if (file == NULL || dest == NULL || len < 0)
	{
		if (error != NULL)
			*error = SJME_ERROR_INVALIDARG;
		
		return SJME_JINT_C(-1);
	}
	
	/* Read from file. */
	rv = fread(dest, 1, len, file->file);
	if (rv == 0)
	{
		/* End of file? */
		if (feof(file->file) != 0)
		{
			if (error != NULL)
				*error = SJME_ERROR_ENDOFFILE;
		}
		
		/* Another error. */
		else
		{
			if (error != NULL)
				*error = SJME_ERROR_UNKNOWN;
		}
		
		return SJME_JINT_C(-1);
	}
	
	/* Return the read bytes. */
	return (sjme_jint)rv;
}

/** Writes single byte to standard output. */
sjme_jint sjme_stdc_stdout_write(sjme_jint b)
{
	sjme_jbyte v;
	size_t check;
	
	/* Write. */
	v = (sjme_jbyte)b;
	check = fwrite(&v, 1, 1, stdout);
	
	/* EOF? */
	if (feof(stdout))
		return SJME_JINT_C(0);
	
	/* Error? */
	if (ferror(stdout))
		return SJME_JINT_C(-1);
	
	return SJME_JINT_C(1);
}

/** Writes single byte to standard error. */
sjme_jint sjme_stdc_stderr_write(sjme_jint b)
{
	sjme_jbyte v;
	size_t check;
	
	/* Write. */
	v = (sjme_jbyte)b;
	check = fwrite(&v, 1, 1, stderr);
	
	/* EOF? */
	if (feof(stderr))
		return SJME_JINT_C(0);
	
	/* Error? */
	if (ferror(stderr))
		return SJME_JINT_C(-1);
	
	return SJME_JINT_C(1);
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
	sjme_jint error;
	
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
	stdcfuncs.filesize = sjme_stdc_filesize;
	stdcfuncs.fileread = sjme_stdc_fileread;
	stdcfuncs.stdout_write = sjme_stdc_stdout_write;
	stdcfuncs.stderr_write = sjme_stdc_stderr_write;
	
	/* Create VM. */
	error = 0;
	jvm = sjme_jvmnew(&options, &stdcfuncs, &error);
	if (jvm == NULL)
	{
		fprintf(stderr, "Failed to create the JVM! (Error %d/0x%X)\n",
			(int)error, (unsigned int)error);
		return EXIT_FAILURE;
	}
	
	/* Execute until termination. */
	error = 0;
	while (sjme_jvmexec(jvm, &error, SJME_JINT_MAX_VALUE) != 0)
	{
		/* The JVM hit some kind of error? */
		if (error != SJME_ERROR_NONE)
		{
			fprintf(stderr, "JVM execution fault! (Error %d/0x%X)\n",
				(int)error, (unsigned int)error);
			return EXIT_FAILURE;
		}
		
		/* Keep going! */
		continue;
	}
	
	return EXIT_SUCCESS;
}

/****************************************************************************/

#endif /* defined(SQUIRRELJME_PORT_STDC) */
