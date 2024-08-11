/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Path handling abstraction.
 * 
 * @since 2024/08/09
 */

#ifndef SQUIRRELJME_PATH_H
#define SQUIRRELJME_PATH_H

#include "sjme/config.h"
#include "sjme/error.h"
#include "sjme/stdTypes.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_PATH_H
extern "C"
{
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/** The maximum path length in SquirrelJME. */
#define SJME_MAX_PATH 1024

#if defined(SJME_CONFIG_HAS_DOS)
	/** Short paths. */
	#defne SJME_PATH_SHORT
#endif

#if defined(SJME_CONFIG_HAS_WINDOWS) || defined(SJME_CONFIG_HAS_MACOS_CLASSIC)
	/** Separator for PATH and classpath. */
	#define SJME_CONFIG_PATH_SEPARATOR ";"
#else
	/** Separator for PATH and classpath. */
	#define SJME_CONFIG_PATH_SEPARATOR ":"
#endif

/** DOS path style. */
#define SJME_CONFIG_PATH_STYLE_DOS 1

/** Macintosh path style. */
#define SJME_CONFIG_PATH_STYLE_MACOS_CLASSIC 2

/** UNIX path style. */
#define SJME_CONFIG_PATH_STYLE_UNIX 3

#if defined(SJME_CONFIG_HAS_WINDOWS) || defined(SJME_CONFIG_HAS_DOS)
	/** Path style in use. */
	#define SJME_CONFIG_PATH_STYLE SJME_CONFIG_PATH_STYLE_DOS
#elif defined(SJME_CONFIG_HAS_MACOS_CLASSIC)
	/** Path style in use. */
	#define SJME_CONFIG_PATH_STYLE SJME_CONFIG_PATH_STYLE_MACOS_CLASSIC
#else
	/** Path style in use. */
	#define SJME_CONFIG_PATH_STYLE SJME_CONFIG_PATH_STYLE_UNIX
#endif

#if SJME_CONFIG_PATH_STYLE == SJME_CONFIG_PATH_STYLE_DOS
	/** Separator for file paths. */
	#define SJME_CONFIG_FILE_SEPARATOR "\\"
#elif SJME_CONFIG_PATH_STYLE == SJME_CONFIG_PATH_STYLE_MACOS_CLASSIC
	/** Separator for file paths. */
	#define SJME_CONFIG_FILE_SEPARATOR ":"
#elif SJME_CONFIG_PATH_STYLE == SJME_CONFIG_PATH_STYLE_UNIX
	/** Separator for file paths. */
	#define SJME_CONFIG_FILE_SEPARATOR "/"
#else
	#error Unknown native path style.
#endif

/**
 * Gets the given name at the given index, usage is more basic than the
 * full version.
 * 
 * @param inPath The input path. 
 * @param inPathLen The input path length.
 * @param inName The name index to get, if @c -1 this will return the
 * root component if there is one.
 * @param outBase The pointer to the path base.
 * @param outLen The length of the path name.
 * @return Any resultant error, if any. Returns @c SJME_ERROR_NO_SUCH_ELEMENT
 * if the root component was requested and there was none.
 * @since 2024/08/10
 */
sjme_errorCode sjme_path_getName(
	sjme_attrInNotNull sjme_lpcstr inPath,
	sjme_attrInPositive sjme_jint inPathLen,
	sjme_attrInNegativeOnePositive sjme_jint inName,
	sjme_attrOutNullable sjme_lpcstr* outBase,
	sjme_attrOutNullable sjme_jint* outLen);

/**
 * Gets the given name at the given index.
 * 
 * @param inPath The input path. 
 * @param inPathLen The input path length.
 * @param inName The name index to get, if @c -1 this will return the
 * root component if there is one.
 * @param outBase The pointer to the path base.
 * @param outBaseDx The index of the path base.
 * @param outEnd The pointer to the path end.
 * @param outEndDx The index of the path end.
 * @param outLen The length of the path name.
 * @param outCount The number of name components, exclusive to all the
 * other arguments.
 * @return Any resultant error, if any. Returns @c SJME_ERROR_NO_SUCH_ELEMENT
 * if the root component was requested and there was none.
 * @since 2024/08/10
 */
sjme_errorCode sjme_path_getNameF(
	sjme_attrInNotNull sjme_lpcstr inPath,
	sjme_attrInPositive sjme_jint inPathLen,
	sjme_attrInNegativeOnePositive sjme_jint inName,
	sjme_attrOutNullable sjme_lpcstr* outBase,
	sjme_attrOutNullable sjme_jint* outBaseDx,
	sjme_attrOutNullable sjme_lpcstr* outEnd,
	sjme_attrOutNullable sjme_jint* outEndDx,
	sjme_attrOutNullable sjme_jint* outLen,
	sjme_attrOutNullable sjme_jint* outCount);

/**
 * Gets the number of names that appear in the given path.
 * 
 * @param inPath The input path.
 * @param inPathLen The input path length.
 * @param outCount The resultant name count.
 * @return Any resultant error, if any.
 * @since 2024/08/10
 */
sjme_errorCode sjme_path_getNameCount(
	sjme_attrInNotNull sjme_lpcstr inPath,
	sjme_attrInPositive sjme_jint inPathLen,
	sjme_attrOutNotNull sjme_attrOutPositive sjme_jint* outCount); 

/**
 * Returns whether the given path as a root.
 * 
 * @param inPath The path to check. 
 * @param inPathLen The input path length.
 * @param hasRoot If there is a root or not.
 * @return Any resultant error, if any.
 * @since 2024/08/10 
 */
sjme_errorCode sjme_path_hasRoot(
	sjme_attrInNotNull sjme_lpcstr inPath,
	sjme_attrInPositive sjme_jint inPathLen,
	sjme_attrOutNotNull sjme_jboolean* hasRoot);

/**
 * Resolves the given path onto the given path buffer.
 * 
 * @param outPath The resultant path to be modified.
 * @param outPathLen The size of the path buffer.
 * @param subPath The sub-path to resolve against.
 * @param subPathLen The sub-path length, if @c INT32_MAX this means all
 * of it.
 * @return Any resultant error, if any.
 * @since 2024/08/09
 */
sjme_errorCode sjme_path_resolveAppend(
	sjme_attrOutNotNull sjme_lpstr outPath,
	sjme_attrInPositiveNonZero sjme_jint outPathLen,
	sjme_attrInNotNull sjme_lpcstr subPath,
	sjme_attrInPositiveNonZero sjme_jint subPathLen);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_PATH_H
}
		#undef SJME_CXX_SQUIRRELJME_PATH_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_PATH_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_PATH_H */
