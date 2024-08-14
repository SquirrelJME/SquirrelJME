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

#include "sjme/list.h"
#include "sjme/nvm.h"
#include "sjme/romInternal.h"
#include "sjme/stream.h"
#include "sjme/seekable.h"

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
typedef struct sjme_rom_libraryBase sjme_rom_libraryBase;

/** Synthetic library structure. */
typedef sjme_rom_libraryBase* sjme_rom_library;

/** List of ROM libraries. */
SJME_LIST_DECLARE(sjme_rom_library, 0);

/** The type ID of ROM libraries. */
#define SJME_TYPEOF_BASIC_sjme_rom_library SJME_BASIC_TYPE_ID_OBJECT

/**
 * Common cache between suites and libraries.
 *
 * @since 2023/12/20
 */
typedef struct sjme_rom_cache
{
	/** The allocation pool to use. */
	sjme_alloc_pool* allocPool;

	/** Wrapped object, if applicable. */
	sjme_frontEnd frontEnd;
} sjme_rom_cache;

/**
 * Internal cache for ROM libraries.
 *
 * @since 2023/12/12
 */
typedef struct sjme_rom_libraryCache
{
	/** Common cache data. */
	sjme_rom_cache common;

	/** Cached size of the library. */
	sjme_jint size;

	/** Is raw access checked? */
	sjme_jboolean checkedRawAccess : 1;

	/** Is raw access valid. */
	sjme_jboolean validRawAccess : 1;
} sjme_rom_libraryCache;

/**
 * Internal cache for ROM suites.
 *
 * @since 2023/12/12
 */
typedef struct sjme_rom_suiteCache
{
	/** Common cache data. */
	sjme_rom_cache common;

	/** Libraries that exist within the suite. */
	sjme_list_sjme_rom_library* libraries;
} sjme_rom_suiteCache;

/**
 * Functions used to access a single library.
 *
 * @since 2023/12/12
 */
typedef struct sjme_rom_libraryFunctions sjme_rom_libraryFunctions;

struct sjme_rom_libraryBase
{
	/** Common data. */
	sjme_nvm_commonBase common;
	
	/** Functions used to access library information. */
	const sjme_rom_libraryFunctions* functions;
	
	/** The handle, may be to a seekable. */
	sjme_pointer handle;

	/** The library ID. */
	sjme_jint id;

	/** The library name. */
	sjme_lpcstr name;

	/** Hash of the library name. */
	sjme_jint nameHash;

	/** Internal cache, used by internal library functions. */
	sjme_rom_libraryCache cache;
};

/**
 * Functions used to access a suite, which is an entire ROM.
 *
 * @since 2023/12/12
 */
typedef struct sjme_rom_suiteFunctions sjme_rom_suiteFunctions;

/**
 * Initializes the library cache.
 *
 * @param inLibrary The input library.
 * @return Any resultant error, if any.
 * @since 2023/12/29
 */
typedef sjme_errorCode (*sjme_rom_libraryInitCacheFunc)(
	sjme_attrInNotNull sjme_rom_library inLibrary);

typedef sjme_errorCode (*sjme_rom_libraryPathFunc)();

/**
 * Access the direct raw data of a given library.
 *
 * @param inLibrary The library to access in a raw fashion.
 * @param dest The destination buffer.
 * @param srcPos The source position within the file.
 * @param length The number of bytes to read.
 * @return Any resultant error, if any.
 * @since 2023/12/30
 */
typedef sjme_errorCode (*sjme_rom_libraryRawData)(
	sjme_attrInNotNull sjme_rom_library inLibrary,
	sjme_attrOutNotNullBuf(length) sjme_pointer dest,
	sjme_attrInPositive sjme_jint srcPos,
	sjme_attrInPositive sjme_jint length);

/**
 * Returns the raw size of the library, also can be used to check if raw
 * access is supported in opaque handlers.
 *
 * @param inLibrary The input library
 * @param outSize The output raw size, if the result is @c -1 then it indicates
 * that this operation is not supported on the given library and it should
 * not try to access resources and otherwise using raw data access.
 * @return Any resultant error code.
 * @since 2023/12/30
 */
typedef sjme_errorCode (*sjme_rom_libraryRawSizeFunc)(
	sjme_attrInNotNull sjme_rom_library inLibrary,
	sjme_attrOutNotNull sjme_jint* outSize);

/**
 * Opens the given resource as a stream.
 *
 * @param inLibrary The library to read the resource from.
 * @param outStream The resultant stream for accessing data.
 * @param resourceName The name of the resource.
 * @return Any resultant error code.
 * @since 2023/12/30
 */
