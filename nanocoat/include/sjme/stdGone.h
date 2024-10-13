/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Functions which are completely missing from a system's native C runtime.
 * 
 * @since 2024/10/03
 */

#ifndef SQUIRRELJME_STDGONE_H
#define SQUIRRELJME_STDGONE_H

#include "sjme/config.h"

#if defined(SJME_CONFIG_HAS_NO_STDARG)
	#if defined(SJME_CONFIG_HAS_NO_VARARGS)
		#include <varargs.h>
	#else
		#error No stdarg or varargs?
	#endif
#else
	#include <stdarg.h>
#endif

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_STDGONE_H
extern "C"
{
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

#if defined(SJME_CONFIG_HAS_NO_SNPRINTF)
int snprintf(
	sjme_attrInNotNull char* buf,
	sjme_attrInPositive size_t bufSize,
	sjme_attrInNotNull const char* format,
	...);
#endif

#if defined(SJME_CONFIG_HAS_NO_VSNPRINTF)
int vsnprintf(
	sjme_attrInNotNull char* buf,
	sjme_attrInPositive size_t bufSize,
	sjme_attrInNotNull const char* format,
	sjme_attrInValue va_list args);
#endif

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_STDGONE_H
}
		#undef SJME_CXX_SQUIRRELJME_STDGONE_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_STDGONE_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_STDGONE_H */
