/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Engine scaffolding.
 * 
 * @since 2021/09/06
 */

#ifndef SQUIRRELJME_SCAFFOLD_H
#define SQUIRRELJME_SCAFFOLD_H

#include "sjmerc.h"
#include "error.h"
#include "video.h"

/* Anti-C++. */
#ifdef __cplusplus
#ifndef SJME_CXX_IS_EXTERNED
#define SJME_CXX_IS_EXTERNED
#define SJME_CXX_SQUIRRELJME_SCAFFOLD_H
extern "C"
{
#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Indicates the reason why an engine is not available.
 * 
 * @since 2021/12/30
 */
typedef enum sjme_engineScaffoldUnavailableType
{
	/** It is actually available. */
	SJME_ENGINE_SCAFFOLD_IS_AVAILABLE = 0,
	
	/** The ROM is missing. */
	SJME_ENGINE_SCAFFOLD_MISSING_ROM,
	
	/** The ROM is not compatible with this engine. */
	SJME_ENGINE_SCAFFOLD_INCOMPATIBLE_ROM,
	
	/** The number of these types. */
	NUM_SJME_ENGINE_SCAFFOLD_UNAVAILABLE_TYPE
} sjme_engineScaffoldUnavailableType;

/**
 * Configuration for an engine within SquirrelJME, this is used to initialize
 * the virtual machine and everything it needs.
 * 
 * @since 2022/01/02
 */
typedef struct sjme_engineConfig
{
	/** The pointer to the ROM to use. */
	const sjme_jubyte* romPointer;
	
	/** The size of the ROM that exists. */
	sjme_jint romSize;
	
	/** Is the ROM dynamically loaded? */
	sjme_jboolean romIsAllocated;
	
	/** Screen width. */
	sjme_jint screenWidth;
	
	/** Screen height. */
	sjme_jint screenHeight;
	
	/** The pixel format to use for the display. */
	sjme_pixelFormat screenPixelFormat;
	
	/** Screen pixel data, if using an already existing buffer. */
	void* screenPixels;
} sjme_engineConfig;

/**
 * This is the scaffold for an engine between the common engine layer and the
 * specific engine implementation.
 * 
 * @since 2021/12/19
 */
typedef struct sjme_engineScaffold
{
	/** The name of the engine. */
	const char* const name;
	
	/**
	 * Checks if this scaffold and engine are available.
	 * 
	 * @param config The configuration of the engine.
	 * @param why Why is this engine not available, this is optional and may
	 * be @c NULL .
	 * @param error The error state.
	 * @return Will return @c true if available, otherwise not. 
	 * @since 2021/12/30
	 */
	sjme_jboolean (*isAvailable)(sjme_engineConfig* config,
		sjme_engineScaffoldUnavailableType* why, sjme_error* error);
} sjme_engineScaffold;

/** Scaffolds which are available for use. */
extern const sjme_engineScaffold* const sjme_engineScaffolds[];

/** SpringCoat engine. */
extern const sjme_engineScaffold sjme_engineScaffoldSpringCoat;

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
#ifdef SJME_CXX_SQUIRRELJME_SCAFFOLD_H
}
#undef SJME_CXX_SQUIRRELJME_SCAFFOLD_H
#undef SJME_CXX_IS_EXTERNED
#endif /* #ifdef SJME_CXX_SQUIRRELJME_SCAFFOLD_H */
#endif /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_SCAFFOLD_H */
