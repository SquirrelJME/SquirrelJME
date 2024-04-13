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
 * @since 2024/03/27
 */

#ifndef SQUIRRELJME_DYLIB_H
#define SQUIRRELJME_DYLIB_H

#include "sjme/nvm.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_DYLIB_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Opaque dynamic library type.
 * 
 * @since 2024/03/27
 */
typedef void* sjme_dylib;

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
	void** outPtr);

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
