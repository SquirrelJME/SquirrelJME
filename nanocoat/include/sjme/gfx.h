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
typedef struct sjme_gfx_graphics sjme_gfx_graphics;

/**
 * The pixel format used for graphics, matches @c UIPixelFormat.
 * 
 * @since 2023/11/25
 */
typedef enum sjme_gfx_pixelFormat
{
	/** 32-bit RGBA (@c uint32_t ) [Java ME Standard]. */
	SJME_GFX_PIXEL_FORMAT_INT_RGBA8888 = 0,
	
	/** 32-bit RGB (@c uint32_t ) [Java ME Standard]. */
	SJME_GFX_PIXEL_FORMAT_INT_RGB888 = 1,
	
	/** 16-bit RGBA4444. (@c uint16_t ) [Java ME Standard]. */
	SJME_GFX_PIXEL_FORMAT_SHORT_RGBA4444 = 2,
	
	/** 16-bit RGB565. (@c uint16_t ) [Java ME Standard]. */
	SJME_GFX_PIXEL_FORMAT_SHORT_RGB565 = 3,
	
	/** 16-bit RGB555. (@c uint16_t ). */
	SJME_GFX_PIXEL_FORMAT_SHORT_RGB555 = 4,
	
	/** 16-bit ABGR1555. (@c uint16_t ) [PlayStation 2]. */
	SJME_GFX_PIXEL_FORMAT_SHORT_ABGR1555 = 5,
	
	/** 65536 Colors (@c uint16_t ). */
	SJME_GFX_PIXEL_FORMAT_SHORT_INDEXED65536 = 6,
	
	/** 256 Colors (@c uint8_t ). */
	SJME_GFX_PIXEL_FORMAT_BYTE_INDEXED256 = 7,
	
	/** Packed 16 colors (4-bit). (packed @c uint8_t ) */
	SJME_GFX_PIXEL_FORMAT_PACKED_INDEXED4 = 8,
	
	/** Packed 4 Colors (2-bit). (packed @c uint8_t ) */
	SJME_GFX_PIXEL_FORMAT_PACKED_INDEXED2 = 9,
	
	/** Packed 2 colors (1-bit). (packed @c uint8_t ) */
	SJME_GFX_PIXEL_FORMAT_PACKED_INDEXED1 = 10,
	
	/** The number of pixel formats. */
	SJME_NUM_GFX_PIXEL_FORMATS = 11
} sjme_gfx_pixelFormat;

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
 * @return Returns @c JNI_TRUE on success.
 * @since 2023/11/24
 */
typedef jboolean (*sjme_gfx_getGraphics)(
	sjme_attrInNotNull sjme_gfx_framebuffer* framebuffer,
	sjme_attrInOutNotNull sjme_gfx_graphics* inOutGraphics,
	sjme_attrInRange(0, SJME_NUM_GFX_PIXEL_FORMATS)
		sjme_gfx_pixelFormat pixelFormat,
	sjme_attrInPositiveNonZero jint bufferWidth,
	sjme_attrInPositiveNonZero jint bufferHeight,
	sjme_attrInNotNull void* buffer,
	sjme_attrInPositive jint offset,
	sjme_attrInNullable void* palette,
	sjme_attrInValue jint surfaceX,
	sjme_attrInValue jint surfaceY,
	sjme_attrInPositiveNonZero jint surfaceWidth,
	sjme_attrInPositiveNonZero jint surfaceHeight);

/**
 * Functions for drawing in a given graphics accordingly.
 * 
 * @since 2023/11/24
 */
typedef struct sjme_gfx_pencilFunctions
{
	/** TODO. */
	jint todo;
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
	void* pixels;
	
	/** The width of the framebuffer in pixels. */
	jint width;
	
	/** The height of the framebuffer in pixels. */
	jint height;
	
	/** The scanline length of the framebuffer in pixels. */
	jint pitch;
	
	/** The true scanline length of the framebuffer in bytes. */
	jint pitchBytes;
	
	/** Function to get a graphics drawing instance for this framebuffer. */
	sjme_gfx_getGraphics getGraphics;
	
	/** If the framebuffer is allocated extra here, this has room for it. */
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
