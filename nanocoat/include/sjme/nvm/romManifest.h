/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Simplified parsing of manifest files, this is not meant to replace fully
 * in-depth manifest management where maps are used.
 * 
 * @since 2026/06/24
 */

#ifndef SJME_C_SQUIRRELJME_ROMMANIFEST_H
#define SJME_C_SQUIRRELJME_ROMMANIFEST_H

#include "sjme/stream.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_ROMMANIFEST_H

extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/** The maximum length permitted for manifest keys, before error. */
#define SJME_NVM_ROM_MANIFEST_KEY_LENGTH 74

/** The maximum length permitted for manifest values, before error. */
#define SJME_NVM_ROM_MANIFEST_VALUE_LENGTH 384

/**
 * Stores a single manifest key/value mapping.
 *
 * @since 2026/06/24
 */
typedef struct sjme_nvm_rom_manifestMap
{
	/** The key. */
	sjme_jchar key[SJME_NVM_ROM_MANIFEST_KEY_LENGTH];

	/** The value. */
	sjme_jchar value[SJME_NVM_ROM_MANIFEST_VALUE_LENGTH];
} sjme_nvm_rom_manifestMap;

/**
 * A single step of the manifest process.
 *
 * @since 2026/06/24
 */
typedef struct sjme_nvm_rom_manifestStep
{
	/** Which attribute table is this within? */
	sjme_jchar attr[SJME_NVM_ROM_MANIFEST_VALUE_LENGTH];

	/** The current map value. */
	sjme_nvm_rom_manifestMap map;

	/** Interstate step state. */
	struct
	{
		/** Look ahead codepoint. */
		sjme_jint lookahead;
	} parse;
} sjme_nvm_rom_manifestStep;

/**
 * Parses the next manifest key from the input stream.
 *
 * @param inputStream The input stream to parse.
 * @param inOutStep The output manifest step data.
 * @return If the end of file is reached
 * then @link SJME_ERROR_END_OF_FILE @endlink will be returned, otherwise
 * any other error.
 * @since 2026/06/26
 */
sjme_errorCode sjme_nvm_rom_manifestParseNext(
	sjme_attrInNotNull sjme_stream_input inputStream,
	sjme_attrInOutNotNull sjme_nvm_rom_manifestStep* inOutStep);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_ROMMANIFEST_H
}
#undef SJME_CXX_SQUIRRELJME_ROMMANIFEST_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_ROMMANIFEST_H */
#endif /* #ifdef __cplusplus */

#endif /* SJME_C_SQUIRRELJME_ROMMANIFEST_H */