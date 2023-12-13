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
 * Standard Suite structure.
 *
 * @since 2023/12/12
 */
typedef struct sjme_rom_suite sjme_rom_suite;

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
	/** Lists the libraries in the suite. */
	sjme_rom_suiteListLibrariesFunc list;

	/** Loads a single library. */
	sjme_rom_suiteLoadLibraryFunc libraries;
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
