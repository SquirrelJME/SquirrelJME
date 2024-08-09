/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/except.h"
#include "sjme/debug.h"

void sjme_genericMessage(sjme_lpcstr file, int line,
	sjme_lpcstr func, sjme_lpcstr prefix, sjme_lpcstr format, va_list args);

sjme_errorCode sjme_except_printStackTraceR(SJME_DEBUG_DECL_FILE_LINE_FUNC,
	sjme_errorCode errorCode, volatile sjme_exceptTrace* exceptTrace)
{
	volatile sjme_exceptTrace* seeker;

	/* Add notice, similar to Java. */
	sjme_messageR(NULL, -1, NULL, SJME_JNI_TRUE,
		"EXCEPTION native: Error %d",
		errorCode);

	/* Go down the stack. */
	seeker = exceptTrace;
	while (seeker != NULL)
	{
		/* Print indicators. */
		if (seeker->file != NULL)
		{
			if (seeker->line >= 0)
				sjme_messageR(NULL, -1, NULL, SJME_JNI_TRUE,
					" | IN %s() (%s:%d)",
					seeker->func,
					sjme_debug_shortenFile(seeker->file),
					seeker->line);
			else
				sjme_messageR(NULL, -1, NULL, SJME_JNI_TRUE,
					" | IN %s() (%s)",
					seeker->func,
					sjme_debug_shortenFile(seeker->file));
		}
		else
		{
			if (seeker->line >= 0)
				sjme_messageR(NULL, -1, NULL, SJME_JNI_TRUE,
					" | IN %s(:%d)",
					seeker->func,
					seeker->line);
			else
				sjme_messageR(NULL, -1, NULL, SJME_JNI_TRUE,
					" | IN %s()",
					seeker->func);
		}

		/* Go up one. */
		seeker = seeker->parent;
	}

	/* Always works. */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_except_gracefulDeathR(SJME_DEBUG_DECL_FILE_LINE_FUNC,
	sjme_attrInNotNull sjme_nvm_frame frame,
	sjme_attrInRange(SJME_NUM_ERROR_CODES, SJME_ERROR_NONE)
		sjme_errorCode errorCode,
	sjme_attrInNotNull sjme_attrFormatArg sjme_lpcstr message, ...)
{
#if defined(SJME_CONFIG_DEBUG)
	va_list args;

	/* Emit debug message. */
	va_start(args, message);
	sjme_messageV(file, line, func, SJME_JNI_FALSE, message, args);
	va_end(args);
	
	/* If debugging and not release version, mark to-do to fix issue. */
	sjme_todoR(file, line, func, "Fix bug! Because %d %08x!",
		(int)errorCode, (int)errorCode);
#else
	sjme_todo("Implement graceful exit for release??");
#endif
}