typedef sjme_errorCode (*sjme_rom_libraryResourceStreamFunc)(
	sjme_attrInNotNull sjme_rom_library inLibrary,
	sjme_attrOutNotNull sjme_stream_input* outStream,
	sjme_attrInNotNull sjme_lpcstr resourceName);

/**
 * Function used to initialize the suite cache.
 *
 * @param inSuite The input suite.
 * @return Any error state.
 * @since 2023/12/15
 */
typedef sjme_errorCode (*sjme_rom_suiteInitCacheFunc)(
	sjme_attrInNotNull sjme_rom_suite inSuite);

/**
 * Returns the ID of the library for the given suite.
 *
 * @param functions The suite functions.
 * @param targetSuite The current suite being accessed.
 * @param targetLibrary The library to get the ID of.
 * @param outId The output library ID, cannot be zero.
 * @return Any resultant error code.
 * @since 2023/12/18
 */
typedef sjme_errorCode (*sjme_rom_suiteLibraryId)(
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
	/** Size of the cache type. */
	sjme_jint uncommonTypeSize;

	/** Initializes the library cache. */
	sjme_rom_libraryInitCacheFunc initCache;

	/** Function to get the path of a library. */
	sjme_rom_libraryPathFunc path;

	/** Access of raw bytes of the input library. */
	sjme_rom_libraryRawData rawData;

	/** The size of this library. */
	sjme_rom_libraryRawSizeFunc rawSize;

	/** Access a resource as a stream. */
	sjme_rom_libraryResourceStreamFunc resourceStream;
};

struct sjme_rom_suiteFunctions
{
	/** Size of the cache type. */
	sjme_jint uncommonTypeSize;

	/** Initialize suite cache. */
	sjme_rom_suiteInitCacheFunc initCache;

	/** Returns the ID of the given library. */
	sjme_rom_suiteLibraryId libraryId;

	/** Lists the libraries in the suite. */
	sjme_rom_suiteListLibrariesFunc list;

	/** Loads a single library. */
	sjme_rom_suiteLoadLibraryFunc loadLibrary;
};

struct sjme_rom_suiteBase
{
	/** Common data. */
	sjme_nvm_commonBase common;
	
	/** Functions. */
	const sjme_rom_suiteFunctions* functions;

	/** Internal cache, used by suite implementations. */
	sjme_rom_suiteCache cache;
};

/**
 * Initializes a library from a Zip.
 *
 * @param pool The pool to use for allocations.
 * @param outLibrary The resultant library.
 * @param base The base address where the Zip is.
 * @param length The length of the Zip.
 * @return Any resultant error, if any.
 * @since 2024/01/01
 */
sjme_errorCode sjme_rom_libraryFromZipMemory(
	sjme_attrInNotNull sjme_alloc_pool* pool,
	sjme_attrOutNotNull sjme_rom_library* outLibrary,
	sjme_attrInNotNull sjme_cpointer base,
	sjme_attrInPositive sjme_jint length);

/**
 * Initializes a library from a Zip.
 *
 * @param pool The pool to use for allocations.
 * @param outLibrary The resultant library.
 * @param seekable The seekable to access the Zip through.
 * @return Any resultant error, if any.
 * @since 2024/01/01
 */
sjme_errorCode sjme_rom_libraryFromZipSeekable(
	sjme_attrInNotNull sjme_alloc_pool* pool,
	sjme_attrOutNotNull sjme_rom_library* outLibrary,
	sjme_attrInNotNull sjme_seekable seekable);

/**
 * Calculates the hash of the given library.
 *
 * @param library The library to calculate the hash of.
 * @param outHash The output hash.
 * @return On any resultant error.
 * @since 2023/12/27
 */
sjme_errorCode sjme_rom_libraryHash(
	sjme_attrInNotNull sjme_rom_library library,
	sjme_attrOutNotNull sjme_jint* outHash);

/**
 * Makes a virtual library from the given functions.
 *
 * @param pool The pool to allocate within.
 * @param outLibrary The output library.
 * @param inFunctions The functions which define how to access the library.
 * @param inFrontEnd Input front end initialization, is optional.
 * @return Any error code.
 * @since 2023/12/29
 */
sjme_errorCode sjme_rom_libraryNew(
	sjme_attrInNotNull sjme_alloc_pool* pool,
	sjme_attrOutNotNull sjme_rom_library* outLibrary,
	sjme_attrInNotNull const sjme_rom_libraryFunctions* inFunctions,
	sjme_attrInNullable const sjme_frontEnd* inFrontEnd);

