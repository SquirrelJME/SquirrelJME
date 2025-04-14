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

#include "sjme/nvm/nvm.h"
#include "sjme/gfxConst.h"

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
 * Frame buffer related storage and information within SquirrelJME, this must
 * be compatible with @c PencilShelf in SquirrelJME.
 * 
 * @since 2023/11/24
 */
typedef struct sjme_gfx_framebuffer sjme_gfx_framebuffer;

/**
 * Stores the state of a renderer within the framebuffer.
 * 
 * @since 2023/11/24
 */
typedef struct sjme_gfx_graphics sjme_gfx_graphics;

/**
 * Obtains a graphics drawing instance of the framebuffer.
 * 
 * @param framebuffer The framebuffer to get the graphics from.
 * @param inOutGraphics The output graphics to initialize into.
 * @param pixelFormat The @c sjme_gfx_pixelFormat used for the draw.
 * @param bufferWidth The buffer width, this is the scanline width of the
 * buffer.
 * @param bufferHeight The buffer height.
 * @param buffer The target buffer to draw to, this is cast to the correct
 * buffer format.
 * @param offset The offset to the start of the buffer.
 * @param palette The color palette, may be @c NULL.
 * @param surfaceX Starting surface X coordinate.
 * @param surfaceY Starting surface Y coordinate.
 * @param surfaceWidth Surface width.
 * @param surfaceHeight Surface height.
 * @return Returns @c SJME_JNI_TRUE on success.
 * @since 2023/11/24
 */
typedef sjme_jboolean (*sjme_gfx_getGraphics)(
	sjme_attrInNotNull sjme_gfx_framebuffer* framebuffer,
	sjme_attrInOutNotNull sjme_gfx_graphics* inOutGraphics,
	sjme_attrInRange(0, SJME_NUM_GFX_PIXEL_FORMATS)
		sjme_gfx_pixelFormat pixelFormat,
	sjme_attrInPositiveNonZero sjme_jint bufferWidth,
	sjme_attrInPositiveNonZero sjme_jint bufferHeight,
	sjme_attrInNotNull sjme_pointer buffer,
	sjme_attrInPositive sjme_jint offset,
	sjme_attrInNullable sjme_pointer palette,
	sjme_attrInValue sjme_jint surfaceX,
	sjme_attrInValue sjme_jint surfaceY,
	sjme_attrInPositiveNonZero sjme_jint surfaceWidth,
	sjme_attrInPositiveNonZero sjme_jint surfaceHeight);

/**
 * Functions for drawing in a given graphics accordingly.
 * 
 * @since 2023/11/24
 */
typedef struct sjme_gfx_pencilFunctions
{
	/** TODO. */
	sjme_jint todo;
} sjme_gfx_pencilFunctions;

struct sjme_gfx_graphics
{
	/** The framebuffer this state draws into */
	sjme_gfx_framebuffer* framebuffer;
	
	/** The pixel format used. */
	sjme_gfx_pixelFormat pixelFormat;
	
	/** Pencil functions for this graphics. */
	sjme_gfx_pencilFunctions pencilFunctions;
};

struct sjme_gfx_framebuffer
{
	/** The pointer to the framebuffer data, may be @c NULL if opaque. */
	sjme_pointer pixels;
	
	/** The width of the framebuffer in pixels. */
	sjme_jint width;
	
	/** The height of the framebuffer in pixels. */
	sjme_jint height;
	
	/** The scanline length of the framebuffer in pixels. */
	sjme_jint pitch;
	
	/** The true scanline length of the framebuffer in bytes. */
	sjme_jint pitchBytes;
	
	/** Function to get a graphics drawing instance for this framebuffer. */
	sjme_gfx_getGraphics getGraphics;
	
	/** If the framebuffer is allocated extra here, this has room for it. */
	sjme_jubyte rawReservedBuffer[sjme_flexibleArrayCount];
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
