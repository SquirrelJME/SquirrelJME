/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Missing JNI definitions.
 * 
 * @since 2024/03/18
 */

#ifndef SQUIRRELJME_JNIMISSING_H
#define SQUIRRELJME_JNIMISSING_H

#include <jni.h>

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_JNIMISSING_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Finds a symbol within the given library via the equivalent of @c dlsym() .
 * 
 * @param libHandle The library to look within.
 * @param inName The name of the symbol to obtain.
 * @return The pointer to the symbol which may be a function or other type
 * exported from the library, will return @c NULL if it was not found.
 * @since 2024/03/18
 */
sjme_attrUnused void* JNICALL JVM_FindLibraryEntry(
	sjme_attrInNotNull void* libHandle,
	sjme_attrInNotNull sjme_lpcstr inName);

/**
 * Loads a native library into memory via the equivalent of @c dlopen() .
 * 
 * @param inName The library name to load. 
 * @return Returns a handle for the loaded object which is dependent
 * on the operating system, otherwise returns @c NULL on failure.
 * @since 2024/03/18
 */
sjme_attrUnused void* JNICALL JVM_LoadLibrary(
	sjme_attrInNotNull sjme_lpcstr inName);

/**
 * Unloads a library that was previously loaded by @c JVM_LoadLibrary .
 * 
 * @param libHandle The library handle to close.
 * @since 2024/03/18 
 */
sjme_attrUnused void JNICALL JVM_UnloadLibrary(
	sjme_attrInNotNull void* libHandle);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_JNIMISSING_H
}
		#undef SJME_CXX_SQUIRRELJME_JNIMISSING_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_JNIMISSING_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_JNIMISSING_H */
