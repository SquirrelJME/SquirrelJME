/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * JVM Compatibility header.
 * 
 * @since 2025/10/22
 */

#ifndef SJME_C_SQUIRRELJME_JVM_H
#define SJME_C_SQUIRRELJME_JVM_H

#include "sjme/config.h"
#include "sjme/jvm/jvm_md.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_JVM_H

extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/
#pragma region(incompleteTypes)

/**
 * Simplifies incomplete types.
 *
 * @param x The name of the type.
 * @since 2026/06/28
 */
#define SJME_JVM_INCOMPLETE__(x) \
	typedef struct SJME_TOKEN_PASTE3(sjme_jvm_incomplete_, x, __)* x

/** DTrace Provider? */
SJME_JVM_INCOMPLETE__(JVM_DTraceProvider);

/** JVM_ExceptionTableEntryType? */
SJME_JVM_INCOMPLETE__(JVM_ExceptionTableEntryType);

/** JVM version info? */
SJME_JVM_INCOMPLETE__(jvm_version_info);

/* No longer needed. */
#undef SJME_JVM_INCOMPLETE__

#pragma endregion(incompleteTypes)
	
/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_JVM_H
}
#undef SJME_CXX_SQUIRRELJME_JVM_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_JVM_H */
#endif /* #ifdef __cplusplus */

#endif /* SJME_C_SQUIRRELJME_JVM_H */
