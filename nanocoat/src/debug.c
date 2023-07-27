/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <stdio.h>
#include <stdlib.h>
#include <stdarg.h>
#include <string.h>

#include "sjme/nvm.h"
#include "sjme/debug.h"

/** Debug buffer size for messages. */
#define DEBUG_BUF 512

void sjme_messageR(const char* file, int line,
	const char* func, const char* message, ...)
{
	char buf[DEBUG_BUF];
	va_list args;
	
	/* Load message buffer. */
	va_start(args, message);
	memset(buf, 0, sizeof(buf));
	vsnprintf(buf, DEBUG_BUF, message, args);
	va_end(args);
	
	/* Print output message. */
	if (file != NULL || line > 0 || func != NULL) 
		fprintf(stderr, "DB: (%s:%d in %s()): %s\n",
			file, line, func, buf);
	else
		fprintf(stderr, "DB: %s\n",
			buf);

	/* Make sure it gets written. */
	fflush(stderr);
}

void sjme_todoR(const char* file, int line,
	const char* func, const char* message, ...)
{
	char buf[DEBUG_BUF];
	va_list args;

	/* Load message buffer. */
	va_start(args, message);
	memset(buf, 0, sizeof(buf));
	vsnprintf(buf, DEBUG_BUF, message, args);
	va_end(args);
	
	/* Print output message. */
	if (file != NULL || line > 0 || func != NULL)
		fprintf(stderr, "TD: TODO Hit (%s:%d in %s()): %s\n",
			file, line, func, buf);
	else
		fprintf(stderr, "TD: TODO Hit: %s\n",
			buf);
	
	/* Exit and stop. */
#if !defined(SJME_CONFIG_RELEASE)
	abort();
#endif
	
	/* Exit after abort happens, it can be ignored in debugging. */
	exit(EXIT_FAILURE);
}

