/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#if defined(SJME_CONFIG_HAS_NO_STDARG)
	#if defined(SJME_CONFIG_HAS_NO_VARARGS)
		#include <varargs.h>
	#else
	#endif
#else
	#include <stdarg.h>
#endif

#include "sjme/stdgone.h"

#if defined(SJME_CONFIG_HAS_NO_SNPRINTF)
int snprintf(
	sjme_attrInNotNull char* buf,
	sjme_attrInPositive size_t bufSize,
	sjme_attrInNotNull const char* format,
	...)
{
	va_list args;
	int result;
	
	if (buf == NULL || format == NULL || bufSize <= 0)
		return -1;
	
	va_start(args, format);

	/* Perform the printing. */
#if defined(MSC_VER)
	result = _vsnprintf(buf, bufSize, format, args);
	buf[bufSize - 1] = 0;
#else
	result = -1;
#endif
	
	va_end(args, format);
	
	return result;
}
#endif
