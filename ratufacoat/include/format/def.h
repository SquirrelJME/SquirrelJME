/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Defines for packs and libraries.
 * 
 * @since 2022/01/09
 */

#ifndef SQUIRRELJME_FORMAT_DEF_H
#define SQUIRRELJME_FORMAT_DEF_H

#include "sjmerc.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_FORMAT_DEF_H
extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Instance of a pack which is a singular ROM which contains multiple JARs or
 * sets of classes.
 * 
 * @since 2021/09/19
 */
typedef struct sjme_packInstance sjme_packInstance;

/**
 * Instance of a library which represents a single JAR or set of classes.
 * 
 * @since 2021/09/19
 */ 
typedef struct sjme_libraryInstance sjme_libraryInstance;

/**
 * Represents a class path.
 * 
 * @since 2022/01/09
 */
typedef struct sjme_classPath
{
	/** The number of libraries in the class path. */
	sjme_jint count;
	
	/** The libraries within the class path. */
	sjme_libraryInstance* libraries[];
} sjme_classPath;

/**
 * Returns the size that would be used for @c sjme_classPath.
 * 
 * @param count The number of library instance references.
 * @return The allocated memory size for the type.
 * @since 2022/03/09
 */
#define SJME_SIZEOF_CLASS_PATH(count) (sizeof(sjme_classPath) + \
	((count) * sizeof(sjme_libraryInstance*)))

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_FORMAT_DEF_H
}
#undef SJME_CXX_SQUIRRELJME_FORMAT_DEF_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_FORMAT_DEF_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_FORMAT_DEF_H */
