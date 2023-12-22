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

#if defined(_WIN32)
	#include <windows.h>
#endif

#include "sjme/nvm.h"
#include "sjme/debug.h"

/** Debug buffer size for messages. */
#define DEBUG_BUF 512

sjme_debug_abortHandlerFunc sjme_debug_abortHandler = NULL;

sjme_debug_exitHandlerFunc sjme_debug_exitHandler = NULL;

sjme_danglingMessageFunc sjme_danglingMessage = NULL;

void sjme_debug_abort(void)
{
#if !defined(SJME_CONFIG_RELEASE)
	/* Use specific abort handler? */
	if (sjme_debug_abortHandler != NULL)
		if (sjme_debug_abortHandler())
			return;

#if defined(_WIN32)
	/* When running tests without a debugger this will pop up about 1000 */
	/* dialogs saying the program aborted, so only abort on debugging. */
	if (!IsDebuggerPresent())
		return;
#endif

	/* Otherwise use C abort handler. */
	abort();
#endif
}

/**
 * Potentially debug exits.
 *
 * @param exitCode The exit code.
 * @since 2023/12/21
 */
static void sjme_debug_exit(int exitCode)
{
	/* Use specific exit handler? */
	if (sjme_debug_exitHandler != NULL)
		if (sjme_debug_exitHandler(exitCode))
			return;

	/* Fallback to normal exit. */
	exit(exitCode);
}

sjme_lpcstr sjme_shortenFile(sjme_lpcstr file)
{
	sjme_jint i, n;
	
	/* There is nothing to shorten. */
	if (file == NULL)
		return NULL;
	
	/* Try to find nanocoat in there. */
	n = strlen(file);
	for (i = (n - 11 >= 0 ? n - 11 : 0); i >= 0; i--)
	{
		if (0 == memcmp(&file[i], "/nanocoat/", 10) ||
			0 == memcmp(&file[i], "\\nanocoat\\", 10))
			return &file[i + 10];
	}
	
	/* Use the full name regardless. */
	return file;
}

void sjme_genericMessage(sjme_lpcstr file, int line,
	sjme_lpcstr func, sjme_lpcstr prefix, sjme_lpcstr format, va_list args)
{
	va_list copy;
	char buf[DEBUG_BUF];
	char fullBuf[DEBUG_BUF];
	int hasPrefix;
	
	/* Need to copy because this works differently on other arches. */
	va_copy(copy, args);
	
	/* Load message buffer. */
	if (format == NULL)
		strncpy(buf, "No message", DEBUG_BUF);
	else
	{
		memset(buf, 0, sizeof(buf));
		vsnprintf(buf, DEBUG_BUF - 1, format, copy);
	}
	
	/* Cleanup the copy. */
	va_end(copy);
	
	/* Print output message. */
	hasPrefix = (prefix != NULL && strlen(prefix) > 0);
	memset(fullBuf, 0, sizeof(fullBuf));
	if (file != NULL || line > 0 || func != NULL)
		snprintf(fullBuf, DEBUG_BUF - 1,
			"%s%s(%s:%d in %s()): %s",
			prefix, (hasPrefix ? " " : ""),
			sjme_shortenFile(file), line, func, buf);
	else
		snprintf(fullBuf, DEBUG_BUF - 1,
			"%s%s%s",
			prefix, (hasPrefix ? " " : ""), buf);
		
	/* First try to print to the frontend callback, if any. */
	if (sjme_danglingMessage == NULL ||
		!sjme_danglingMessage(fullBuf, buf))
		fprintf(stderr, "%s\n", fullBuf);
	
	/* Make sure it gets written. */
	fflush(stderr);
}

void sjme_messageR(sjme_lpcstr file, int line,
	sjme_lpcstr func, sjme_jboolean isBlank, sjme_lpcstr message, ...)
{
	va_list list;
	
	va_start(list, message);
	
	sjme_genericMessage(file, line, func,
		(isBlank ? "" : "DB"), message,
		list);
		
	va_end(list);
}

void sjme_messageV(SJME_DEBUG_DECL_FILE_LINE_FUNC,
	sjme_jboolean isBlank,
	sjme_attrInNullable sjme_attrFormatArg sjme_lpcstr message,
	va_list args)
{
	sjme_genericMessage(file, line, func,
		(isBlank ? "" : "DB"), message,
		args);
}

sjme_errorCode sjme_dieR(sjme_lpcstr file, int line,
	sjme_lpcstr func, sjme_lpcstr message, ...)
{
	va_list list;
	va_list copy;
	
	va_start(list, message);
	
	sjme_genericMessage(file, line, func, "FATAL", message,
		list);
		
	va_end(list);
	
	/* Exit and stop. */
	sjme_debug_abort();
	
	/* Exit after abort happens, it can be ignored in debugging. */
	sjme_debug_exit(EXIT_FAILURE);
	
	/* Never reaches, but returns false naturally. */
	return SJME_ERROR_UNKNOWN;
}

void sjme_todoR(sjme_lpcstr file, int line,
	sjme_lpcstr func, sjme_lpcstr message, ...)
{
	va_list list;
	
	va_start(list, message);
	
	sjme_genericMessage(file, line, func, "TD TODO HIT", message,
		list);
		
	va_end(list);
	
	/* Exit and stop. */
	sjme_debug_abort();
	
	/* Exit after abort happens, it can be ignored in debugging. */
	sjme_debug_exit(EXIT_FAILURE);
}

