/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include <stdarg.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "sjme/stdTypes.h"

#if defined(SJME_CONFIG_HAS_WINDOWS)
	#define WIN32_LEAN_AND_MEAN 1
	
	#include <windows.h>
	#include <debugapi.h>

	#undef WIN32_LEAN_AND_MEAN
#endif

#include "sjme/debug.h"
#include "sjme/alloc.h"

/** Debug buffer size for messages. */
#define DEBUG_BUF 512

sjme_debug_handlerFunctions* sjme_debug_handlers = NULL;

void sjme_debug_abort(void)
{
	/* Use specific abort handler? */
	if (sjme_debug_handlers != NULL && sjme_debug_handlers->abort != NULL)
		if (sjme_debug_handlers->abort())
			return;

#if defined(SJME_CONFIG_HAS_WINDOWS)
	/* When running tests without a debugger this will pop up about 1000 */
	/* dialogs saying the program aborted, so only abort on debugging. */
	if (!IsDebuggerPresent())
		return;
#endif

	/* Otherwise use C abort handler. */
	abort();
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
	if (sjme_debug_handlers != NULL && sjme_debug_handlers->exit != NULL)
		if (sjme_debug_handlers->exit(exitCode))
			return;

	/* Fallback to normal exit. */
	exit(exitCode);
}

sjme_lpcstr sjme_debug_shortenFile(sjme_lpcstr file)
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

sjme_errorCode sjme_error_fatalR(SJME_DEBUG_DECL_FILE_LINE_FUNC,
	sjme_attrInValue sjme_errorCode error)
{
#if defined(SJME_CONFIG_DEBUG)
	sjme_dieR(file, line, func, "FATAL ERROR: %d!", error);
#endif
	
	return sjme_error_default(error);
}

sjme_errorCode sjme_error_notImplementedR(SJME_DEBUG_DECL_FILE_LINE_FUNC,
	sjme_attrInValue sjme_intPointer context)
{
#if defined(SJME_CONFIG_DEBUG)
	sjme_todoR(file, line, func, "NOT IMPLEMENTED: %d %p!",
		(int)context, (void*)context);
#endif
	
	return SJME_ERROR_NOT_IMPLEMENTED;
}

sjme_errorCode sjme_error_outOfMemoryR(SJME_DEBUG_DECL_FILE_LINE_FUNC,
	sjme_attrInNullable sjme_alloc_pool* inPool,
	sjme_attrInValue sjme_intPointer context)
{
#if defined(SJME_CONFIG_DEBUG)
	/* Dump entire pool contents. */
	if (inPool != NULL)
		sjme_alloc_poolDump(inPool);

	/* It could be huge... */
	sjme_todoR(file, line, func, "OUT OF MEMORY %p: %d %p!",
		inPool, (int)context, (void*)context);
#endif

	return SJME_ERROR_OUT_OF_MEMORY;
}

void sjme_genericMessage(sjme_lpcstr file, int line,
	sjme_lpcstr func, sjme_lpcstr prefix, sjme_lpcstr format, va_list args)
{
	va_list copy;
	char buf[DEBUG_BUF];
	char fullBuf[DEBUG_BUF];
	int hasPrefix;
	sjme_jboolean handled;
	
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
			sjme_debug_shortenFile(file), line, func, buf);
	else
		snprintf(fullBuf, DEBUG_BUF - 1,
			"%s%s%s",
			prefix, (hasPrefix ? " " : ""), buf);
		
	/* First try to print to the frontend callback, if any. */
	handled = SJME_JNI_FALSE;
	if (sjme_debug_handlers != NULL && sjme_debug_handlers->message != NULL)
		handled = sjme_debug_handlers->message(
			fullBuf, buf);
	
	if (!handled)
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

static void sjme_message_hexDumpChar(sjme_lpstr* w, sjme_lpstr end,
	sjme_jint c)
{
	if ((*w) < end)
		*((*w)++) = c;
}

static void sjme_message_hexDumpHex(sjme_lpstr* w, sjme_lpstr end,
	sjme_jint c)
{
	if (c < 10)
		sjme_message_hexDumpChar(w, end, '0' + c);
	else
		sjme_message_hexDumpChar(w, end, 'A' + (c - 10));
}

void sjme_message_hexDump(
	sjme_attrInNullable sjme_buffer inData,
	sjme_attrInPositive sjme_jint inLen)
{
#define SJME_HEX_LINE 12
	sjme_jint at, sub, i, c;
	sjme_cchar buf[DEBUG_BUF];
	sjme_lpstr w, end;
	
	if (inData == NULL || inLen <= 0)
		return;
	
	/* Print all sequences. */
	for (at = 0; at < inLen; at += SJME_HEX_LINE)
	{
		/* Clear buffer. */
		memset(buf, 0, sizeof(buf));
		w = buf;
		end = &buf[DEBUG_BUF - 1];
		
		/* Print hex first. */
		for (sub = at, i = 0; sub < inLen && i < SJME_HEX_LINE; sub++, i++)
		{
			/* Get byte from the input. */
			c = ((sjme_jubyte*)inData)[sub];
			
			/* Write hex. */
			sjme_message_hexDumpHex(&w, end, (c >> 4) & 0xF);
			sjme_message_hexDumpHex(&w, end, c & 0xF);
				
			/* Space. */ 
			sjme_message_hexDumpChar(&w, end, ' '); 
		}
		
		/* Split to hex. */
		sjme_message_hexDumpChar(&w, end, '|');
		
		/* Then characters. */
		for (sub = at, i = 0; sub < inLen && i < SJME_HEX_LINE; sub++, i++)
		{
			/* Get byte from the input. */
			c = ((sjme_jubyte*)inData)[sub];
			
			if (c >= ' ' && c < 0x7F)
				sjme_message_hexDumpChar(&w, end, c);
			else
				sjme_message_hexDumpChar(&w, end, '.');
		}
		
		/* End splice. */
		sjme_message_hexDumpChar(&w, end, '|');
		
		/* Send out the buffer. */
		sjme_messageR(NULL, -1, NULL, SJME_JNI_TRUE,
			"%s", buf);
	}

#undef SJME_HEX_LINE
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

