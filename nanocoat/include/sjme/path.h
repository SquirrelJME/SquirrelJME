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
 * @file
 * @since 2024/08/09
 */

#ifndef SJME_C_PATH_H
#define SJME_C_PATH_H

#include "sjme/config.h"
#include "sjme/error.h"
#include "sjme/stdTypes.h"
#include "sjme/nvm/mleConst.h"
#include "sjme/native.h"

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

/** The maximum file name length in SquirrelJME. */
#define SJME_MAX_FILE_NAME 128

/** The maximum path length in SquirrelJME. */
#define SJME_MAX_PATH 1024

/**
 * The maximum path depth in SquirrelJME, this should be able to store the
 * most reasonable amount of names.
 *
 * On my current Debian system, /var has:
 * @code
 *      1 /
 *     12 //
 *    193 ///
 *    225 ////
 *    181 /////
 *    371 //////
 *    133 ///////
 *    238 ////////
 *    655 /////////
 *    992 //////////
 *   2238 ///////////
 *   4128 ////////////
 *   6868 /////////////
 *   5679 //////////////
 *   5504 ///////////////
 *   3693 ////////////////
 *   1663 /////////////////
 *   1131 //////////////////
 *    740 ///////////////////
 *    209 ////////////////////
 *    100 /////////////////////
 *     40 //////////////////////
 *     23 ///////////////////////
 *     19 ////////////////////////
 *      1 /////////////////////////
 *      1 ////////////////////////// <-- 26
 * @endcode
 */
#define SJME_MAX_PATH_DEPTH 32

#if defined(SJME_CONFIG_HAS_OS_PC_DOS)
	/** Short paths. */
	#define SJME_PATH_SHORT
#endif

#if defined(SJME_CONFIG_HAS_OS_WINDOWS) || \
	defined(SJME_CONFIG_HAS_OS_MACOS_CLASSIC)
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

/** Windows path style. */
#define SJME_CONFIG_PATH_STYLE_WINDOWS 4

#if defined(SJME_CONFIG_HAS_OS_WINDOWS) || \
	defined(SJME_CONFIG_HAS_OS_WINDOWS_CE)
	/** Path style in use. */
	#define SJME_CONFIG_PATH_STYLE SJME_CONFIG_PATH_STYLE_WINDOWS
#elif defined(SJME_CONFIG_HAS_OS_PC_DOS)
	/** Path style in use. */
	#define SJME_CONFIG_PATH_STYLE SJME_CONFIG_PATH_STYLE_DOS
#elif defined(SJME_CONFIG_HAS_OS_MACOS_CLASSIC)
	/** Path style in use. */
	#define SJME_CONFIG_PATH_STYLE SJME_CONFIG_PATH_STYLE_MACOS_CLASSIC
#else
	/** Path style in use. */
	#define SJME_CONFIG_PATH_STYLE SJME_CONFIG_PATH_STYLE_UNIX
#endif

/* Windows or DOS path style? */
#define SJME_CONFIG_PATH_STYLE_IS_DOS_OR_WINDOWS \
	(SJME_CONFIG_PATH_STYLE == SJME_CONFIG_PATH_STYLE_DOS || \
	SJME_CONFIG_PATH_STYLE == SJME_CONFIG_PATH_STYLE_WINDOWS)

#if SJME_CONFIG_PATH_STYLE == SJME_CONFIG_PATH_STYLE_WINDOWS
	/** Separator for file paths. */
	#define SJME_CONFIG_FILE_SEPARATOR '\\'
	
	/** Alternative separator for file paths. */
	#define SJME_CONFIG_FILE_SEPARATOR_ALT '/'
#elif SJME_CONFIG_PATH_STYLE == SJME_CONFIG_PATH_STYLE_DOS
	/** Separator for file paths. */
	#define SJME_CONFIG_FILE_SEPARATOR '\\'
	
	/** Alternative separator for file paths. */
	#define SJME_CONFIG_FILE_SEPARATOR_ALT '\\'

	/** DOS uses uppercase filenames. */
	#define SJME_CONFIG_PATH_STYLE_IS_UPPERCASE
