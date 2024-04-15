/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Static SquirrelJME payload setup, can be used at compile time to set some
 * initial boot settings and otherwise embedded into the image.
 * 
 * @since 2023/07/27
 */

#ifndef SQUIRRELJME_PAYLOAD_H
#define SQUIRRELJME_PAYLOAD_H

#include "sjme/nvm.h"
#include "sjme/config.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_PAYLOAD_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/** The maximum ROMs permitted in the payload. */
#define SJME_NVM_PAYLOAD_MAX_ROMS 10

typedef struct sjme_payload_rom
{
	/** Is this ROM active? */
	sjme_jboolean isActive;

	/** Is this a library? If not then it is a ROM. */
	sjme_jboolean isLibrary;

	/** The address of the ROM data. */
	void* address;

	/** The length of the ROM data. */
	sjme_jint length;
} sjme_payload_rom;

/**
 * Contains the payload information.
 * 
 * @since 2023/07/27
 */
struct sjme_payload_config
{
	/** ROMs that are available built-in. */
	sjme_payload_rom roms[SJME_NVM_PAYLOAD_MAX_ROMS];
};

/** The static payload configuration. */
extern const sjme_payload_config sjme_payload_config_data;

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_PAYLOAD_H
}
		#undef SJME_CXX_SQUIRRELJME_PAYLOAD_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_PAYLOAD_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_PAYLOAD_H */
