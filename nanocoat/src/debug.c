/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
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

static void sjme_genericMessage(const char* file, int line,
	const char* func, const char* prefix, const char* format, va_list args)
{
	va_list copy;
	char buf[DEBUG_BUF];

	/* Need to copy because this works differently on other arches. */
	va_copy(copy, args);
	
	/* Load message buffer. */
	if (format == NULL)
		strncpy(buf, "No message", DEBUG_BUF);
	else
	{
		memset(buf, 0, sizeof(buf));
		vsnprintf(buf, DEBUG_BUF, format, copy);
	}
	
	/* Cleanup the copy. */
	va_end(copy);
	
	/* Print output message. */
	if (file != NULL || line > 0 || func != NULL) 
		fprintf(stderr, "%s (%s:%d in %s()): %s\n",
			prefix, file, line, func, buf);
	else
		fprintf(stderr, "%s %s\n",
			prefix, buf);
	
	/* Make sure it gets written. */
	fflush(stderr);
}

#if defined(SJME_CONFIG_DEBUG)
void sjme_messageR(const char* file, int line,
	const char* func, const char* message, ...)
{
	va_list list;
	
	va_start(list, message);
	
	sjme_genericMessage(file, line, func, "DB", message,
		list);
		
	va_end(list);
}

void sjme_messageV(SJME_DEBUG_DECL_FILE_LINE_FUNC,
	sjme_attrInNullable sjme_attrFormatArg const char* message,
	va_list args)
{
	sjme_genericMessage(file, line, func, "DB", message,
		args);
}
#endif

jboolean sjme_dieR(const char* file, int line,
	const char* func, const char* message, ...)
{
	va_list list;
	va_list copy;
	
	va_start(list, message);
	
	sjme_genericMessage(file, line, func, "FATAL", message,
		list);
		
	va_end(list);
	
	/* Exit and stop. */
#if !defined(SJME_CONFIG_RELEASE)
	abort();
#endif
	
	/* Exit after abort happens, it can be ignored in debugging. */
	exit(EXIT_FAILURE);
	
	/* Never reaches, but returns false naturally. */
	return JNI_FALSE;
}

void sjme_todoR(const char* file, int line,
	const char* func, const char* message, ...)
{
	va_list list;
	
	va_start(list, message);
	
	sjme_genericMessage(file, line, func, "TD TODO HIT", message,
		list);
		
	va_end(list);
	
	/* Exit and stop. */
#if !defined(SJME_CONFIG_RELEASE)
	abort();
#endif
	
	/* Exit after abort happens, it can be ignored in debugging. */
	exit(EXIT_FAILURE);
}