#elif SJME_CONFIG_PATH_STYLE == SJME_CONFIG_PATH_STYLE_MACOS_CLASSIC
	/** Separator for file paths. */
	#define SJME_CONFIG_FILE_SEPARATOR ':'
	
	/** Alternative separator for file paths. */
	#define SJME_CONFIG_FILE_SEPARATOR_ALT ':'
#elif SJME_CONFIG_PATH_STYLE == SJME_CONFIG_PATH_STYLE_UNIX
	/** Separator for file paths. */
	#define SJME_CONFIG_FILE_SEPARATOR '/'
	
	/** Alternative separator for file paths. */
	#define SJME_CONFIG_FILE_SEPARATOR_ALT '/'
#else
	#error Unknown native path style.
#endif

/**
 * Flags that may be associated with a path.
 *
 * @since 2025/12/09
 */
typedef enum sjme_path_flags
{
	/** Does this path represent a directory? */
	SJME_PATH_IS_DIRECTORY = INT32_C(0x01),

	/** Does this path have a root component? This makes a path absolute. */
	SJME_PATH_HAS_ROOT = INT32_C(0x02),

	/** Is this path relative? That is there is no root */
	SJME_PATH_IS_RELATIVE = INT32_C(0x04),

	/** All path flags. */
	SJME_PATH_ALL_FLAGS = SJME_PATH_IS_DIRECTORY |
		SJME_PATH_HAS_ROOT | SJME_PATH_IS_RELATIVE,
} sjme_path_flags;
	
/**
 * Generic file system path.
 *
 * @since 2025/12/09
 */
typedef struct sjme_path
{
	/** The characters which make up the path. */
	sjme_cchar chars[SJME_MAX_PATH];

	/** Must always be zero. */
	sjme_jint zero;

	/** The flags for this path. */
	sjme_jint flags;

	/** The length of the entire path. */
	sjme_jushort length;
	
	/** The names within this path. */ 
	sjme_jushort nameCount;

	/**
	 * The offsets for each path name. Every name that prefaces the last name
	 * shall end in a directory separator.
	 */
	sjme_jushort names[SJME_MAX_PATH_DEPTH + 1];
} sjme_path;

/**
 * Checks if the given path is valid.
 * 
 * @param path The path to check.
 * @return Any resultant error, if any.
 * @since 2025/12/10
 */
sjme_errorCode sjme_path_check(
	sjme_attrInNotNull const sjme_path* path);

/**
 * Checks if the given path is denormalized.
 * 
 * @param path The path to check.
 * @param requireAbsolute Require that the input path be absolute, if it is
 * not an absolute path then this will fail.
 * @return Any resultant error, if any.
 * @since 2025/12/10
 */
sjme_errorCode sjme_path_checkDenormal(
	sjme_attrInNotNull const sjme_path* path,
	sjme_attrInValue sjme_jboolean requireAbsolute);

/**
 * Returns a default system path.
 * 
 * @param nal The native abstraction layer to use, if this is not specified
 * then the default is used.
 * @param outPath The output path.
 * Returns @link SJME_ERROR_PATH_NOT_DEFINED @endlink if the requested
 * directory is not defined.
 * @param type The type of path to obtain.
 * @param index The index of the path to use, if @c -1 then only the valid
 * possible match is used.
 * @since 2025/12/07
 */
sjme_errorCode sjme_path_default(
	sjme_attrInNullable const sjme_nal* nal,
	sjme_attrOutNotNull sjme_attrOutOverwrite sjme_path* outPath,
	sjme_attrInValue sjme_nvm_defaultDirectoryType type,
	sjme_attrInNegativeOnePositive sjme_jint index);

/**
 * Gets the specific name index for the given path.
 * 
 * @param outPath The output path.
 * @param inPath The input path.
 * @param nameDx The specified name index to obtain.
 * @return Any resultant error, if any.
 * @since 2025/12/09
 */
sjme_errorCode sjme_path_getName(
	sjme_attrOutNotNull sjme_attrOutOverwrite sjme_path* outPath,
	sjme_attrInNotNull const sjme_path* inPath,
	sjme_attrInPositive sjme_jint nameDx);

