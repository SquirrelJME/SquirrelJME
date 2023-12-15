/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * ROM related structures, this replicates what is in @c JarPackageShelf.
 * 
 * @since 2023/12/12
 */

#ifndef SQUIRRELJME_ROM_H
#define SQUIRRELJME_ROM_H

#include "sjme/nvm.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_ROM_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Standard ROM library structure.
 *
 * @since 2023/12/12
 */
typedef struct sjme_rom_library sjme_rom_library;

/**
 * Internal cache for ROM libraries.
 *
 * @since 2023/12/12
 */
typedef struct sjme_rom_libraryCache sjme_rom_libraryCache;

/**
 * Internal cache for ROM suites.
 *
 * @since 2023/12/12
 */
typedef struct sjme_rom_suiteCache sjme_rom_suiteCache;

typedef sjme_errorCode (*sjme_rom_libraryPathFunc)();

typedef sjme_errorCode (*sjme_rom_libraryRawData)();

typedef sjme_errorCode (*sjme_rom_libraryResourceStreamFunc)();

typedef sjme_errorCode (*sjme_rom_libraryResourceDirectFunc)();

typedef sjme_errorCode (*sjme_rom_librarySizeFunc)();

typedef sjme_errorCode (*sjme_rom_suiteListLibrariesFunc)();

typedef sjme_errorCode (*sjme_rom_suiteLoadLibraryFunc)();

/**
 * Functions used to access a single library.
 *
 * @since 2023/12/12
 */
typedef struct sjme_rom_libraryFunctions
{
	/** Wrapped object, if applicable. */
	sjme_frontEnd frontEnd;

	/** Function to get the path of a library. */
	sjme_rom_libraryPathFunc path;

	/** The size of this library. */
	sjme_rom_librarySizeFunc size;

	/** Access of raw bytes of the input library. */
	sjme_rom_libraryRawData rawData;

	/** Open resource as a stream. */
	sjme_rom_libraryResourceStreamFunc resourceStream;

	/** Direct resource access, if available. */
	sjme_rom_libraryResourceDirectFunc resourceDirect;
} sjme_rom_libraryFunctions;

/**
 * Functions used to access a suite, which is an entire ROM.
 *
 * @since 2023/12/12
 */
typedef struct sjme_rom_suiteFunctions
{
	/** Wrapped object, if applicable. */
	sjme_frontEnd frontEnd;

	/** Lists the libraries in the suite. */
	sjme_rom_suiteListLibrariesFunc list;

	/** Loads a single library. */
	sjme_rom_suiteLoadLibraryFunc loadLibrary;
} sjme_rom_suiteFunctions;

struct sjme_rom_library
{
	/** Internal cache. */
	sjme_rom_libraryCache* cache;
};

struct sjme_rom_suite
{
	/** Internal cache. */
	sjme_rom_suiteCache* cache;

	/** Functions. */
	sjme_rom_suiteFunctions functions;
};

/**
 * Combines multiple suites into one.
 *
 * @param pool The pool to allocate within.
 * @param outSuite The output suite.
 * @param inSuites The input suites.
 * @param numInSuites The number of input suites.
 * @return
 */
sjme_errorCode sjme_rom_combineSuites(
	sjme_attrInNotNull sjme_alloc_pool* pool,
	sjme_attrOutNotNull sjme_rom_suite** outSuite,
	sjme_attrInNotNull sjme_rom_suite** inSuites,
	sjme_attrInPositive sjme_jint numInSuites);

/**
 * Makes a virtual suite from the given functions.
 *
 * @param pool The pool to allocate within.
 * @param outSuite The output suite.
 * @param inFunctions The functions which define how to access the suite.
 * @return Any error code.
 * @since 2023/12/15
 */
sjme_errorCode sjme_rom_makeVirtualSuite(
	sjme_attrInNotNull sjme_alloc_pool* pool,
	sjme_attrOutNotNull sjme_rom_suite** outSuite,
	sjme_attrInNotNull const sjme_rom_suiteFunctions* inFunctions);

/**
 * Scans the payload for suites
 *
 * @param pool The pool to allocate within.
 * @param outSuite The output resultant suite, if there would be nothing in
 * here then this outputs @c NULL .
 * @param payloadConfig The payload configuration used.
 * @return Any error status.
 * @since 2023/12/15
 */
sjme_errorCode sjme_rom_scanPayload(
	sjme_attrInNotNull sjme_alloc_pool* pool,
	sjme_attrOutNotNull sjme_rom_suite** outSuite,
	sjme_attrInNotNull const sjme_payload_config* payloadConfig);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_ROM_H
}
		#undef SJME_CXX_SQUIRRELJME_ROM_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_ROM_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_ROM_H */
