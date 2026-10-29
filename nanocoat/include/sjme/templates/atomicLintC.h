/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Purely used for linting so that static analyzers do not break on template
 * files.
 * 
 * @since 2026/10/29
 */

#ifndef SJME_C_SQUIRRELJME_ATOMICLINTC_H
#define SJME_C_SQUIRRELJME_ATOMICLINTC_H

#include "sjme/stdTypes.h"
#include "sjme/tokenUtils.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_ATOMICLINTC_H

extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

#if !defined(sjme_atomic_typeName) && \
	!defined(sjme_atomic_valueName) && \
	!defined(sjme_atomic_storageType) && \
	!defined(sjme_atomic_typeWidth)
	struct sjme_atomic_sjme_jint
	{
		sjme_jint value;
	};
	
	#define sjme_atomic_typeName struct sjme_atomic_sjme_jint
	#define sjme_atomic_valueName sjme_jint
	#define sjme_atomic_storageType sjme_jint
	#define sjme_atomic_typeWidth 4
#endif

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_ATOMICLINTC_H
}
#undef SJME_CXX_SQUIRRELJME_ATOMICLINTC_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_ATOMICLINTC_H */
#endif /* #ifdef __cplusplus */

#endif /* SJME_C_SQUIRRELJME_ATOMICLINTC_H */
