/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/config.h"

#if defined(SJME_CONFIG_HAS_OS_WINDOWS)
	#define WIN32_LEAN_AND_MEAN 1
	
	#include <windows.h>
	#include <winternl.h>
	
	#if SJME_CONFIG_WINDOWS_VERSION_NT_LEAST(SJME_CONFIG_WINDOWS_VERSION_NT_4)
		#include <debugapi.h>
	#endif

	#undef WIN32_LEAN_AND_MEAN
#elif defined(SJME_CONFIG_HAS_OS_POSIX)
	#include <signal.h>
#endif

#include "sjme/debug.h"
#include "sjme/alloc.h"
#include "sjme/dylib.h"

/** Debug buffer size for messages. */
#define DEBUG_BUF 512

/** The crash function to call. */
sjme_threadLocal(sjme_thread_mainFunc, sjme_debug_crashFunc);

/** The parameter to pass to the crash function. */
sjme_threadLocal(sjme_thread_parameter, sjme_debug_crashFuncParam);

sjme_attrExport sjme_attrSelectAnyWeak
	sjme_debug_handlerFunctions* sjme_debug_handlers = NULL;

#if defined(SJME_CONFIG_HAS_OS_POSIX)
static void sjme_debug_crashPosix(int signalId)
{
	sjme_thread_mainFunc crashFunc;
	sjme_thread_parameter crashParam;

	/* No longer handle the signal, otherwise an infinite loop occurs. */
	signal(signalId, SIG_DFL);
	
	/* Call the crash function, if it was set. */
	crashFunc = sjme_debug_crashFunc;
	crashParam = sjme_debug_crashFuncParam;
	if (crashFunc != NULL)
		crashFunc(crashParam);
	
	/* Raise the signal in this process, so that it actually crashes. */
	raise(signalId);
}
#endif

sjme_jboolean sjme_debug_abort(sjme_errorCode error)
{
	static sjme_atomic(sjme_jint) didAbort;
#if SJME_CONFIG_WINDOWS_VERSION_NT_LEAST(SJME_CONFIG_WINDOWS_VERSION_NT_4)
	PPEB peb;
#endif

	/* Only trigger abort once. */
	if (sjme_atomic_cs(sjme_jint, &didAbort, 0, 1))
	{
		/* Use specific abort handler? */
		if (sjme_debug_handlers != NULL && sjme_debug_handlers->abort != NULL)
			if (sjme_debug_handlers->abort(error))
				return SJME_JNI_TRUE;

#if SJME_CONFIG_WINDOWS_VERSION_NT_LEAST(SJME_CONFIG_WINDOWS_VERSION_NT_4)
		/* When running tests without a debugger this will pop up about 1000 */
		/* dialogs saying the program aborted, so only abort on debugging. */
		if (!IsDebuggerPresent())
			return SJME_JNI_FALSE;

#if defined(SJME_CONFIG_DEBUG) && \
	!(defined(__MINGW32__) || defined(__MINGW64__))
		/* Do not pop up an annoying dialog. */
		_set_abort_behavior(0, _WRITE_ABORT_MSG | _CALL_REPORTFAULT);

		/* Breakpoint. */
		__debugbreak();
#endif
#endif
		
		/* Use C abort handler. */
		abort();

		/* We skipped abort, so clear it. */
		sjme_atomic_cs(sjme_jint, &didAbort, 1, 0);

		/* Triggered abort. */
		return SJME_JNI_TRUE;
	}

	/* Did not trigger. */
	return SJME_JNI_FALSE;
}

void sjme_debug_crashContext(
	sjme_attrInNullable sjme_thread_mainFunc crashFunc,
	sjme_attrInNullable sjme_thread_parameter crashParam)
{
	/* Just replaces the other. */
	sjme_debug_crashFunc = crashFunc;
	sjme_debug_crashFuncParam = crashParam;
}

sjme_errorCode sjme_debug_crashRegister(void)
{
#if defined(SJME_CONFIG_HAS_OS_POSIX)
	sjme_errorCode error;

	/* These are the general memory and computation related signals. */
	error = SJME_ERROR_NONE;
	if (signal(SIGSEGV, sjme_debug_crashPosix) == SIG_ERR)
		error = SJME_ERROR_INVALID_ARGUMENT;
	if (signal(SIGBUS, sjme_debug_crashPosix) == SIG_ERR)
		error = SJME_ERROR_INVALID_ARGUMENT;
	if (signal(SIGILL, sjme_debug_crashPosix) == SIG_ERR)
		error = SJME_ERROR_INVALID_ARGUMENT;
	if (signal(SIGFPE, sjme_debug_crashPosix) == SIG_ERR)
		error = SJME_ERROR_INVALID_ARGUMENT;
	
	return error;
#else
	return SJME_ERROR_NOT_IMPLEMENTED;
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
	sjme_attrInNullable sjme_alloc_pool allocPool,
	sjme_attrInValue sjme_intPointer context)
{
#if defined(SJME_CONFIG_DEBUG)
	/* Dump entire pool contents. */
	if (allocPool != NULL)
		sjme_alloc_poolDump(allocPool, SJME_JNI_FALSE);

	/* It could be huge... */
	sjme_todoR(file, line, func, "OUT OF MEMORY %p: %d %p!",
		allocPool, (int)context, (void*)context);
#endif

	return SJME_ERROR_OUT_OF_MEMORY;
}

void sjme_genericMessage(sjme_lpcstr file, int line,
	sjme_lpcstr func, sjme_lpcstr prefix, sjme_lpcstr format, va_list args)
{
#if !defined(SJME_CONFIG_HAS_NO_STDIO)
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

	/* Make sure it gets written somewhere. */
	if (!handled)
	{
		fprintf(stderr, "%s\n", fullBuf);
		fflush(stderr);
	}
#endif
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
	if (sjme_debug_abort(SJME_ERROR_UNKNOWN_NEGATIVE))
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
	if (sjme_debug_abort(SJME_ERROR_UNKNOWN_NEGATIVE))
		sjme_debug_exit(EXIT_FAILURE);
}

