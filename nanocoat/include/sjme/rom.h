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
#include "sjme/list.h"

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
 * Internal cache for ROM libraries.
 *
 * @since 2023/12/12
 */
typedef struct sjme_rom_libraryCache sjme_rom_libraryCache;

/**
 * Standard ROM library structure.
 *
 * @since 2023/12/12
 */
typedef struct sjme_rom_libraryCore
{
	/** Internal cache. */
	sjme_rom_libraryCache* cache;
} sjme_rom_libraryCore;

/** Synthetic library structure. */
typedef sjme_rom_libraryCore* sjme_rom_library;

/** List of ROM libraries. */
SJME_LIST_DECLARE(sjme_rom_library, 0);

/** The type ID of ROM libraries. */
#define SJME_BASIC_TYPEOF_sjme_rom_library SJME_BASIC_TYPE_ID_OBJECT

/**
 * Functions used to access a single library.
 *
 * @since 2023/12/12
 */
typedef struct sjme_rom_libraryFunctions sjme_rom_libraryFunctions;

/**
 * Functions used to access a suite, which is an entire ROM.
 *
 * @since 2023/12/12
 */
typedef struct sjme_rom_suiteFunctions sjme_rom_suiteFunctions;

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

/**
 * Function used to initialize the suite cache.
 *
 * @param functions The functions definitions and potential internal state.
 * @param pool The pool to allocate within, if needed.
 * @param targetSuite The suite to initialize the cache for.
 * @return Any error state.
 * @since 2023/12/15
 */
typedef sjme_errorCode (*sjme_rom_suiteInitCacheFunc)(
	sjme_attrInNotNull const sjme_rom_suiteFunctions* functions,
	sjme_attrInNotNull sjme_alloc_pool* pool,
	sjme_attrInOutNotNull sjme_rom_suite targetSuite);

/**
 * Returns the ID of the library for the given suite.
 *
 * @param functions The suite functions.
 * @param targetSuite The current suite being accessed.
 * @param targetLibrary The library to get the ID of.
 * @param outId The output library ID.
 * @return Any resultant error code.
 * @since 2023/12/18
 */
typedef sjme_errorCode (*sjme_rom_suiteLibraryId)(
	sjme_attrInNotNull const sjme_rom_suiteFunctions* functions,
	sjme_attrInNotNull sjme_rom_suite targetSuite,
	sjme_attrInNotNull sjme_rom_library targetLibrary,
	sjme_attrOutNotNull sjme_jint* outId);

/**
 * Determines the list of libraries within the suite.
 *
 * @param targetSuite The suite the request is being made in.
 * @param outLibraries The output library list.
 * @return Any resultant error code.
 * @since 2023/12/21
 */
typedef sjme_errorCode (*sjme_rom_suiteListLibrariesFunc)(
	sjme_attrInNotNull sjme_rom_suite targetSuite,
	sjme_attrOutNotNull sjme_list_sjme_rom_library** outLibraries);

typedef sjme_errorCode (*sjme_rom_suiteLoadLibraryFunc)();

struct sjme_rom_libraryFunctions
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
};

struct sjme_rom_suiteFunctions
{
	/** Wrapped object, if applicable. */
	sjme_frontEnd frontEnd;

	/** Initialize suite cache. */
	sjme_rom_suiteInitCacheFunc initCache;

	/** Returns the ID of the given library. */
	sjme_rom_suiteLibraryId libraryId;

	/** Lists the libraries in the suite. */
	sjme_rom_suiteListLibrariesFunc list;

	/** Loads a single library. */
	sjme_rom_suiteLoadLibraryFunc loadLibrary;
};

struct sjme_rom_suiteCore
{
	/** Internal cache. */
	sjme_rom_suiteCache* cache;

	/** Functions. */
	const sjme_rom_suiteFunctions* functions;
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
sjme_errorCode sjme_rom_fromMerge(
	sjme_attrInNotNull sjme_alloc_pool* pool,
	sjme_attrOutNotNull sjme_rom_suite* outSuite,
	sjme_attrInNotNull sjme_rom_suite** inSuites,
	sjme_attrInPositive sjme_jint numInSuites);

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
sjme_errorCode sjme_rom_fromPayload(
	sjme_attrInNotNull sjme_alloc_pool* pool,
	sjme_attrOutNotNull sjme_rom_suite* outSuite,
	sjme_attrInNotNull const sjme_payload_config* payloadConfig);

/**
 * Makes a virtual suite from the given functions.
 *
 * @param pool The pool to allocate within.
 * @param outSuite The output suite.
 * @param inFunctions The functions which define how to access the suite.
 * @return Any error code.
 * @since 2023/12/15
 */
sjme_errorCode sjme_rom_newSuite(
	sjme_attrInNotNull sjme_alloc_pool* pool,
	sjme_attrOutNotNull sjme_rom_suite* outSuite,
	sjme_attrInNotNull const sjme_rom_suiteFunctions* inFunctions);

/**
 * Resolves the class path library by their ID.
 *
 * @param inSuite The suite to look within.
 * @param inIds The IDs to obtain.
 * @param outLibs The output libraries.
 * @return Any resultant error state.
 * @since 2023/12/18
 */
sjme_errorCode sjme_rom_resolveClassPathById(
	sjme_attrInNotNull sjme_rom_suite inSuite,
	sjme_attrInNotNull const sjme_list_sjme_jint* inIds,
	sjme_attrOutNotNull sjme_list_sjme_rom_library** outLibs);

/**
 * Resolves the class path library by their name.
 *
 * @param inSuite The suite to look within.
 * @param inNames The names to obtain.
 * @param outLibs The output libraries.
 * @return Any resultant error state.
 * @since 2023/12/18
 */
sjme_errorCode sjme_rom_resolveClassPathByName(
	sjme_attrInNotNull sjme_rom_suite inSuite,
	sjme_attrInNotNull const sjme_list_sjme_lpcstr* inNames,
	sjme_attrOutNotNull sjme_list_sjme_rom_library** outLibs);

/**
 * Returns all of the libraries which are available within this suite.
 *
 * @param inSuite The input suite.
 * @param outLibs The resultant libraries.
 * @return Any resultant error state.
 * @since 2023/12/20
 */
sjme_errorCode sjme_rom_suiteLibraries(
	sjme_attrInNotNull sjme_rom_suite inSuite,
	sjme_attrOutNotNull sjme_list_sjme_rom_library** outLibs);

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