/**
 * Gets the specific name index for the given path to be used with
 * any @code printf("%.*s") @endcode style statement.
 * 
 * @param outFLimit The limit to the number of characters.
 * @param outFStr The base pointer.
 * @param inPath The input path.
 * @param nameDx The specified name index to obtain.
 * @return Any resultant error, if any.
 * @since 2025/12/09
 */
sjme_errorCode sjme_path_getNameF(
	sjme_attrOutNotNull sjme_attrOutOverwrite sjme_jint* outFLimit,
	sjme_attrOutNotNull sjme_attrOutOverwrite sjme_lpcstr* outFStr,
	sjme_attrInNotNull const sjme_path* inPath,
	sjme_attrInPositive sjme_jint nameDx);

/**
 * Gets the parent of the current path, up to the root component.
 * 
 * @param outPath The output path.
 * @param inPath The input path.
 * @return Any resultant error, if any.
 * @since 2025/12/09
 */
sjme_errorCode sjme_path_getParent(
	sjme_attrOutNotNull sjme_attrOutOverwrite sjme_path* outPath,
	sjme_attrInNotNull const sjme_path* inPath);

/**
 * Gets the root component of the path, if any.
 * 
 * @param outPath The output path.
 * @param inPath The input path.
 * @return Any resultant error, if any.
 * @since 2025/12/09
 */
sjme_errorCode sjme_path_getRoot(
	sjme_attrOutNotNull sjme_attrOutOverwrite sjme_path* outPath,
	sjme_attrInNotNull const sjme_path* inPath);

/**
 * Normalizes the given path, this removes all relative components where
 * possible.
 * 
 * @param outPath The output path.
 * @param inPath The input path.
 * @return Any resultant error, if any.
 * @since 2025/12/09
 */
sjme_errorCode sjme_path_normalize(
	sjme_attrOutNotNull sjme_attrOutOverwrite sjme_path* outPath,
	sjme_attrInNotNull const sjme_path* inPath);

/**
 * Parses the given path.
 *
 * Path parsing performs basic standardization of paths:
 * @code
 * "//////aa/////bb////cc" -> "/a/b/c"
 * "//aa/////bb////cc" -> "//a/b/c"
 * "a//b" -> "a/b"
 * "C:\\aa" -> "C:\a"
 * "C:/hello" -> "C:\hello"
 * "\\hello\world//maybe" -> "\\hello\world\maybe"
 * "c:\\okay" -> "C:\OKAY" (DOS)
 * @endcode
 * 
 * @param outPath The output path.
 * @param strPath The string based path.
 * @return Any resultant error, if any.
 * @since 2025/12/09
 */
sjme_errorCode sjme_path_parse(
	sjme_attrOutNotNull sjme_attrOutOverwrite sjme_path* outPath,
	sjme_attrInNotNull sjme_lpcstr strPath);

/**
 * Parses the given path with format specifiers.
 * 
 * @param outPath The output path.
 * @param format The format specifier to use for the path.
 * @param ... The format values.
 * @return Any resultant error, if any.
 * @since 2025/12/09
 */
sjme_errorCode sjme_path_parseF(
	sjme_attrOutNotNull sjme_attrOutOverwrite sjme_path* outPath,
	sjme_attrInNotNull sjme_attrFormatArg sjme_lpcstr format,
	...) sjme_attrFormatOuter(1, 2);
	
/**
 * Resolves the input path against the given path, the resultant path will
 * be in a subdirectory unless @a inPath is absolute.
 * 
 * @param outPath The output path.
 * @param inPath The input path to resolve against.
 * @return Any resultant error, if any.
 * @since 2025/12/09
 */
sjme_errorCode sjme_path_resolveP(
	sjme_attrOutNotNull sjme_attrOutModify sjme_path* outPath,
	sjme_attrInNotNull const sjme_path* inPath);
	
/**
 * Resolves the input path against the given path, the resultant path will
 * be in a subdirectory unless @c subPath is absolute. This is the same
 * as calling @link sjme_path_parse() @endlink and then resolving against
 * the parsed path.
 * 
 * @param outPath The output path.
 * @param inPath The input path.
 * subPath The path to resolve against, must end in @c NULL .
 * @return Any resultant error, if any.
 * @since 2025/12/09
 */