/**
 * Reads from a library directly to access its raw bytes.
 *
 * @param library The library to access.
 * @param destPtr The destination buffer.
 * @param srcPos The source position.
 * @param length The number of bytes to read.
 * @return Any resultant error if any.
 * @since 2023/12/30
 */
sjme_errorCode sjme_rom_libraryRawRead(
	sjme_attrInNotNull sjme_rom_library library,
	sjme_attrOutNotNullBuf(length) sjme_pointer destPtr,
	sjme_attrInPositive sjme_jint srcPos,
	sjme_attrInPositive sjme_jint length);

/**
 * Reads from a library directly to access its raw bytes, this variant of
 * the function provides more control over offsets so that it can more easily
 * be used with iterators and such.
 *
 * @param library The library to access.
 * @param destPtr The destination buffer.
 * @param destOffset The offset into the destination.
 * @param srcPos The source position.
 * @param srcOffset The source offset.
 * @param length The number of bytes to read.
 * @return Any resultant error if any.
 * @since 2023/12/30
 */
sjme_errorCode sjme_rom_libraryRawReadIter(
	sjme_attrInNotNull sjme_rom_library library,
	sjme_attrOutNotNullBuf(length) void* destPtr,
	sjme_attrInPositive sjme_jint destOffset,
	sjme_attrInPositive sjme_jint srcPos,
	sjme_attrInPositive sjme_jint srcOffset,
	sjme_attrInPositive sjme_jint length);

/**
 * Returns the raw size of the library.
 *
 * @param library The library to get the raw size of.
 * @param outSize The resultant size.
 * @return Any resultant error code.
 * @since 2023/12/30
 */
sjme_errorCode sjme_rom_libraryRawSize(
	sjme_attrInNotNull sjme_rom_library library,
	sjme_attrOutNotNull sjme_jint* outSize);

/**
 * Obtains the given resource as a stream.
 *
 * @param library The library to get the resource from.
 * @param outStream The resultant stream.
 * @param rcName The name of the resource to obtain.
 * @return On any errors, if any.
 * @since 2023/12/31
 */
sjme_errorCode sjme_rom_libraryResourceAsStream(
	sjme_attrInNotNull sjme_rom_library library,
	sjme_attrOutNotNull sjme_stream_input* outStream,
	sjme_attrInNotNull sjme_lpcstr rcName);

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
 * Combines multiple suites into one.
 *
 * @param pool The pool to allocate within.
 * @param outSuite The output suite.
 * @param inSuites The input suites.
 * @param numInSuites The number of input suites.
 * @return
 */
sjme_errorCode sjme_rom_suiteFromMerge(
	sjme_attrInNotNull sjme_alloc_pool* pool,
	sjme_attrOutNotNull sjme_rom_suite* outSuite,
	sjme_attrInNotNull sjme_rom_suite* inSuites,
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
sjme_errorCode sjme_rom_suiteFromPayload(
	sjme_attrInNotNull sjme_alloc_pool* pool,
	sjme_attrOutNotNull sjme_rom_suite* outSuite,
	sjme_attrInNotNull const sjme_payload_config* payloadConfig);

/**
 * Initializes a suite from a Zip.
 *
 * @param pool The pool to use for allocations.
 * @param outSuite The resultant suite.
 * @param seekable The seekable to access the Zip through.
 * @return Any resultant error, if any.
 * @since 2024/08/11
 */
sjme_errorCode sjme_rom_suiteFromZipSeekable(
	sjme_attrInNotNull sjme_alloc_pool* pool,
	sjme_attrOutNotNull sjme_rom_suite* outSuite,
	sjme_attrInNotNull sjme_seekable seekable);

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

/**
 * Makes a virtual suite from the given functions.
 *
 * @param pool The pool to allocate within.
 * @param outSuite The output suite.
 * @param inFunctions The functions which define how to access the suite.
 * @param inFrontEnd Input front end initialization, is optional.
 * @return Any error code.
 * @since 2023/12/15
 */
sjme_errorCode sjme_rom_suiteNew(
	sjme_attrInNotNull sjme_alloc_pool* pool,
	sjme_attrOutNotNull sjme_rom_suite* outSuite,
	sjme_attrInNotNull const sjme_rom_suiteFunctions* inFunctions,
	sjme_attrInNullable const sjme_frontEnd* inFrontEnd);

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
