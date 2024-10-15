/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Payload seed, defines a default payload that can be included.
 * 
 * @since 2023/07/27
 */

#ifndef SQUIRRELJME_PAYLOADSEED_H
#define SQUIRRELJME_PAYLOADSEED_H

#include "sjme/config.h"
#include "sjme/payload.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_PAYLOADSEED_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

#if defined(SJME_CONFIG_ROM0_HEADER)
	#include SJME_CONFIG_ROM0_HEADER
#endif

#if defined(SJME_CONFIG_ROM1_HEADER)
	#include SJME_CONFIG_ROM1_HEADER
#endif

#if defined(SJME_CONFIG_ROM2_HEADER)
	#include SJME_CONFIG_ROM2_HEADER
#endif

#if defined(SJME_CONFIG_ROM3_HEADER)
	#include SJME_CONFIG_ROM3_HEADER
#endif

#if defined(SJME_CONFIG_ROM4_HEADER)
	#include SJME_CONFIG_ROM4_HEADER
#endif

#if defined(SJME_CONFIG_ROM5_HEADER)
	#include SJME_CONFIG_ROM5_HEADER
#endif

#if defined(SJME_CONFIG_ROM6_HEADER)
	#include SJME_CONFIG_ROM6_HEADER
#endif

#if defined(SJME_CONFIG_ROM7_HEADER)
	#include SJME_CONFIG_ROM7_HEADER
#endif

#if defined(SJME_CONFIG_ROM8_HEADER)
	#include SJME_CONFIG_ROM8_HEADER
#endif

#if defined(SJME_CONFIG_ROM9_HEADER)
	#include SJME_CONFIG_ROM9_HEADER
#endif

const sjme_payload_config sjme_payload_config_data =
{
	.roms =
	{
		SJME_CONFIG_ROM0_ADDR,
		SJME_CONFIG_ROM1_ADDR,
		SJME_CONFIG_ROM2_ADDR,
		SJME_CONFIG_ROM3_ADDR,
		SJME_CONFIG_ROM4_ADDR,
		SJME_CONFIG_ROM5_ADDR,
		SJME_CONFIG_ROM6_ADDR,
		SJME_CONFIG_ROM7_ADDR,
		SJME_CONFIG_ROM8_ADDR,
		SJME_CONFIG_ROM9_ADDR,
	},
};

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_PAYLOADSEED_H
}
		#undef SJME_CXX_SQUIRRELJME_PAYLOADSEED_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_PAYLOADSEED_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_PAYLOADSEED_H */
