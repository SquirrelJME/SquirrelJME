/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Graphics drawing operations.
 * 
 * @since 2023/11/24
 */

#ifndef SQUIRRELJME_GFX_H
#define SQUIRRELJME_GFX_H

#include "sjme/nvm.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_GFX_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Frame buffer related storage and information within SquirrelJME.
 * 
 * @since 2023/11/24
 */
typedef struct sjme_gfx_framebuffer sjme_gfx_framebuffer;

/**
 * Stores the state of a renderer within the framebuffer.
 * 
 * @since 2023/11/24
 */
typedef struct sjme_gfx_graphics
{
	/** The framebuffer this state draws into */
	sjme_gfx_framebuffer* framebuffer;
} sjme_gfx_graphics;

/**
 * Obtains a graphics drawing instance of the framebuffer.
 * 
 * @param framebuffer The framebuffer to get the graphics from.
 * @param graphics The output graphics to initialize into.
 * @return Returns @c JNI_TRUE on success.
 * @since 2023/11/24
 */
typedef jboolean (*sjme_gfx_getGraphics)(
	sjme_attrInNotNull sjme_gfx_framebuffer* framebuffer,
	sjme_attrInOutNotNull sjme_gfx_graphics* graphics);

/**
 * Functions for drawing in this framebuffer accordingly.
 * 
 * @since 2023/11/24
 */
typedef struct sjme_gfx_framebufferFuncs
{
	/** Gets the graphics from the framebuffer. */
	sjme_gfx_getGraphics getGraphics;
} sjme_gfx_framebufferFuncs;

struct sjme_gfx_framebuffer
{
	/** The pointer to the framebuffer data. */
	void* pixels;
	
	/** The width of the framebuffer in pixels. */
	jint width;
	
	/** The height of the framebuffer in pixels. */
	jint height;
	
	/** The scanline length of the framebuffer in pixels. */
	jint pitch;
	
	/** The true scanline length of the framebuffer in bytes. */
	jint pitchBytes;
	
	/** Functions to use when drawing into this framebuffer. */
	sjme_gfx_framebufferFuncs functions;
	
	/** If the framebuffer is allocated, this has room for it. */
	jubyte rawReservedBuffer[sjme_flexibleArrayCount];
};

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_GFX_H
}
		#undef SJME_CXX_SQUIRRELJME_GFX_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_GFX_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_GFX_H */
