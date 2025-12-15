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
	
/**
 * Generic file system path.
 *
 * @since 2025/12/09
 */
typedef struct sjme_path sjme_path;

/**
 * Parses the given path, how this is handled along with the result depends
 * on the implementation function. If any input path is determined to be
 * not valid then the appropriate error shall be returned.
 * 
 * @param path The current working path for processing.
 * @param outFLimit The limit to the number of characters.
 * @param outFStr The base pointer, if this is set to a non-@c NULL pointer
 * then @a outFLimit must also be valid and the parser will append the
 * given string to the working path.
 * @param walkPath The input path being processed, when a piece of the path
 * has been processed accordingly this shall increment the pointer
 * accordingly.
 * @return Any resultant error, if any.
 * @since 2025/12/11
 */
typedef sjme_errorCode (*sjme_path_styleParseFunc)(
	sjme_attrInOutNotNull sjme_attrOutModify sjme_path* path,
	sjme_attrOutNotNull sjme_attrOutOverwrite sjme_jint* outFLimit,
	sjme_attrOutNotNull sjme_attrOutOverwrite sjme_lpcstr* outFStr,
	sjme_attrInOutNotNull sjme_lpcstr* walkPath);

/**
 * Function for taking a single path.
 *
 * @param path The path being looked at.
 * @return Any resultant error, if any.
 * @since 2025/12/11
 */
typedef sjme_errorCode (*sjme_path_stylePathFunc)(
	sjme_attrInOutNotNull sjme_attrOutModify sjme_path* path);

/**
 * Subtring specifier for paths.
 *
 * @since 2015/12/12
 */
typedef struct sjme_path_styleSub
{
	/** The number of characters. */
	sjme_jbyte len;

	/** The characters which make up the path. */
	sjme_lpcstr str;
} sjme_path_styleSub;

/**
 * The specific type of path style to use.
 *
 * @since 2025/12/11
 */
typedef enum sjme_path_styleType
{
	/** None path style, all paths are not valid. */
	SJME_PATH_STYLE_NONE = 0,

	/** POSIX paths. */
	SJME_PATH_STYLE_POSIX = 1,

	/** DOS paths. */
	SJME_PATH_STYLE_DOS = 2,

	/** VFAT paths, DOS with less restrictions. */
	SJME_PATH_STYLE_VFAT = 3,

	/** Windows paths, VFAT + UNC. */
	SJME_PATH_STYLE_WINDOWS = 4,
	
	/** The number of path styles. */
	SJME_NUM_PATH_STYLES,
} sjme_path_styleType;

struct sjme_path_style
{
	/** The style type of this path, this is self referencing. */
	sjme_path_styleType type;
	
	/** Family specific check for validity. */
	sjme_path_stylePathFunc check;
	
	/**
	 * Parses the root component of the path.
	 *
	 * If there is no root then @link SJME_ERROR_NO_SUCH_ELEMENT @endlink
	 * shall be returned to indicate this.
	 */
	sjme_path_styleParseFunc parseRoot;

	/**
	 * Parses the next name of the path.
	 *
	 * If no more names remain, then @link SJME_ERROR_NO_SUCH_ELEMENT @endlink
	 * shall be returned to indicate this.
	 */
	sjme_path_styleParseFunc parseName;

	/** Finalization stage for path parsing, perform any final processing. */
	sjme_path_stylePathFunc parseFinalize;
	
	/** The primary and alternative directory separator. */
	sjme_path_styleSub dirSep[2];

	/** The path separator. */
	sjme_path_styleSub pathSep;

	/** Does this style not support relative paths? */
	sjme_jboolean requireAbsolute;

	/** Current directory dot style. */
	sjme_path_styleSub dot[2];

	/** Parent directory dot style. */
	sjme_path_styleSub dotDot[2];
};

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

struct sjme_path
{
	/** The characters which make up the path. */
	sjme_cchar chars[SJME_MAX_PATH];

	/** Must always be zero. */
	sjme_jint zero;

	/** The flags for this path. */
	sjme_jint flags;

	/** The path style, this determines how paths are parsed and handled. */
	const sjme_path_style* style;

	/** The length of the entire path. */
	sjme_jushort length;
	
	/** The names within this path. */ 
	sjme_jushort nameCount;

	/**
	 * The offsets for each path name. Every name that prefaces the last name
	 * shall end in a directory separator.
	 */
	sjme_jushort names[SJME_MAX_PATH_DEPTH + 1];
};

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
 * Checks if the string has a valid directory separator.
 * 
 * @param path The input path.
 * @param string The string to check.
 * @param noAlt Do not permit the alternative directory separator.
 * @return Any resultant error, if any. Will
 * be @link SJME_ERROR_NO_SUCH_ELEMENT @endlink if there is no directory
 * seperator.
 * @since 2025/12/13
 */
sjme_errorCode sjme_path_checkDirSep(
	sjme_attrInNotNull const sjme_path* path,
	sjme_attrInNotNull sjme_lpcstr string,
	sjme_attrInValue sjme_jboolean noAlt);

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
 * Parses the given path using the default path style provider.
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
 * Parses the given path with format specifiers using the default path
 * style provider.
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
 * Parses the given path using the specified style provider.
 *
 * @param style The style to use for the path.
 * @param outPath The output path.
 * @param strPath The string based path.
 * @return Any resultant error, if any.
 * @since 2025/12/11
 */
sjme_errorCode sjme_path_parseY(
	sjme_attrInValue sjme_path_styleType style,
	sjme_attrOutNotNull sjme_attrOutOverwrite sjme_path* outPath,
	sjme_attrInNotNull sjme_lpcstr strPath);

/**
 * Parses the given path using the specified style provider.
 *
 * @param style The style to use for the path.
 * @param outPath The output path.
 * @param strPath The string based path.
 * @return Any resultant error, if any.
 * @since 2025/12/13
 */
sjme_errorCode sjme_path_parseYP(
	sjme_attrInValue const sjme_path_style* style,
	sjme_attrOutNotNull sjme_attrOutOverwrite sjme_path* outPath,
	sjme_attrInNotNull sjme_lpcstr strPath);
	
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
 * @param nal The native abstraction layer to use, if @c NULL then a default
 * is used.
 * @param outPath The output path.
 * @return Any resultant error, if any.
 * @since 2025/12/10
 */
sjme_errorCode sjme_path_userHome(
	sjme_attrInNullable const sjme_nal* nal,
	sjme_attrOutNotNull sjme_attrOutOverwrite sjme_path* outPath);

/** The available path styles. */
extern const sjme_path_style sjme_path_styles[SJME_NUM_PATH_STYLES];
	
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