sjme_errorCode sjme_path_resolveS(
	sjme_attrOutNotNull sjme_attrOutModify sjme_path* outPath,
	sjme_attrInNotNull sjme_lpcstr inPath);
	
/**
 * Resolves the input path against the given path, the resultant path will
 * be in a subdirectory unless @c ... is absolute. Passing multiple paths is
 * the same as calling @link sjme_path_resolveP() @endlink multiple times
 * except in a single inline operation. The last passed pointer must always
 * be @c NULL so that the end of the paths is known.
 * 
 * @param outPath The output path.
 * @param ... The @code const sjme_path* @endcode to resolve against,
 * must end in @c NULL .
 * @return Any resultant error, if any.
 * @since 2025/12/09
 */
sjme_errorCode sjme_path_resolveV(
	sjme_attrOutNotNull sjme_attrOutModify sjme_path* outPath,
	...);

/**
 * Resolves the input path against the given path, the resultant path will
 * be in the same directory unless @c ... is absolute.
 * 
 * @param outPath The output path.
 * @param inPath The input path.
 * @return Any resultant error, if any.
 * @since 2025/12/09
 */
sjme_errorCode sjme_path_resolveSibling(
	sjme_attrOutNotNull sjme_attrOutModify sjme_path* outPath,
	sjme_attrInNotNull const sjme_path* inPath);
	
/**
 * Returns the sub-path of the given path with the specified name indexes.
 * 
 * @param outPath The output path.
 * @param inPath The input path.
 * @param beginDx The beginning index, inclusive.
 * @param endDx The ending index, exclusive.
 * @return Any resultant error, if any.
 * @since 2025/12/09
 */
sjme_errorCode sjme_path_subPath(
	sjme_attrOutNotNull sjme_attrOutOverwrite sjme_path* outPath,
	sjme_attrInNotNull const sjme_path* inPath,
	sjme_attrInPositive sjme_jint beginDx,
	sjme_attrInPositive sjme_jint endDx);

/**
 * Obtains the user's home directory.
 * 
 * @param outPath The output path.
 * @return Any resultant error, if any.
 * @since 2025/12/10
 */
sjme_errorCode sjme_path_userHome(
	sjme_attrOutNotNull sjme_attrOutOverwrite sjme_path* outPath);
	
#if 0
/**
 * Calculates and returns the base name of the path, removing any and all
 * directory components.
 * 
 * @param inOutPath The input/output path.
 * @param inOutPathLen The length of the input/output path.
 * @return Any resultant error, if any.
 * @since 2025/12/09
 */
sjme_errorCode sjme_path_baseName(
	sjme_attrInOutNotNullBuf(inOutPathLen) sjme_lpstr inOutPath,
	sjme_attrInPositive sjme_jint inOutPathLen);

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
 * @param outIsRoot Is this the root directory?
 * @return Any resultant error, if any.
 * Returns @link SJME_ERROR_NO_SUCH_ELEMENT
 * if the root component was requested and there was none.
 * @since 2024/08/10
 */
sjme_errorCode sjme_path_getName(
	sjme_attrInNotNullBuf(inPathLen) sjme_lpcstr inPath,
	sjme_attrInPositive sjme_jint inPathLen,
	sjme_attrInNegativeOnePositive sjme_jint inName,
	sjme_attrOutNullable sjme_lpcstr* outBase,
	sjme_attrOutNullable sjme_jint* outLen,
	sjme_jboolean* outIsRoot);

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
 * @param outIsRoot Is this the root directory?
 * @return Any resultant error, if any.
 * Returns @link SJME_ERROR_NO_SUCH_ELEMENT @endlink
 * if the root component was requested and there was none.
 * @since 2024/08/10
 */
