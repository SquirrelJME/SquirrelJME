/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/except.h"

sjme_errorCode sjme_except_gracefulDeathR(SJME_DEBUG_DECL_FILE_LINE_FUNC,
	sjme_attrInNotNull sjme_nvm_frame* frame,
	sjme_attrInRange(SJME_NUM_ERROR_CODES, SJME_ERROR_NONE)
		sjme_errorCode errorCode,
	sjme_attrInNotNull sjme_attrFormatArg const char* message, ...)
{
#if defined(SJME_CONFIG_DEBUG)
	va_list args;

	/* Emit debug message. */
	va_start(args, message);
	sjme_messageV(file, line, func, message, args);
	va_end(args);
	
	/* If debugging and not release version, mark to-do to fix issue. */
	sjme_todoR(file, line, func, "Fix bug! Because %d %08x!",
		(int)errorCode, (int)errorCode);
#else
	sjme_todo("Implement graceful exit for release??");
#endif
}
