/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Dynamic Library loading.
 * 
 * @file
 * @since 2024/03/27
 */

#ifndef SJME_C_DYLIB_H
#define SJME_C_DYLIB_H

#include "sjme/stdTypes.h"
#include "sjme/error.h"
#include "sjme/debug.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_DYLIB_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

#if defined(SJME_CONFIG_HAS_OS_WINDOWS) && \
	!defined(SJME_CONFIG_HAS_OS_WINDOWS_WINE)
	/** Symbol is exported through a library. */
	#define sjme_attrExport __declspec(dllexport)
#elif defined(SJME_CONFIG_HAS_GCC) || \
	defined(SJME_CONFIG_HAS_CLANG)
	/** Symbol is exported through a library. */
	#define sjme_attrExport __attribute__((visibility("default")))
#else
	/** Symbol is exported through a library. */
	#define sjme_attrExport
#endif
	
#if defined(SJME_CONFIG_HAS_MSVC)
	/** Select any symbol (weakly) that exists, if possible. */
	#define sjme_attrSelectAnyWeak __declspec(selectany)
#elif defined(SJME_CONFIG_HAS_GCC) || \
	defined(SJME_CONFIG_HAS_CLANG)
	/** Select any symbol (weakly) that exists, if possible. */
	#define sjme_attrSelectAnyWeak __attribute__((weak))
#else
	/** Select any symbol (weakly) that exists, if possible. */
	#define sjme_attrSelectAnyWeak
#endif

#if defined(SJME_CONFIG_HAS_GCC) || defined(SJME_CONFIG_HAS_CLANG)
	/** Symbol is hidden in a library. */
	#define SJME_DYLIB_HIDDEN __attribute__((visibility("hidden")))
#else
	/** Symbol is hidden in a library. */
	#define SJME_DYLIB_HIDDEN
#endif

/**
 * Opaque dynamic library type.
 * 
 * @since 2024/03/27
 */
typedef sjme_pointer sjme_dylib;

/**
 * Closes the given dynamic library.
 * 
 * @param inLib The library to close. 
 * @return Any error if it occurs.
 * @since 2024/03/27
 */
sjme_errorCode sjme_dylib_close(
	sjme_attrInNotNull sjme_dylib inLib);

/**
 * Looks up the given symbol in the library.
 * 
 * @param inLib The library to look within.
 * @param inSymbol The symbol to obtain from the library.
 * @param outPtr The resultant pointer of the symbol.
 * @return Any error if it occurs.
 * @since 2024/03/27
 */
sjme_errorCode sjme_dylib_lookup(
	sjme_attrInNotNull sjme_dylib inLib,
	sjme_attrInNotNull sjme_lpcstr inSymbol,
	sjme_pointer* outPtr);

/**
 * Calculates the name of the given library for the current system.
 * 
 * @param inLibName The input library name.
 * @param inLibSuffix The suffix for the library. 
 * @param outName The resultant library name.
 * @param outLen The length of the output buffer.
 * @return Any error code as applicable.
 * @since 2024/04/13
 */
sjme_errorCode sjme_dylib_name(
	sjme_attrInNotNull sjme_lpcstr inLibName,
	sjme_attrInNullable sjme_lpcstr inLibSuffix,
	sjme_attrOutNotNullBuf(outLen) sjme_lpstr outName,
	sjme_attrInPositive sjme_jint outLen);

/**
 * Opens a dynamic library.
 * 
 * @param libPath The path to the library to open.
 * @param outLib The resultant opened library.
 * @return Any error if it occurs.
 * @since 2024/03/27
 */
sjme_errorCode sjme_dylib_open(
	sjme_attrInNotNull sjme_lpcstr libPath,
	sjme_attrInOutNotNull sjme_dylib* outLib);

/**
 * Returns the current executable as a dynamic library.
 * 
 * @param outLib The resultant library which points to ourself.
 * @return Any resultant error, if any.
 * @since 2024/08/17
 */
sjme_errorCode sjme_dylib_self(
	sjme_attrInOutNotNull sjme_dylib* outLib);

/** The debug handlers to use. */
extern sjme_attrExport
	sjme_debug_handlerFunctions* sjme_debug_handlers;

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_DYLIB_H
}
		#undef SJME_CXX_SQUIRRELJME_DYLIB_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_DYLIB_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_DYLIB_H */