sjme_errorCode sjme_path_getNameF(
	sjme_attrInNotNullBuf(inPathLen) sjme_lpcstr inPath,
	sjme_attrInPositive sjme_jint inPathLen,
	sjme_attrInNegativeOnePositive sjme_jint inName,
	sjme_attrOutNullable sjme_lpcstr* outBase,
	sjme_attrOutNullable sjme_jint* outBaseDx,
	sjme_attrOutNullable sjme_lpcstr* outEnd,
	sjme_attrOutNullable sjme_jint* outEndDx,
	sjme_attrOutNullable sjme_jint* outLen,
	sjme_attrOutNullable sjme_jint* outCount,
	sjme_jboolean* outIsRoot);

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
	sjme_attrInNotNullBuf(inPathLen) sjme_lpcstr inPath,
	sjme_attrInPositive sjme_jint inPathLen,
	sjme_attrOutNotNull sjme_attrOutPositive sjme_jint* outCount);

/**
 * Checks whether the given path is a directory or not.
 * 
 * @param inPath The input path.
 * @param inPathLen The input path length.
 * @param isDirectory The result of whether this is a directory or not.
 * @return Any resultant error, if any.
 * @since 2025/12/09
 */
sjme_errorCode sjme_path_isDirectory(
	sjme_attrInNotNullBuf(inPathLen) sjme_lpcstr inPath,
	sjme_attrInPositive sjme_jint inPathLen,
	sjme_attrOutNotNull sjme_jboolean* isDirectory);

/**
 * Checks whether the given path and offset is a directory separator or not.
 * 
 * @param inPath The input path.
 * @param inPathOff The offset into the path.
 * @param inPathLen The input path length.
 * @param isDirectory The result of whether this is a directory separator or
 * not.
 * @param outFollowing Optional output pointer for the following path
 * components of the directory separator, if this is a path separator. If it
 * is not then it will be the read character.
 * @return Any resultant error, if any.
 * @since 2025/12/09
 */
sjme_errorCode sjme_path_isDirectorySep(
	sjme_attrInNotNullBuf(inPathLen) sjme_lpcstr inPath,
	sjme_attrInPositive sjme_jint inPathOff,
	sjme_attrInPositive sjme_jint inPathLen,
	sjme_attrOutNotNull sjme_jboolean* isDirectory,
	sjme_attrOutNullable sjme_lpcstr* outFollowing);
	
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
	sjme_attrInNotNullBuf(inPathLen) sjme_lpcstr inPath,
	sjme_attrInPositive sjme_jint inPathLen,
	sjme_attrOutNotNull sjme_jboolean* hasRoot);

/**
 * Normalizes the given path, removing any relative components.
 * 
 * @param inOutPath The input/output path.
 * @param inOutPathOff The offset into the input/output path.
 * @param inOutPathLen The length of the input/output path.
 * @param requireAbsolute If @link SJME_JNI_TRUE @endlink then the resultant
 * path must be an absolute path.
 * @return Any resultant error, if any.
 * @since 2025/12/09
 */
sjme_errorCode sjme_path_normalize(
	sjme_attrInOutNotNullBuf(inOutPathLen) sjme_lpstr inOutPath,
	sjme_attrInPositive sjme_jint inOutPathOff,
	sjme_attrInPositive sjme_jint inOutPathLen,
	sjme_attrInValue sjme_jboolean requireAbsolute);
	
/**
 * Resolves the given path onto the given path buffer.
 * 
 * @param inOutPath The resultant path to be modified.
 * @param inOutPathLen The size of the path buffer.
 * @param subPath The sub-path to resolve against.
 * @param subPathLen The sub-path length, if @c INT32_MAX this means all
 * of it.
 * @return Any resultant error, if any.
 * @since 2024/08/09
 */
sjme_errorCode sjme_path_resolveAppend(
	sjme_attrInOutNotNullBuf(outPathLen) sjme_lpstr inOutPath,
	sjme_attrInPositiveNonZero sjme_jint inOutPathLen,
	sjme_attrInNotNullBuf(subPathLen) sjme_lpcstr subPath,
	sjme_attrInPositiveNonZero sjme_jint subPathLen);

/**
 * Returns the user's home directory.
 * 
 * @param outPath The output path.
 * @param outPathLen The length of the output path.
 * @return Any resultant error, if any.
 * @since 2025/12/08
 */
sjme_errorCode sjme_path_userHome(
	sjme_attrOutNotNullBuf(outPathLen) sjme_lpstr outPath,
	sjme_attrInPositiveNonZero sjme_jint outPathLen);
#endif
	
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
